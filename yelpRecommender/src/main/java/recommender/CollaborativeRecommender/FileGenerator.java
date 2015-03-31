package recommender.CollaborativeRecommender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import recommender.dayTimeRecommender.DayTime;

/**
 * Lee los archivos del dataset originales en csv y genera los archivos
 * necesarios para el funcionamiento del resto de la aplicacion
 * 
 * @author Pisco
 *
 */
public class FileGenerator {

	private Hashtable<String, Integer> userIdMapping;
	private Hashtable<String, Integer> businessIdMapping;
	private Hashtable<Integer, String> idUserMapping;
	private Hashtable<Integer, String> idBusinessMapping;
	private Hashtable<String, List<String>> neighborhoodsBusiness;
	private Hashtable<String, List<DayTime>> businessDayTime;

	private int reviewsSize;

	public FileGenerator() {
		userIdMapping = new Hashtable<String, Integer>();
		businessIdMapping = new Hashtable<String, Integer>();
		idUserMapping = new Hashtable<Integer, String>();
		idBusinessMapping = new Hashtable<Integer, String>();
		neighborhoodsBusiness = new Hashtable<String, List<String>>();
		businessDayTime = new Hashtable<String, List<DayTime>>();
	}

	public void generateFiles(String inDir, String outDir) {
		try {
			generateUserFile(inDir, outDir);
			generateBusinessFile(inDir, outDir);
			generateReviewFile(inDir, outDir);
			readNeighborhoodsFile(outDir);
			readCheckinsFile(outDir);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void generateBusinessFile(String inDir, String outDir) throws Exception {
		String file = inDir + "business.csv";
		String out = outDir + "businessID.csv";

		BufferedReader bf = new BufferedReader(new FileReader(new File(file)));
		FileWriter fw = new FileWriter(new File(out));

		String str = bf.readLine();// Encabezado
		str = bf.readLine();
		int id = 0;
		while (str != null) {
			String[] linea = str.trim().split(";");
			String businessId = linea[1].replace("\"", "").trim();
			fw.write(businessId + ";" + id + "\r\n");
			businessIdMapping.put(businessId, id);
			idBusinessMapping.put(id, businessId);
			str = bf.readLine();
			id++;
		}
		bf.close();
		fw.close();
	}

	/**
	 * Genera un CSV con userId;id Donde el id es un int generado
	 * 
	 * @throws Exception
	 */
	private void generateUserFile(String inDir, String outDir) throws Exception {
		String file = inDir + "user.csv";
		String out = outDir + "userID.csv";

		BufferedReader bf = new BufferedReader(new FileReader(new File(file)));
		FileWriter fw = new FileWriter(new File(out));

		String str = bf.readLine();// Encabezado
		str = bf.readLine();
		int id = 0;
		while (str != null) {
			String[] linea = str.trim().split(";");
			String userId = linea[1].replace("\"", "").trim();
			fw.write(userId + ";" + id + "\r\n");
			userIdMapping.put(userId, id);
			idUserMapping.put(id, userId);
			str = bf.readLine();
			id++;
		}
		bf.close();
		fw.close();
	}

	/**
	 * Genera un CSV con user_id;business_id;rating
	 * 
	 * @throws Exception
	 */
	private void generateReviewFile(String inDir, String outDir) throws Exception {
		String file = outDir + "review_cf.csv";
		String out = outDir + "review.csv";

		BufferedReader bf = new BufferedReader(new FileReader(new File(file)));
		FileWriter fw = new FileWriter(new File(out));

		String str = bf.readLine();// Encabezado
		str = bf.readLine();
		reviewsSize++;
		while (str != null) {
			String[] linea = str.trim().split(";");
			String userId = linea[1].replace("\"", "").trim();
			String businessId = linea[2].replace("\"", "").trim();
			int stars = Integer.parseInt(linea[3].trim());
			fw.write(userIdMapping.get(userId) + ";" + businessIdMapping.get(businessId) + ";" + stars + "\r\n");
			str = bf.readLine();
			reviewsSize++;
		}
		bf.close();
		fw.close();
		generateItemRecommenderFiles(outDir);
	}

	private void generateItemRecommenderFiles(String outDir) throws Exception {
		// dir+size+"_itemRecommender.csv"
		String file = outDir + "review.csv";
		for (int i = 1; i < 11; i++) {
			BufferedReader bf = new BufferedReader(new FileReader(new File(file)));
			FileWriter fw = new FileWriter(new File(outDir + (i * 10) + "_itemRecommender.csv"));
			String str = bf.readLine();
			int j = 1;
			int limite = (reviewsSize * i * 10) / 100;
			while (str != null && j < limite) {
				String[] linea = str.trim().split(";");
				int userId = Integer.parseInt(linea[0].replace("\"", "").trim());
				int businessId = Integer.parseInt(linea[1].replace("\"", "").trim());
				int stars = Integer.parseInt(linea[2].trim());
				fw.write(userId + ";" + businessId + ";" + stars + "\r\n");
				str = bf.readLine();
				j++;
			}
			bf.close();
			fw.close();
		}
	}

	private void readNeighborhoodsFile(String outDir) throws Exception {
		String file = outDir + "neighborhoods.csv";
		BufferedReader bf = new BufferedReader(new FileReader(new File(file)));
		String[] encabezado = bf.readLine().replace("\"", "").trim().split(";");// Encabezado
		for (int i = 1; i < encabezado.length; i++) {
			neighborhoodsBusiness.put(encabezado[i].trim(), new ArrayList<String>());
		}
		String str = bf.readLine();
		while (str != null) {
			String[] linea = str.replace("\"", "").trim().split(";");
			String businessId = linea[1].replace("\"", "").trim();
			for (int i = 2; i < linea.length; i++) {
				int v = Integer.parseInt(linea[i].trim());
				if (v == 1) {
					String neighbor = encabezado[i - 1].trim();
					List<String> neighbors = neighborhoodsBusiness.get(neighbor);
					neighbors.add(businessId.trim());
					neighborhoodsBusiness.put(neighbor, neighbors);
				}
			}
			str = bf.readLine();
		}
		bf.close();
	}

	private void readCheckinsFile(String outDir) throws Exception {
		String file = outDir + "checkins.csv";
		BufferedReader bf = new BufferedReader(new FileReader(new File(file)));
		String str = bf.readLine();
		str = bf.readLine();
		while (str != null) {
			String[] linea = str.replace("\"", "").trim().split(";");
			String businessId = linea[1].replace("\"", "").trim();
			List<DayTime> dayTime = new ArrayList<DayTime>();

			int s01 = Integer.parseInt(linea[2].replace("\"", ""));
			dayTime.add(new DayTime(1, 0, s01));
			int s61 = Integer.parseInt(linea[3].replace("\"", ""));
			dayTime.add(new DayTime(1, 6, s61));
			int s101 = Integer.parseInt(linea[4].replace("\"", ""));
			dayTime.add(new DayTime(1, 10, s101));
			int s161 = Integer.parseInt(linea[5].replace("\"", ""));
			dayTime.add(new DayTime(1, 16, s161));
			int s201 = Integer.parseInt(linea[6].replace("\"", ""));
			dayTime.add(new DayTime(1, 20, s201));
			int s02 = Integer.parseInt(linea[7].replace("\"", ""));
			dayTime.add(new DayTime(2, 0, s02));
			int s62 = Integer.parseInt(linea[8].replace("\"", ""));
			dayTime.add(new DayTime(2, 6, s62));
			int s102 = Integer.parseInt(linea[9].replace("\"", ""));
			dayTime.add(new DayTime(2, 10, s102));
			int s162 = Integer.parseInt(linea[10].replace("\"", ""));
			dayTime.add(new DayTime(2, 16, s162));
			int s202 = Integer.parseInt(linea[11].replace("\"", ""));
			dayTime.add(new DayTime(2, 20, s202));
			int s03 = Integer.parseInt(linea[12].replace("\"", ""));
			dayTime.add(new DayTime(3, 0, s03));
			int s63 = Integer.parseInt(linea[13].replace("\"", ""));
			dayTime.add(new DayTime(3, 6, s63));
			int s103 = Integer.parseInt(linea[14].replace("\"", ""));
			dayTime.add(new DayTime(3, 10, s103));
			int s163 = Integer.parseInt(linea[15].replace("\"", ""));
			dayTime.add(new DayTime(3, 16, s163));
			int s203 = Integer.parseInt(linea[16].replace("\"", ""));
			dayTime.add(new DayTime(3, 20, s203));
			businessDayTime.put(businessId, dayTime);
			str = bf.readLine();
		}
		bf.close();
	}

	public int getUserGeneratedId(String userId) {
		return userIdMapping.get(userId);
	}

	public String getUserId(int userId) {
		return idUserMapping.get(userId);
	}

	public int getBusinessGeneratedId(String businessId) {
		return businessIdMapping.get(businessId);
	}

	public String getBusinessId(int businessId) {
		return idBusinessMapping.get(businessId);
	}

	public int getReviewsSize() {
		return reviewsSize;
	}

	public List<String> getBusinessInNeighbor(String neighborhood) {
		return neighborhoodsBusiness.get(neighborhood);
	}

	public List<DayTime> getBusinessDayTime(String business) {
		return businessDayTime.get(business);
	}

	public Set<String> getAllUsers() {
		return userIdMapping.keySet();
	}

	public Set<String> getAllNeighborhoods() {
		return neighborhoodsBusiness.keySet();
	}

	public static void main(String[] args) throws Exception {
		FileGenerator f = new FileGenerator();
		f.readNeighborhoodsFile("/Users/Pisco/Downloads/yelp/new/");
		f.readCheckinsFile("/Users/Pisco/Downloads/yelp/new/");
		System.out.println("!");
	}
}