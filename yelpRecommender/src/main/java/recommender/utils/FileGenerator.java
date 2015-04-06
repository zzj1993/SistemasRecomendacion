package recommender.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import recommender.dayTimeRecommender.DayTime;
import entity.Business;
import entity.ReviewCF;
import entity.ShortReview;

/**
 * Lee los archivos del dataset originales en csv y genera los archivos
 * necesarios para el funcionamiento del resto de la aplicacion
 * 
 * @author Pisco
 *
 */
public class FileGenerator {

	private final RecommendersInformation recommendersInformation;

	public FileGenerator(RecommendersInformation recommendersInformation) {
		this.recommendersInformation = recommendersInformation;
	}

	public void generateFiles(String inDir, String outDir) {
		try {
			System.out.println("Reading Users...");
			readUserFile(inDir);
			System.out.println("Reading Users Names...");
			readUserNames(outDir);
			System.out.println("Reading businesses...");
			readBusinessFile(inDir);
			System.out.println("Reading Reviews...");
			generateReviewFile(inDir, outDir);
			System.out.println("Reading neighborhoods...");
			readNeighborhoodsFile(outDir);
			System.out.println("Reading checkins...");
			readCheckinsFile(outDir);
			System.out.println("Reading Business Names...");
			readBusinessNames(outDir);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readBusinessNames(String outDir) throws IOException {
		String file = outDir + "businessInformation.csv";
		BufferedReader bf = new BufferedReader(new FileReader(new File(file)));
		String str = bf.readLine();// Encabezado
		str = bf.readLine();
		while (str != null) {
			String[] linea = str.trim().split(",");
			String businessId = linea[0].replace("\"", "").trim();
			String name;
			if (linea.length > 1) {
				name = linea[1].replace("\"", "").trim();
			} else {
				name = "Nameless Business";
			}
			String full_address = linea[2].replace("\"", "").trim();
			String city = linea[3].replace("\"", "").trim();
			String state = linea[4].replace("\"", "").trim();
			int review_count = Integer.parseInt(linea[6].replace("\"", "").trim());
			List<ReviewCF> revs =recommendersInformation.getBusinessReviews(businessId);
			List<ShortReview> reviews = new ArrayList<ShortReview>();
			if(revs != null){
				for(ReviewCF r : revs){
					String userName = recommendersInformation.getUserName(r.getUserId());
					reviews.add(new ShortReview(userName, r.getStars()));
				}
			}
			Business b = new Business(businessId, name, full_address, city, state, review_count,
					recommendersInformation.getBusinessNeighborhoods(businessId), reviews);
			recommendersInformation.addBusiness(b);
			str = bf.readLine();
		}
		bf.close();
	}

	private void readUserNames(String outDir) throws IOException {
		String file = outDir + "userNames.csv";
		BufferedReader bf = new BufferedReader(new FileReader(new File(file)));
		String str = bf.readLine();// Encabezado
		str = bf.readLine();
		while (str != null) {
			String[] linea = str.trim().split(",");
			String userId = linea[0].replace("\"", "").trim();
			String name;
			if (linea.length > 1) {
				name = linea[1].replace("\"", "").trim();
			} else {
				name = "Nameless User";
			}
			recommendersInformation.addUserName(userId, name);
			str = bf.readLine();
		}
		bf.close();
	}

	private void readBusinessFile(String inDir) throws Exception {
		String file = inDir + "business.csv";
		BufferedReader bf = new BufferedReader(new FileReader(new File(file)));
		String str = bf.readLine();// Encabezado
		str = bf.readLine();
		int id = 0;
		while (str != null) {
			String[] linea = str.trim().split(";");
			String businessId = linea[1].replace("\"", "").trim();
			recommendersInformation.addBusiness(businessId, id);
			str = bf.readLine();
			id++;
		}
		bf.close();
	}

	private void readUserFile(String inDir) throws Exception {
		String file = inDir + "user.csv";
		BufferedReader bf = new BufferedReader(new FileReader(new File(file)));
		String str = bf.readLine();// Encabezado
		str = bf.readLine();
		int id = 0;
		while (str != null) {
			String[] linea = str.trim().split(";");
			String userId = linea[1].replace("\"", "").trim();
			recommendersInformation.addUser(userId, id);
			str = bf.readLine();
			id++;
		}
		bf.close();
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
		while (str != null) {
			String[] linea = str.trim().split(";");
			String userId = linea[1].replace("\"", "").trim();
			String businessId = linea[2].replace("\"", "").trim();
			int stars = Integer.parseInt(linea[3].trim());
			fw.write(recommendersInformation.getUserGeneratedId(userId) + ";"
					+ recommendersInformation.getBusinessGeneratedId(businessId) + ";" + stars + "\r\n");
			str = bf.readLine();
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
			int limite = (recommendersInformation.getReviewsSize() * i * 10) / 100;
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
		// for (int i = 1; i < encabezado.length; i++) {
		// neighborhoodsBusiness.put(encabezado[i].trim(), new
		// ArrayList<String>());
		// }
		String str = bf.readLine();
		while (str != null) {
			String[] linea = str.replace("\"", "").trim().split(";");
			String businessId = linea[1].replace("\"", "").trim();
			for (int i = 2; i < linea.length; i++) {
				int v = Integer.parseInt(linea[i].trim());
				if (v == 1) {
					String neighborhood = encabezado[i - 1].trim();
					recommendersInformation.addNeighborhoodBusiness(neighborhood, businessId.trim());
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
			recommendersInformation.addBusinessDayTime(businessId, dayTime);
			str = bf.readLine();
		}
		bf.close();
	}
}