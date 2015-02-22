package uniandes.recomendadorPeliculas.business;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class Recommenders {
	
	public static final int JACCARD=1;
	
	public static final int COS_SIM = 2;
	
	public static final int PEARSON = 3;
	
	private DataModel model;
	
	public Recommenders(String arch){
		try {
			model = new FileDataModel(new File(arch),"::");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}	
	
	
	public List<RecommendedItem> getUserBasedRecomemndations(int userid, int modeltype, int neighborhoodSize, int nRecommendations) throws TasteException{
		
		UserSimilarity similarity = getUsersSimilarity(modeltype);
		
		UserNeighborhood neighborhood = new NearestNUserNeighborhood(neighborhoodSize,similarity, model);
		
		UserBasedRecommender recommender = new GenericUserBasedRecommender(	model, neighborhood, similarity);
		List<RecommendedItem> recommendations = recommender.recommend(userid, nRecommendations);		
		return recommendations;
	}
	
	public List<RecommendedItem> getItemBasedRecommendations(int userid, int modeltype, int nRecommendations) throws TasteException{
		
		ItemSimilarity similarity = getItemsSimilarity(modeltype);		
		ItemBasedRecommender recommender = new GenericItemBasedRecommender(	model,similarity);
		List<RecommendedItem> recommendations = recommender.recommend(userid, nRecommendations);		
		return recommendations;
	}
	
	private UserSimilarity getUsersSimilarity(int modeltype) throws TasteException{
		UserSimilarity similarity = null;
		
		if(modeltype==JACCARD){
			similarity = new TanimotoCoefficientSimilarity(model);
		}
		else if(modeltype==COS_SIM){		
				similarity = new UncenteredCosineSimilarity(model);
				}
		
		else if(modeltype==PEARSON){
			similarity = new PearsonCorrelationSimilarity(model);
		}
		
		return similarity;
	}
	
	private ItemSimilarity getItemsSimilarity(int modeltype) throws TasteException{
		ItemSimilarity similarity = null;
		
		if(modeltype==JACCARD){
			similarity = new TanimotoCoefficientSimilarity(model);
		}
		else if(modeltype==COS_SIM){		
				similarity = new UncenteredCosineSimilarity(model);
				}
		
		else if(modeltype==PEARSON){
			similarity = new PearsonCorrelationSimilarity(model);
		}
		
		return similarity;
	}
	
	public static final List<Long> Recommended2Id(List<RecommendedItem> list){
		List<Long> resp = new ArrayList<Long>();
		for (RecommendedItem recommendation : list) {
			resp.add((recommendation.getItemID()));
		}
		return resp;
	}
	
	public static void main(String[] args) throws IOException {
		Recommenders r = new Recommenders("data/ratings.dat");
		try {
			r.getUserBasedRecomemndations(2, 1, 100, 10);
			r.getItemBasedRecommendations(2, 1, 10);
			r.getUserBasedRecomemndations(2, 2, 100, 10);
			r.getItemBasedRecommendations(2, 2, 10);
			r.getUserBasedRecomemndations(2, 3, 100, 10);
			r.getItemBasedRecommendations(2, 3, 10);
			r.getUserBasedRecomemndations(2, 1, 10, 10);			
			r.getUserBasedRecomemndations(2, 2, 10, 10);			
			r.getUserBasedRecomemndations(2, 3, 10, 10);
			
		} catch (TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	

}
