package entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RecommendationParameters {

	private final String userId;
	private final String neighborhood;
	private final int day;
	private final int time;
	private final String text;

	@JsonCreator
	public RecommendationParameters(@JsonProperty("userId") String userId,
			@JsonProperty("neighborhood") String neighborhood, @JsonProperty("day") int day,
			@JsonProperty("time") int time, @JsonProperty("text") String text) {
		this.userId = userId;
		this.neighborhood = neighborhood;
		this.day = day;
		this.time = time;
		this.text = text;
	}

	public String getUserId() {
		return userId;
	}

	public String getNeighborhood() {
		return neighborhood;
	}

	public int getDay() {
		return day;
	}

	public int getTime() {
		return time;
	}

	public String getText() {
		return text;
	}
}