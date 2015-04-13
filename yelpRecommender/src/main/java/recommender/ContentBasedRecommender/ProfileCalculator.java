package recommender.ContentBasedRecommender;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;


public class ProfileCalculator {
	
	private HashMap<String, double[]> business; 
	
	public void loadBusiness(String file){
		
		BufferedReader reed=null;	
		business = new HashMap<>();
		try {
			reed= new BufferedReader(new FileReader(file));			
			String line=null;
			
			reed.readLine();//header
			while((line=reed.readLine())!=null){
				String[] splitted=line.split(";");
//				System.out.println(line);
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
	
	public void calcule(){
		
		HashMap<String, UserProfile> prof = new HashMap<String, UserProfile>();
		BufferedReader reed=null;
		PrintWriter pr = null;
		try {
			pr= new PrintWriter(new File("data/user_profile.dat")); 
			reed= new BufferedReader(new FileReader("data/review_cf.csv"));			
			String line=null;			
			reed.readLine();//header
			line = reed.readLine();
			while(line != null){				
				String[] splitted=line.split(";");
				String businessId=splitted[2].replace("\"", "");
				String userId = splitted[1].replace("\"", "");
				int calif = Integer.parseInt(splitted[3]);
				double[] bprofile = business.get(businessId);				
				if(bprofile!=null&&Double.parseDouble(splitted[5])<calif){					
					for (int i = 0; i < 5; i++) {
						int cat = (int) bprofile[i];
						UserProfile profile =null;
						if(cat>0){
							String key = userId+";"+cat;							
							double[] tmp = Arrays.copyOfRange(bprofile, 5, bprofile.length);							
							if(prof.containsKey(key)){
								profile= prof.get(key);
								profile.addReview(businessId, tmp, calif);								
							}
							else
							{
								profile = new UserProfile(userId, cat);
								profile.addReview(businessId, tmp, calif);							
								prof.put(key, profile);								
							}
						}
						else{
							i=5;
						}
					}
				}				
				line = reed.readLine();			
			}
			Iterator<Entry<String, UserProfile>> iter = prof.entrySet().iterator();
			System.out.println(prof.size());
			while(iter.hasNext()){
				Entry<String, UserProfile> a = iter.next();
				UserProfile tm = a.getValue();				
				pr.println(tm.toString());				
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(reed!=null)
				try {reed.close();pr.close();} 
			catch (IOException e) {}
			
		}		
	
	}
	
	public static void main(String[] args) {
		ProfileCalculator main = new ProfileCalculator();
		main.loadBusiness("data/business_profile.csv");
		main.calcule();
		System.out.println("termino");		
	}

}
