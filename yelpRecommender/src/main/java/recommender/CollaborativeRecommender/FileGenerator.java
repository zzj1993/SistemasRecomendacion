package recommender.CollaborativeRecommender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Hashtable;

/**
 * Lee los archivos del dataset originales en csv y genera los archivos necesarios para el funcionamiento del
 * resto de la aplicacion
 * @author Pisco
 *
 */
public class FileGenerator {

	private Hashtable<String, Integer> userIdMapping;
	private Hashtable<String, Integer> businessIdMapping;
	private Hashtable<Integer, String> idUserMapping;
	private Hashtable<Integer, String> idBusinessMapping;
	
	private int reviewsSize;
	
	public FileGenerator() {
		userIdMapping = new Hashtable<String, Integer>();
		businessIdMapping = new Hashtable<String, Integer>();
		idUserMapping = new Hashtable<Integer, String>();
		idBusinessMapping = new Hashtable<Integer, String>();
	}
	
	public void generateFiles(String inDir, String outDir){
		try {
			generateUserFile(inDir, outDir);
			generateBusinessFile(inDir, outDir);
			generateReviewFile(inDir, outDir);		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void generateBusinessFile(String inDir, String outDir) throws Exception{
		String file = inDir+"business.csv";
		String out = outDir+"businessID.csv";
		
		BufferedReader bf = new BufferedReader(new FileReader(new File(file)));
		FileWriter fw = new FileWriter(new File(out));

		String str = bf.readLine();// Encabezado
		str = bf.readLine();
		int id = 0;
		while (str != null) {
			String[] linea = str.split(";");
			String businessId = linea[1].replace("\"", "");
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
	 * Genera un CSV con userId;id
	 * Donde el id es un int generado
	 * @throws Exception
	 */
	private  void generateUserFile(String inDir, String outDir) throws Exception {
		String file = inDir+"user.csv";
		String out = outDir+"userID.csv";
		
		BufferedReader bf = new BufferedReader(new FileReader(new File(file)));
		FileWriter fw = new FileWriter(new File(out));

		String str = bf.readLine();// Encabezado
		str = bf.readLine();
		int id = 0;
		while (str != null) {
			String[] linea = str.split(";");
			String userId = linea[1].replace("\"", "");
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
	 * @throws Exception
	 */
	private  void generateReviewFile(String inDir, String outDir) throws Exception {
		String file = outDir+"review_cf.csv";
		String out = outDir+"review.csv";

		BufferedReader bf = new BufferedReader(new FileReader(new File(file)));
		FileWriter fw = new FileWriter(new File(out));

		String str = bf.readLine();// Encabezado
		str = bf.readLine();
		reviewsSize++;
		while (str != null) {
			String[] linea = str.split(";");
			String userId = linea[1].replace("\"", "");
			String businessId = linea[2].replace("\"", "");
			int stars = Integer.parseInt(linea[3]);
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
		String file = outDir+"review.csv";
		for(int i = 1 ; i < 11 ; i++){
			BufferedReader bf = new BufferedReader(new FileReader(new File(file)));
			FileWriter fw = new FileWriter(new File(outDir+(i*10)+"_itemRecommender.csv"));
			String str = bf.readLine();
			int j = 1;
			int limite = (reviewsSize*i*10)/100;
			while (str != null && j < limite) {
				String[] linea = str.split(";");
				int userId = Integer.parseInt(linea[0].replace("\"", ""));
				int businessId = Integer.parseInt(linea[1].replace("\"", ""));
				int stars = Integer.parseInt(linea[2]);
				fw.write(userId + ";" + businessId + ";" + stars + "\r\n");
				str = bf.readLine();
				j++;
			}
			bf.close();
			fw.close();
		}
	}

	public int getUserGeneratedId(String userId){
		return userIdMapping.get(userId);
	}
	
	public String getUserId(int userId){
		return idUserMapping.get(userId);
	}
	
	public int getBusinessGeneratedId(String businessId){
		return businessIdMapping.get(businessId);
	}
	
	public String getBusinessId(int businessId){
		return idBusinessMapping.get(businessId);
	}
	
	public int getReviewsSize(){
		return reviewsSize;
	}
}