package recommender.hybrid;

import java.util.Collections;
import java.util.List;

import recommender.dayTimeRecommender.DayTimeRecommender;
import recommender.neighborhoodRecommender.NeighborhoodRecommender;
import entity.Prediction;

public class HybridRecommender {
	
	private final NeighborhoodRecommender neighborhoodRecommender;
	private final DayTimeRecommender dayTimeRecommender;
	
	private long recommendationTime;
	private int recommendationCount;
	private double precision;
	private double recall;
	private long trainingTime;
	private double rmse;
	private double mae;
	private double randomUsers;
	private double rmseMaeSize;

	
	public HybridRecommender(NeighborhoodRecommender neighborhoodRecommender, DayTimeRecommender dayTimeRecommender){
		this.neighborhoodRecommender = neighborhoodRecommender;
		this.dayTimeRecommender = dayTimeRecommender;
	}

	public List<Prediction> recommendItems(String userId, String neighborhood, int size, int day, int time){
		List<Prediction> neighborhoodRecommendations = neighborhoodRecommender.recommendItems(userId, neighborhood, size);
		List<Prediction> dayTimeRecommendations = dayTimeRecommender.recommendItems(userId, size, day, time);
		dayTimeRecommendations.addAll(neighborhoodRecommendations);
		Collections.sort(dayTimeRecommendations);
		return dayTimeRecommendations;
	}

	public double getRMSE() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getMAE() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getPrecision() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getRecall() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getTrainingTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getRecommendationTime() {
		// TODO Auto-generated method stub
		return 0;
	}
}