package reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import entity.ReviewCF;

public class ReviewCFReader {
	
	private List<ReviewCF> reviewsModel;
	private Hashtable<String, List<ReviewCF>> business;
	private Hashtable<String, List<ReviewCF>> user;
	private double mean;
	
	public ReviewCFReader(){
		reviewsModel = new ArrayList<ReviewCF>();
		business = new Hashtable<String, List<ReviewCF>>();
		user = new Hashtable<String, List<ReviewCF>>();
	}
	
	public void init() throws IOException{
		BufferedReader bf = new BufferedReader(new FileReader(new File("/Users/Pisco/Downloads/yelp/new/review_cf.csv")));
		String str = bf.readLine();//Encabezado
		str = bf.readLine();
		while(str != null){
			String[] linea = str.split(";");
			String userId = linea[1];
			String businessId = linea[2];
			int stars = Integer.parseInt(linea[3]);
			double businessStars = Double.parseDouble(linea[4]);
			double userStars = Double.parseDouble(linea[5]);
			double computedStars = Double.parseDouble(linea[6]);
			ReviewCF review = new ReviewCF(businessId, userId, stars, businessStars, userStars, computedStars);
			reviewsModel.add(review);
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
		mean = mean/(double) reviewsModel.size();
	}

	/**
	 * Genera un recomendacion de tamaño size para un usuario dado
	 * @param userId
	 * @param size
	 * @return La lista con la lista de ids de los negocios
	 */
	public List<String> recommendItem(String userId, int size){
		List<String> result = new ArrayList<String>(size);
		
		double userMean = getuserMean(userId);
		List<String> businessReviewed = getBusinessReviewed(userId);
		
		//Recorrer los business y calcular la prediccion
		Iterator<String> it = business.keySet().iterator();
		while(it.hasNext()){
			String businessId = it.next();
			double businessMean = getBusinessMean(businessId);
			
			
		}
		
	
		return result;
	}
	
	public double getPrediction(double userMean, double businessMean){
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
	
	public double getuserMean(String userId){
		double mean = 0D;
		if(user.containsKey(userId)){
			mean = user.get(userId).get(0).getUserStars();
		}
		return mean;
	}
	
	public double getBusinessMean(String businessId){
		double mean = 0D;
		if(business.containsKey(businessId)){
			mean = business.get(businessId).get(0).getBusinessStars();
		}
		return mean;
	}
	
	public double getMean() {
		return mean;
	}

	public static void main(String[] args) throws IOException {
		
		ReviewCFReader r = new ReviewCFReader();
		long ini = System.currentTimeMillis();
		r.init();
		System.out.println("Time: "+((System.currentTimeMillis()-ini)/1000));
		System.out.println("Mean: "+r.getMean());
	}
}