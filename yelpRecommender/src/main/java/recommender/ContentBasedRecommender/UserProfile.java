package recommender.ContentBasedRecommender;

import java.util.ArrayList;
import java.util.List;

import converter.jsonConverter.Constants;



public class UserProfile {
	private String userId;
	private int category;
	private double average;
	private int numReviews;
	private double[] profile;
	private List<String> reviews;
	
	public UserProfile(String userId, int category){
		
		this.userId = userId;
		this.category = category;
		average = 0;
		numReviews = 0;
		profile = new double[51];
		reviews = new ArrayList<String>();
	}
	
	public UserProfile(String[] line){		
		this.userId = line[0];
		this.category = Integer.parseInt(line[1]);
		average = Double.parseDouble(line[2]);
		numReviews = Integer.parseInt(line[3]);
		profile = new double[51];
		reviews = new ArrayList<String>();
		for (int i = 0; i < numReviews; i++) {
			reviews.add(line[i+4]);
		}
		for (int i = 0; i < 51; i++) {			
			int index = 4+numReviews;			
			profile[i]=Double.parseDouble(line[i+index]);
			
		}
		
		
	}

	public String getUserId() {
		return userId;
	}

	public int getCategory() {
		return category;
	}

	public double getAverage() {
		return average;
	}

	public int getNumReviews() {
		return numReviews;
	}

	public double[] getProfile() {
		return profile;
	}

	public List<String> getReviews() {
		return reviews;
	}
	
	public void addReview(String businessId, double[] businessProfile, int calification){		
		reviews.add(businessId);
		for (int i = 0; i < profile.length; i++) {
			profile[i] = (profile[i]*numReviews+businessProfile[i]) /(numReviews+1);			
		}
		average = (average*numReviews + calification) / (numReviews+1);		
		numReviews++;		
	}
	
	public boolean businessReviewed(String key){
		return reviews.contains(key);
	}
	
	public String toString(){
		StringBuilder resp = new StringBuilder(userId);
		resp.append(Constants.SEPARADOR+category);
		resp.append(Constants.SEPARADOR+average);
		resp.append(Constants.SEPARADOR+numReviews);
		for (int i = 0; i < reviews.size(); i++) {
			resp.append(Constants.SEPARADOR+reviews.get(i));
		}
		for (int i = 0; i < profile.length; i++) {
			resp.append(Constants.SEPARADOR+profile[i]);
		}
		return resp.toString();
	}
	
	
}
