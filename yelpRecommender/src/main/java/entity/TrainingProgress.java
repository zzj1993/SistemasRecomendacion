package entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TrainingProgress {
	
	private int trainingProgress;

	@JsonCreator
	public TrainingProgress(@JsonProperty("trainingProgress") int trainingProgress) {
		this.trainingProgress = trainingProgress;
	}

	public int getTrainingProgress() {
		return trainingProgress;
	}
}