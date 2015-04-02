package recommender.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;

import recommender.dayTimeRecommender.DayTime;
import entity.Business;
import entity.MeanCF;
import entity.ReviewCF;

public class RecommendersInformation {

	private Hashtable<String, List<ReviewCF>> businessReviews;
	private Hashtable<String, List<ReviewCF>> userReviews;
	private Hashtable<String, MeanCF> businessMeans;
	private Hashtable<String, MeanCF> userMeans;
	private List<ReviewCF> reviews;
	
	private String dir;
	private DataModel testDataModel;
	
	private Hashtable<String, Integer> userIdGeneratedId;
	private Hashtable<String, Integer> businessIdGeneratedId;
	private Hashtable<Integer, String> generatedIdUserId;
	private Hashtable<Integer, String> generatedIdBusinessId;
	private Hashtable<String, List<String>> neighborhoodsBusiness;
	private Hashtable<String, List<DayTime>> businessDayTime;
	private Hashtable<String, String> userNames;
	private Hashtable<String, Business> business;

	public RecommendersInformation(String dir) {
		this.dir = dir;
		businessReviews = new Hashtable<String, List<ReviewCF>>();
		userReviews = new Hashtable<String, List<ReviewCF>>();
		businessMeans = new Hashtable<String, MeanCF>();
		userMeans = new Hashtable<String, MeanCF>();
		reviews = new ArrayList<ReviewCF>();
		
		userIdGeneratedId = new Hashtable<String, Integer>();
		businessIdGeneratedId = new Hashtable<String, Integer>();
		generatedIdUserId = new Hashtable<Integer, String>();
		generatedIdBusinessId = new Hashtable<Integer, String>();
		neighborhoodsBusiness = new Hashtable<String, List<String>>();
		businessDayTime = new Hashtable<String, List<DayTime>>();
		userNames = new Hashtable<String, String>();
		business = new Hashtable<String, Business>(); 
	}
	
	private String getFile(int size) {
		// /dir/10_itemRecommender.csv
		return dir + size + "_itemRecommender.csv";
	}

	public void init(String file) {
		System.out.println("Loading Data...");
		loadInformation(file);
		System.out.println("Data Loaded");
	}

	public void addReview(ReviewCF review) {
		reviews.add(review);
	}

	public void createUserMean(String userId, double userStars) {
		if (!userMeans.containsKey(userId)) {
			MeanCF mean = new MeanCF(userStars, 1);
			userMeans.put(userId, mean);
		}
	}

	public void addUserMean(String userId, double userStars) {
		if (userMeans.containsKey(userId)) {
			MeanCF oldMean = userMeans.get(userId);
			MeanCF newMean = new MeanCF(oldMean.getSuma() + userStars, oldMean.getCount() + 1);
			userMeans.put(userId, newMean);
		} else {
			createUserMean(userId, userStars);
		}
	}

	public void createBusinessMean(String businessId, double businessStars) {
		if (!businessMeans.containsKey(businessId)) {
			MeanCF mean = new MeanCF(businessStars, 1);
			businessMeans.put(businessId, mean);
		}
	}

	public void addBusinessMean(String businessId, double businessStars) {
		if (businessMeans.containsKey(businessId)) {
			MeanCF oldMean = businessMeans.get(businessId);
			MeanCF newMean = new MeanCF(oldMean.getSuma() + businessStars, oldMean.getCount() + 1);
			businessMeans.put(businessId, newMean);
		} else {
			createBusinessMean(businessId, businessStars);
		}
	}

	public void addUserReview(ReviewCF review) {
		List<ReviewCF> lista;
		if (userReviews.containsKey(review.getUserId())) {
			lista = userReviews.get(review.getUserId());
		} else {
			lista = new ArrayList<ReviewCF>();
		}
		lista.add(review);
		userReviews.put(review.getUserId(), lista);
	}

	public void addBusinessReview(ReviewCF review) {
		List<ReviewCF> lista;
		if (businessReviews.containsKey(review.getBusinessId())) {
			lista = businessReviews.get(review.getBusinessId());
		} else {
			lista = new ArrayList<ReviewCF>();
		}
		lista.add(review);
		businessReviews.put(review.getBusinessId(), lista);
	}

	public int getReviewsSize() {
		return reviews.size();
	}

	public double getUserMean(String userId) {
		return userMeans.get(userId).getMean();
	}

	public double getBusinessMean(String businessId) {
		return businessMeans.get(businessId).getMean();
	}

	public Set<String> getAllBusinessKeys() {
		return businessReviews.keySet();
	}

	public boolean userReviewsContainsUser(String userId) {
		return userReviews.containsKey(userId);
	}

	public List<ReviewCF> getUserReviews(String userId) {
		return userReviews.get(userId);
	}

	public List<ReviewCF> getReviews() {
		return reviews;
	}

