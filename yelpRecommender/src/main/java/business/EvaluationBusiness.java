package business;

import java.util.ArrayList;
import java.util.List;

import recommender.CollaborativeRecommender.CollaborativeRecommender;
import recommender.CollaborativeRecommender.ItemRecommender;
import recommender.ContentBasedRecommender.ContentBasedRecommender;
import recommender.dayTimeRecommender.DayTimeRecommender;
import recommender.hybrid.HybridRecommender;
import recommender.neighborhoodRecommender.NeighborhoodRecommender;
import recommender.text.TextRecommender;
import entity.EvaluationStatistics;

public class EvaluationBusiness {

	private final CollaborativeRecommender cfRecommender;
	private final ItemRecommender itemRecommender;
	private final NeighborhoodRecommender nRecommender;
	private final DayTimeRecommender dayTimeRecommender;
	private final TextRecommender textRecommender;
	private final HybridRecommender hybridRecommender;
	private final ContentBasedRecommender contentRecommender;

	public EvaluationBusiness(CollaborativeRecommender cfRecommender, ItemRecommender itemRecommender,
			NeighborhoodRecommender nRecommender, DayTimeRecommender dayTimeRecommender, HybridRecommender hybridRecommender,
			TextRecommender textRecommender, ContentBasedRecommender contentRecommender) {
		this.cfRecommender = cfRecommender;
		this.itemRecommender = itemRecommender;
		this.nRecommender = nRecommender;
		this.dayTimeRecommender = dayTimeRecommender;
		this.textRecommender = textRecommender;
		this.hybridRecommender = hybridRecommender;
		this.contentRecommender = contentRecommender;
	}

	public List<EvaluationStatistics> getStatistics() {
		List<EvaluationStatistics> results = new ArrayList<EvaluationStatistics>();
		results.add(getCollaborativeRecommenderStatistics());
		results.add(getItemRecommenderStatistics());
		results.add(getNeighborhoodRecommenderStatistics());
		results.add(getDayTimeRecommenderStatistics());
		results.add(getTextRecommenderStatistics());
		results.add(getHybridStatistics());
		results.add(getContentRecommenderStatistics());
		return results;
	}

	private EvaluationStatistics getTextRecommenderStatistics() {
		double rmse = textRecommender.getRMSE();
		double mae = textRecommender.getMAE();
		double precision = textRecommender.getPrecision();
		double recall = textRecommender.getRecall();
		double trainingTime = textRecommender.getTrainingTime();
		double recommendationTime = textRecommender.getRecommendationTime();
		int size = itemRecommender.getDatasetSize();
		EvaluationStatistics evaluation = new EvaluationStatistics(rmse, mae, precision, recall, trainingTime,
				recommendationTime, size, Recommenders.TEXT_RECOMMENDER);
		return evaluation;
	}

	private EvaluationStatistics getHybridStatistics() {
		double rmse = hybridRecommender.getRMSE();
		double mae = hybridRecommender.getMAE();
		double precision = hybridRecommender.getPrecision();
		double recall = hybridRecommender.getRecall();
		double trainingTime = hybridRecommender.getTrainingTime();
		double recommendationTime = hybridRecommender.getRecommendationTime();
		int size = itemRecommender.getDatasetSize();
		EvaluationStatistics evaluation = new EvaluationStatistics(rmse, mae, precision, recall, trainingTime,
				recommendationTime, size, Recommenders.HYBRID_RECOMMENDER);
		return evaluation;
	}

	private EvaluationStatistics getDayTimeRecommenderStatistics() {
		double rmse = dayTimeRecommender.getRMSE();
		double mae = dayTimeRecommender.getMAE();
		double precision = dayTimeRecommender.getPrecision();
		double recall = dayTimeRecommender.getRecall();
		double trainingTime = dayTimeRecommender.getTrainingTime();
		double recommendationTime = dayTimeRecommender.getRecommendationTime();
		int size = itemRecommender.getDatasetSize();
		EvaluationStatistics evaluation = new EvaluationStatistics(rmse, mae, precision, recall, trainingTime,
				recommendationTime, size, Recommenders.DAYTIME_RECOMMENDER);
		return evaluation;
	}

	private EvaluationStatistics getNeighborhoodRecommenderStatistics() {
		double rmse = nRecommender.getRMSE();
		double mae = nRecommender.getMAE();
		double precision = nRecommender.getPrecision();
		double recall = nRecommender.getRecall();
		double trainingTime = nRecommender.getTrainingTime();
		double recommendationTime = nRecommender.getRecommendationTime();
		int size = itemRecommender.getDatasetSize();
		EvaluationStatistics evaluation = new EvaluationStatistics(rmse, mae, precision, recall, trainingTime,
				recommendationTime, size, Recommenders.NEIGHBORHOOD_RECOMMENDER);
		return evaluation;
	}

	private EvaluationStatistics getItemRecommenderStatistics() {
		double rmse = itemRecommender.getRMSE();
		double mae = itemRecommender.getMAE();
		double precision = itemRecommender.getPrecision();
		double recall = itemRecommender.getRecall();
		double trainingTime = itemRecommender.getTrainingTime();
		double recommendationTime = itemRecommender.getRecommendationTime();
		int size = itemRecommender.getDatasetSize();
		EvaluationStatistics evaluation = new EvaluationStatistics(rmse, mae, precision, recall, trainingTime,
				recommendationTime, size, Recommenders.ITEM_RECOMMENDER);
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
		EvaluationStatistics evaluation = new EvaluationStatistics(rmse, mae, precision, recall, trainingTime,
				recommendationTime, size, Recommenders.COLLABORATIVE_RECOMMENDER);
		return evaluation;
	}
	
	private EvaluationStatistics getContentRecommenderStatistics(){
		double rmse = 0;
		double mae = 0;
		double precision = contentRecommender.getPrecision();
		double recall = contentRecommender.getRecall();
		double trainingTime = 0;
		double recommendationTime = 0;
		int size = 0;
		EvaluationStatistics evaluation = new EvaluationStatistics(rmse, mae, precision, recall, trainingTime,
				recommendationTime, size, Recommenders.CONTENT_RECOMMENDER);
		return evaluation;
	}
}