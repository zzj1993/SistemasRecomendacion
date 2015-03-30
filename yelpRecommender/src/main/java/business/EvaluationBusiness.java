package business;

import java.util.ArrayList;
import java.util.List;

import recommender.CollaborativeRecommender.CollaborativeRecommender;
import recommender.CollaborativeRecommender.ItemRecommender;
import entity.EvaluationStatistics;

public class EvaluationBusiness {
	
	private final CollaborativeRecommender cfRecommender;
	private final ItemRecommender itemRecommender;
	//private final NaiveBayes textRecommender;
	
	
	public EvaluationBusiness(CollaborativeRecommender cfRecommender, ItemRecommender itemRecommender) {
		this.cfRecommender = cfRecommender;
		this.itemRecommender = itemRecommender;
		//this.textRecommender = textRecommender;
	}
	
	public List<EvaluationStatistics> getStatistics(){
		List<EvaluationStatistics> results = new ArrayList<EvaluationStatistics>();
		results.add(getCollaborativeRecommenderStatistics());
		results.add(getItemRecommenderStatistics());
//		results.add(getTextRecommenderStatistics());
		return results;
	}

	private EvaluationStatistics getItemRecommenderStatistics() {
		double rmse = itemRecommender.getRMSE();
		double mae = itemRecommender.getMAE();
		double precision = itemRecommender.getPrecision();
		double recall = itemRecommender.getRecall();
		double trainingTime = itemRecommender.getTrainingTime();
		double recommendationTime = itemRecommender.getRecommendationTime();
		int size = itemRecommender.getDatasetSize();
		EvaluationStatistics evaluation = new EvaluationStatistics(rmse, mae, precision, recall, trainingTime, recommendationTime, size, Recommenders.ITEM_RECOMMENDER);
		return evaluation;
	}

	private EvaluationStatistics getCollaborativeRecommenderStatistics() {
		double rmse = cfRecommender.getRMSE();
		double mae = cfRecommender.getMAE();
		double precision = cfRecommender.getPrecision();
		double recall = cfRecommender.getRecall();
		double trainingTime = cfRecommender.getTrainingTime();
		double recommendationTime = cfRecommender.getRecommendationTime();
		int size = cfRecommender.getDatasetSize();
		EvaluationStatistics evaluation = new EvaluationStatistics(rmse, mae, precision, recall, trainingTime, recommendationTime, size, Recommenders.COLLABORATIVE_RECOMMENDER);
		return evaluation;
	}
	
	
	
//	private EvaluationStatistics getTextRecommenderStatistics(){
//		double rmse = textRecommender.getRMSE();
//		double mae = textRecommender.getMAE();
//		double precision = textRecommender.getPrecision();
//		double recall = textRecommender.getRecall();
//		double trainingTime = textRecommender.getTrainingTime();
//		double recommendationTime = textRecommender.getRecommendationTime();
//		int size = textRecommender.getDatasetSize();
//		EvaluationStatistics evaluation = new EvaluationStatistics(rmse, mae, precision, recall, trainingTime, recommendationTime, size, Recommenders.TEXT_RECOMMENDER);
//		return evaluation;
//	}
}