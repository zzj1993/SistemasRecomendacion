package business;

import java.util.ArrayList;
import java.util.List;

import recommender.CollaborativeRecommender.CollaborativeRecommender;
import sentimentAnalysis.NaiveBayes;
import entity.EvaluationStatistics;

public class EvaluationBusiness {
	
	private final CollaborativeRecommender cfRecommender;
	//private final NaiveBayes textRecommender;
	
	
	public EvaluationBusiness(CollaborativeRecommender cfRecommender) {
		this.cfRecommender = cfRecommender;
		//this.textRecommender = textRecommender;
	}
	
	public List<EvaluationStatistics> getStatistics(){
		List<EvaluationStatistics> results = new ArrayList<EvaluationStatistics>();
		results.add(getCollaborativeRecommenderStatistics());
//		results.add(getTextRecommenderStatistics());
		return results;
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