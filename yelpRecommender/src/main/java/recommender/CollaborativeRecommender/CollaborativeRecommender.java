package recommender.CollaborativeRecommender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import entity.Prediction;
import entity.ReviewCF;

/**
 * Filtro colaborativo basado en reviews de yelp
 * El calculo de la prediccion se realiza r = u + Bu + Bi
 * Donde u es el promedio general, Bu es el promedio para el usuario
 * Bi es el promedio para el negocio.
 * La idea es mantener todo en memoria para faciliar el recalculo de los promedios
 * y la generacion de las predicciones.
 * 
 * TODO
 * Se propone agregar dos funciones adicionales para probar: r = u + Bu + Bi + f(x)
 * 
 */
public class CollaborativeRecommender {

	private Hashtable<String, List<ReviewCF>> business;
	private Hashtable<String, List<ReviewCF>> user;
	private double mean;
	
	public CollaborativeRecommender(){
		business = new Hashtable<String, List<ReviewCF>>();
		user = new Hashtable<String, List<ReviewCF>>();
	}
	
	public void init() throws IOException{
		BufferedReader bf = new BufferedReader(new FileReader(new File("/Users/Pisco/Downloads/yelp/new/review_cf.csv")));
		String str = bf.readLine();//Encabezado
		str = bf.readLine();
		int count = 0;
		while(str != null){
			String[] linea = str.split(";");
			String userId = linea[1].replace("\"", "");
			String businessId = linea[2].replace("\"", "");
			int stars = Integer.parseInt(linea[3]);
			double businessStars = Double.parseDouble(linea[4]);
			double userStars = Double.parseDouble(linea[5]);
			double computedStars = Double.parseDouble(linea[6]);
			ReviewCF review = new ReviewCF(businessId, userId, stars, businessStars, userStars, computedStars);
			count++;
			List<ReviewCF> lista;
			if(business.containsKey(businessId)){
				lista = business.get(businessId);
			}else{
				lista = new ArrayList<ReviewCF>();
			}
			lista.add(review);
			business.put(businessId, lista);
			
			if(user.containsKey(userId)){
				lista = user.get(userId);
			}else{
				lista = new ArrayList<ReviewCF>();
			}
			lista.add(review);
			user.put(userId, lista);
			mean += stars; 
			str = bf.readLine();
		}
		bf.close();
		mean = mean/(double) count;
	}

	/**
	 * Genera un recomendacion de tamaño size para un usuario dado
	 * @param userId
	 * @param size
	 * @return La lista con la lista de ids de los negocios
	 */
	public List<Prediction> recommendItem(String userId, int size){
		double userMean = getuserMean(userId);
		List<String> businessReviewed = getBusinessReviewed(userId);
		
		//Recorrer los business y calcular la prediccion
		Iterator<String> it = business.keySet().iterator();
		List<Prediction> predictions = new ArrayList<Prediction>();
		while(it.hasNext()){
			String businessId = it.next();
			if(!businessReviewed.contains(businessId)){
				double businessMean = getBusinessMean(businessId);
				double prediction = getPrediction(userMean, businessMean);
				predictions.add(new Prediction(businessId, prediction));
			}			
		}
		
		Collections.sort(predictions);
		return predictions.subList(0, size);
	}
	
	private double getPrediction(double userMean, double businessMean){
		return mean + (userMean - mean) + (businessMean - mean);
	}
	
	private List<String> getBusinessReviewed(String userId){
		List<String> businessReviewed = new ArrayList<String>();
		if(user.containsKey(userId)){
			List<ReviewCF> userReviews = user.get(userId);
			for(int i = 0 ; i < userReviews.size() ; i++){
				ReviewCF review = userReviews.get(i);
				String businessId = review.getBusinessId();
				if(!businessReviewed.contains(businessId)){
					businessReviewed.add(businessId);
				}
			}
		}
		return businessReviewed;
	}
	
	private double getuserMean(String userId){
		double mean = 0D;
		if(user.containsKey(userId)){
			mean = user.get(userId).get(0).getUserStars();
		}
		return mean;
	}
	
	private double getBusinessMean(String businessId){
		double mean = 0D;
		if(business.containsKey(businessId)){
			mean = business.get(businessId).get(0).getBusinessStars();
		}
		return mean;
	}
	
	public double getMean() {
		return mean;
	}
	
	public void addRating(String userId, String businessId, double stars){
		//Agregar el rating
		//Recalcular los promedios
		//Todo se hace en memoria no se tiene persistencia
		
	}
	
	public void getRMSE(){
		
	}
	
	public void getMAE(){
		
	}
	
	public void getPrecision(){
		
	}
	
	public void getRecall(){
		
	}

	public static void main(String[] args) throws IOException {
		
		CollaborativeRecommender r = new CollaborativeRecommender();
		long ini = System.currentTimeMillis();
		r.init();
		System.out.println("Time: "+((System.currentTimeMillis()-ini)/1000));
		System.out.println("Mean: "+ r.getMean());
		ini = System.currentTimeMillis();
		List<Prediction> pr = r.recommendItem("--0HEXd4W6bJI8k7E0RxTA", 10);
		System.out.println("Time: "+((System.currentTimeMillis()-ini)/1000));
		for(Prediction p : pr)
			System.out.println(p.getKey());
	}
}