package recommender.ContentBasedRecommender;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math.stat.correlation.PearsonsCorrelation;

import recommender.utils.RecommendersInformation;
import entity.Prediction;


public class ContentBasedRecommender {
	private HashMap<String, double[]> business;
	private HashMap<String, UserProfile> userProfiles;
	private double precision;
	private double recall;
	private RecommendersInformation recommendersInformation;
	private PearsonsCorrelation correlation;
	private String dir;
	
	public ContentBasedRecommender(RecommendersInformation recommendersInformation, String dir){
		this.recommendersInformation = recommendersInformation;
		this.dir = dir;
	}
	public ContentBasedRecommender(){
		correlation = new PearsonsCorrelation();
	}
	
	public void init(){
		correlation = new PearsonsCorrelation();
		loadBusiness(dir+"business_profile.csv");
		loadUserProfiles(dir+"user_profile.dat");
		evaluatePrecisionRecall();	
	}
public void loadBusiness(String file){
		
		BufferedReader reed=null;	
		business = new HashMap<>();
		try {
			reed= new BufferedReader(new FileReader(file));			
			String line=null;
			
			reed.readLine();//header
			while((line=reed.readLine())!=null){
				String[] splitted=line.split(";");

				String businessId=splitted[0];
				double vector[] = new double[56];
				for (int i = 0; i < vector.length; i++) {
					vector[i]=Double.parseDouble(splitted[1+i]);
				}				
				
				if(!business.containsKey(businessId)){
					business.put(businessId,vector);
				}			
			}		
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(reed!=null)
				try {reed.close();} 
			catch (IOException e) {}
			
		}
	
	}

	public void loadUserProfiles(String file){
		BufferedReader reed=null;	
		userProfiles = new HashMap<>();
		try {
			reed= new BufferedReader(new FileReader(file));			
			String line=null;
			
			
			while((line=reed.readLine())!=null){
				String[] splitted=line.split(";");				
				String profileId=splitted[0] +";"+splitted[1] ;
				UserProfile profile = new UserProfile(splitted);				
				if(!userProfiles.containsKey(profileId)){
					userProfiles.put(profileId,profile);
				}			
			}		
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(reed!=null)
				try {reed.close();} 
			catch (IOException e) {}
			
		}
	}
	public UserProfile getUserProfile2(String key){
		return userProfiles.get(key);
	}
	
	public double[] getUserProfile(String key){
		double[] resp = userProfiles.get(key).getProfile();		
		return resp;
	}
	
	public double[] getBusiness(String key){
		double[] resp = business.get(key);		
		return Arrays.copyOfRange(resp, 5, resp.length);
	}
	
	public List<Prediction> recommendByCategory(String key, int cat){
		List<Prediction> resp = new ArrayList<Prediction>();
		Iterator<String> iter = recommendersInformation.getAllBusinessKeys().iterator();		
		while(iter.hasNext()){
			String bkey = iter.next();
			double[] bp = business.get(bkey);
			double sim =0;
			int cont = 0;
			for (int i = 0; i <5; i++) {
				if(bp[i]==cat){
					UserProfile user =userProfiles.get(key+";"+(int)bp[i]);
					if(user!=null&&!user.businessReviewed(bkey)){
						double[] profile = user.getProfile();
						double[] bprofile = getBusiness(bkey);
						double corr = correlation.correlation(profile, bprofile);
						if(!Double.isNaN(corr)&&corr<1){
							sim += corr*user.getAverage();
							cont++;											
						}
					}					
				}
				else if(bp[i]==0){
					i=5;
				}
			}
			if(cont>0){				
			Prediction tmp = new Prediction(bkey, (sim/cont));
			resp.add(tmp);
			}
		}		
		Collections.sort(resp);
		try{resp = resp.subList(0, 10);}catch(Exception e){}
		return resp;
	}
	
	public List<Prediction> recommendFromList(String key, List<String> a){
		List<Prediction> resp = new ArrayList<Prediction>();
		Iterator<String> iter = a.iterator();		
		while(iter.hasNext()){
			String bkey = iter.next();
			double[] bp = business.get(bkey);
			double sim =0;
			int cont = 0;
			for (int i = 0; i <5; i++) {
				if(bp[i]>0){
					UserProfile user =userProfiles.get(key+";"+(int)bp[i]);
					if(user!=null&&!recommendersInformation.getBusinessReviewed(key).contains(bkey)){
						double[] profile = user.getProfile();
						double[] bprofile = getBusiness(bkey);
						double corr = correlation.correlation(profile, bprofile);
						if(!Double.isNaN(corr)&&corr<1){
							sim += corr*user.getAverage();
							cont++;											
						}
					}					
				}
				else{
					i=5;
				}
			}
			if(cont>0){				
			Prediction tmp = new Prediction(bkey, (sim/cont));
			resp.add(tmp);
			}
		}
		Collections.sort(resp);
		try{resp = resp.subList(0, 10);}catch(Exception e){}
		return resp;
		
	}	

