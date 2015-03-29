package entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EvaluationStatistics {

	private final double rmse;
	private final double mae;
	private final double precision;
	private final double recall;
	private final double trainingTime;
	private final double recommendationTime;
	private final int size;
	private final String name;

	@JsonCreator
	public EvaluationStatistics(@JsonProperty("rmse") double rmse,
			@JsonProperty("mae") double mae,
			@JsonProperty("precision") double precision,
			@JsonProperty("recall") double recall,
			@JsonProperty("trainingTime") double trainingTime,
			@JsonProperty("recommendationTime") double recommendationTime,
			@JsonProperty("size") int size,
			@JsonProperty("name") String name) {
		this.rmse = rmse;
		this.mae = mae;
		this.precision = precision;
		this.recall = recall;
		this.trainingTime = trainingTime;
		this.recommendationTime = recommendationTime;
		this.size = size;
		this.name = name;
	}

	public double getRmse() {
		return rmse;
	}

	public double getMae() {
		return mae;
	}

	public double getPrecision() {
		return precision;
	}

	public double getRecall() {
		return recall;
	}

	public double getTrainingTime() {
		return trainingTime;
	}

	public double getRecommendationTime() {
		return recommendationTime;
	}
	
	public int getSize(){
		return size;
	}

	public String getName() {
		return name;
	}
}