package business;

import java.util.ArrayList;
import java.util.List;

import recommender.CollaborativeRecommender.CollaborativeRecommender;
import entity.EvaluationStatistics;

public class EvaluationBusiness {
	
	private final CollaborativeRecommender recommender;
	
	public EvaluationBusiness(CollaborativeRecommender recommender) {
		this.recommender = recommender;
	}
	
	public List<EvaluationStatistics> getStatistics(){
		List<EvaluationStatistics> results = new ArrayList<EvaluationStatistics>();
		results.add(getCollaborativeRecommenderStatistics());
		return results;
	}

	private EvaluationStatistics getCollaborativeRecommenderStatistics() {
		double rmse = recommender.getRMSE();
		double mae = recommender.getMAE();
		double precision = recommender.getPrecision();
		double recall = recommender.getRecall();
		double trainingTime = recommender.getTrainingTime();
		double recommendationTime = recommender.getRecommendationTime();
		int size = recommender.getDatasetSize();
		EvaluationStatistics evaluation = new EvaluationStatistics(rmse, mae, precision, recall, trainingTime, recommendationTime, size, "Collaborative Recommender");
		return evaluation;
	}
}