	public List<String> getRandomUsers(double percentage) {
		List<String> users = new ArrayList<String>(userMeans.keySet());
		Random r = new Random();
		Collections.shuffle(users);
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < (percentage* users.size()) / 100; i++) {
			String k = users.get(r.nextInt(users.size()));
			result.add(k);
		}
		return result;
	}

	public int getAllGoodBusinessSize() {
		Iterator<String> it = businessMeans.keySet().iterator();
		int goodBusinessSize = 0;
		while (it.hasNext()) {
			String key = it.next();
			if (Double.compare(businessMeans.get(key).getMean(), 4.0D) >= 0) {
				goodBusinessSize++;
			}
		}
		return goodBusinessSize;
	}
	
	public List<ReviewCF> getReviewsSublist(int size){
		return reviews.subList(0, size);
	}

	private void loadInformation(String file) {
		loadUserBusinessMeans(file);
		loadTestDataModel();
	}

	private void loadTestDataModel() {
		try {
			testDataModel = new FileDataModel(new File(getFile(100)), ";");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadUserBusinessMeans(String file) {
		try {
			BufferedReader bf = new BufferedReader(new FileReader(new File(file)));
			String str = bf.readLine();// Encabezado
			str = bf.readLine();
			while (str != null) {
				String[] linea = str.trim().split(";");
				String userId = linea[1].replace("\"", "").trim();
				String businessId = linea[2].replace("\"", "").trim();
				int stars = Integer.parseInt(linea[3].trim());
				double businessStars = Double.parseDouble(linea[4].trim());
				double userStars = Double.parseDouble(linea[5].trim());
				double computedStars = Double.parseDouble(linea[6].trim());
				ReviewCF review = new ReviewCF(businessId, userId, stars, computedStars);
				addReview(review);
				addBusinessMean(businessId, businessStars);
				addUserMean(userId, userStars);
				addBusinessReview(review);
				addUserReview(review);
				createUserMean(userId, userStars);
				str = bf.readLine();
			}
			bf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public DataModel getTestDataModel(){
		return testDataModel;
	}
	
	public DataModel getDataModel(int size) throws IOException{
		return new FileDataModel(new File(getFile(size)), ";");
	}
	
	public void addUser(String userId, int userGeneratedId){
		userIdGeneratedId.put(userId, userGeneratedId);
		generatedIdUserId.put(userGeneratedId, userId);
	}
	
	public void addBusiness(String businessId, int businessGeneratedId){
		businessIdGeneratedId.put(businessId, businessGeneratedId);
		generatedIdBusinessId.put(businessGeneratedId, businessId);
	}
	
	public int getUserGeneratedId(String userId){
		return userIdGeneratedId.get(userId);
	}
	
	public String getUserId(int generatedUserId){
		return generatedIdUserId.get(generatedUserId);
	}
	
	public int getBusinessGeneratedId(String businessId){
		return businessIdGeneratedId.get(businessId);
	}
	
	public String getBusinessId(int generatedBusinessId){
		return generatedIdBusinessId.get(generatedBusinessId);
	}
	
	public void addNeighborhoodBusiness(String neighborhood, String business){
		List<String> businesses;
		if(neighborhoodsBusiness.contains(neighborhood)){
			businesses = neighborhoodsBusiness.get(neighborhood);
		}else{
			businesses = new ArrayList<String>();
		}
		businesses.add(business);
		neighborhoodsBusiness.put(neighborhood, businesses);
	}
	
	public List<String> getBusinessesInNeighborhood(String neighborhood){
		return neighborhoodsBusiness.get(neighborhood);
	}
	
	public void addBusinessDayTime(String businessId, List<DayTime> dayTime){
		businessDayTime.put(businessId, dayTime);
	}
	
	public List<DayTime> getBusinessDayTime(String businessId){
		return businessDayTime.get(businessId);
	}

	public int getAllGoodBusinessSizeInNeighborhood(String neighborhood) {
		int goodBusinessSize = 0;
		List<String> businesses = neighborhoodsBusiness.get(neighborhood);
		if(businesses != null){
			for(int i = 0 ; i < businesses.size() ; i++){
				if (Double.compare(businessMeans.get(businesses.get(i)).getMean(), 4.0D) >= 0) {
					goodBusinessSize++;
				}
			}
		}
		return goodBusinessSize;
	}
	
	public List<String> getNeighborhoods(int percentage) {
		List<String> neighborhoods = new ArrayList<String>(neighborhoodsBusiness.keySet());
		Random r = new Random();
		Collections.shuffle(neighborhoods);
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < (percentage * neighborhoods.size()) / 100; i++) {
			String k = neighborhoods.get(r.nextInt(neighborhoods.size()));
			result.add(k);
		}
		return result;
	}
	
	public List<String> getNeighborhoods() {
		List<String> neighborhoods = new ArrayList<String>(neighborhoodsBusiness.keySet());
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < neighborhoods.size(); i++) {
			String k = neighborhoods.get(i);
			result.add(k);
		}
		Collections.sort(result);
		return result;
	}
	
	public void addUserName(String userID, String name){
		userNames.put(userID, name);
	}
	
	public String getUserName(String userId){
		return userNames.get(userId);
	}
	
	public void addBusiness(Business b){
		business.put(b.getBusinessId(), b);
	}
	
	public Business getBusinessInformation(String businessId){
		return business.get(businessId);
	}
	
	public List<String> getBusinessNeighborhoods(String businessId){
		List<String> result = new ArrayList<String>();
		Iterator<String> it = neighborhoodsBusiness.keySet().iterator();
		while(it.hasNext()){
			String n = it.next();
			List<String> bs = neighborhoodsBusiness.get(n);
			if(bs.contains(businessId)){
				result.add(n);
			}
		}
		return result;
	}
}