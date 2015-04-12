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
	
	public ContentBasedRecommender(RecommendersInformation recommendersInformation){
		this.recommendersInformation = recommendersInformation;
	}
	public ContentBasedRecommender(){
		correlation = new PearsonsCorrelation();
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
//		Iterator<String> iter = recommendersInformation.getAllBusinessKeys().iterator();
		Iterator<String> iter = business.keySet().iterator();
		
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
//		Iterator<String> iter = recommendersInformation.getAllBusinessKeys().iterator();
		Iterator<String> iter = a.iterator();		
		while(iter.hasNext()){
			String bkey = iter.next();
			double[] bp = business.get(bkey);
			double sim =0;
			int cont = 0;
			for (int i = 0; i <5; i++) {
				if(bp[i]>0){
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
	private void evaluatePrecisionRecall(){
		List<String> randomUsers = recommendersInformation.getRandomUsers();
		int goodBusiness = recommendersInformation.getAllGoodBusinessSize();
		precision = 0.0;
		recall = 0.0;
		int i = 1;
		for (String u : randomUsers) {
			List<Prediction> items = recommend(u, 10);
			long goodRecommendations = items.parallelStream().filter(p -> Double.compare(p.getValue(), 4.0D) >= 0).count();
			if (items != null && !items.isEmpty()) {
				precision += (double) goodRecommendations / (double) items.size();
			}
			recall += (double) goodRecommendations / (double) goodBusiness;
			System.out.println("Item Recommender: Precision Recall: " + i + " de " + randomUsers.size());			
			i++;
		}

		precision = precision / (double) randomUsers.size();
		recall = recall / (double) randomUsers.size();
	}
	
	public static void main(String[] args) {
		
		ContentBasedRecommender main = new ContentBasedRecommender();
		main.loadBusiness("data/business_profile.csv");
		main.loadUserProfiles("data/user_profile.dat");
		PearsonsCorrelation a = new PearsonsCorrelation();
		
		//System.out.println(main.getUserProfile("Pwg_JKuB05o_77bnGPdNhg;11").length);
		//System.out.println(main.getBusiness("36a2h_kADYEoBOr3rtaj1Q").length);
		
		double x1 =a.correlation(main.getUserProfile("k4LqH2CRof1XvgEGI-JBiQ;2"),main.getBusiness("D4ckco-duEqCzyYdyuaxjg"));
		System.out.println(Arrays.toString(main.getUserProfile("k4LqH2CRof1XvgEGI-JBiQ;2")));
		System.out.println(Arrays.toString(main.getBusiness("D4ckco-duEqCzyYdyuaxjg")));
		double x2 =a.correlation(main.getUserProfile("k4LqH2CRof1XvgEGI-JBiQ;17"),main.getBusiness("D4ckco-duEqCzyYdyuaxjg"));
		System.out.println(Arrays.toString(main.getUserProfile("k4LqH2CRof1XvgEGI-JBiQ;17")));
		System.out.println(Arrays.toString(main.getBusiness("D4ckco-duEqCzyYdyuaxjg")));
		double y1 = 2.75 + (main.getUserProfile2("k4LqH2CRof1XvgEGI-JBiQ;2").getAverage()-2.75)*x1;
		double y2 = 2.75 + (main.getUserProfile2("k4LqH2CRof1XvgEGI-JBiQ;17").getAverage()-2.75)*x2;
		System.out.println(main.getUserProfile2("k4LqH2CRof1XvgEGI-JBiQ;2").getAverage());
		System.out.println(y1 + " " + y2 + " "+ ((y1+y2)/2));
		
		List<Prediction> list = main.recommend("iSJLdur495--oPDE0b1Vpw", 10);
		for (Prediction prediction : list) {
			System.out.println(prediction.getKey()+":"+prediction.getValue());
		}
	}

}