	public List<Prediction> recommend(String key,int size){
		List<Prediction> resp = new ArrayList<Prediction>();
//		Iterator<String> iter = recommendersInformation.getAllBusinessKeys().iterator();
		Iterator<String> iter = business.keySet().iterator();
		
		while(iter.hasNext()){
			String bkey = iter.next();
			double[] bp = business.get(bkey);
			double sim =0;
			int cont = 0;
			for (int i = 0; i <5; i++) {
				if(bp[i]>0){
					UserProfile user =userProfiles.get(key+";"+(int)bp[i]);
					if(user!=null&&!recommendersInformation.getBusinessReviewed(key).contains(bkey)){
						double[] profile = user.getProfile();
						double[] bprofile = getBusiness(bkey);
						double corr = correlation.correlation(profile, bprofile);
						if(!Double.isNaN(corr)){
							sim += corr;
							cont++;											
						}
					}					
				}
				else{
					i=5;
				}
			}
			if(cont>0){				
			Prediction tmp = new Prediction(bkey, (sim/cont));
			resp.add(tmp);
			}
		}
		Collections.sort(resp);		
		try{resp = resp.subList(0, size);}catch(Exception e){}
		return resp;
	}
	public List<Prediction> recommend(String key){
		List<Prediction> resp = new ArrayList<Prediction>();
//		Iterator<String> iter = recommendersInformation.getAllBusinessKeys().iterator();
		Iterator<String> iter = business.keySet().iterator();
		
		while(iter.hasNext()){
			String bkey = iter.next();
			double[] bp = business.get(bkey);
			double sim =0;
			int cont = 0;
			for (int i = 0; i <5; i++) {
				if(bp[i]>0){
					UserProfile user =userProfiles.get(key+";"+(int)bp[i]);
					if(user!=null&&!recommendersInformation.getBusinessReviewed(key).contains(bkey)){
						double[] profile = user.getProfile();
						double[] bprofile = getBusiness(bkey);
						double corr = correlation.correlation(profile, bprofile);
						if(!Double.isNaN(corr)){
							sim += corr;
							cont++;											
						}
					}					
				}
				else{
					i=5;
				}
			}
			if(cont>0){				
			Prediction tmp = new Prediction(bkey, (sim/cont));
			resp.add(tmp);
			}
		}
		Collections.sort(resp);		
		return resp;
	}
	private void evaluatePrecisionRecall(){
		List<String> randomUsers = recommendersInformation.getRandomUsers();
		int goodBusiness = recommendersInformation.getAllGoodBusinessSize();		
		precision = 0.0;
		recall = 0.0;
		int i = 1;
		for (String u : randomUsers) {
			List<Prediction> items = recommend(u,10);			
			long goodRecommendations = items.parallelStream().filter(p -> Double.compare(p.getValue(), 0.8) >= 0).count();
			if (items != null && !items.isEmpty()) {
				precision += (double) goodRecommendations / (double) items.size();
			}
			recall += (double) goodRecommendations / (double) goodBusiness;
			System.out.println("Content Recommender: Precision Recall: " + i + " de " + randomUsers.size());			
			i++;
		}
		precision = precision / (double) randomUsers.size();
		recall = recall / (double) randomUsers.size();
	}
	
	public double getPrecision() {
		return precision;
	}
	public double getRecall() {
		return recall;
	}
	
//	public static void main(String[] args) {
//		
//		ContentBasedRecommender main = new ContentBasedRecommender();
//		main.loadBusiness("data/business_profile.csv");
//		main.loadUserProfiles("data/user_profile.dat");
//		PearsonsCorrelation a = new PearsonsCorrelation();		
//		List<Prediction> list = main.recommend("fI_87gH3jSuxdI_Wq-Rmwg", 10);		
//		for (Prediction prediction : list) {
//			System.out.println(prediction.getKey()+":"+prediction.getValue()+Arrays.toString(main.getBusiness(prediction.getKey())));
//		}
//	}
}
