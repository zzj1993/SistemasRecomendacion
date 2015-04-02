package entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Review {

	private final String businessId;
	private final String userId;
	private final int stars;
	private final double computedStars;
	private final String text;

	@JsonCreator
	public Review(@JsonProperty("businessId") String businessId, @JsonProperty("userId") String userId,
			@JsonProperty("stars") int stars, @JsonProperty("computedStars") double computedStars,
			@JsonProperty("text") String text) {
		this.businessId = businessId;
		this.userId = userId;
		this.stars = stars;
		this.computedStars = computedStars;
		this.text = text;
	}

	public String getBusinessId() {
		return businessId;
	}

	public String getUserId() {
		return userId;
	}

	public int getStars() {
		return stars;
	}

	public double getComputedStars() {
		return computedStars;
	}

	public String getText() {
		return text;
	}
	
	public ReviewCF getReviewCF(){
		return new ReviewCF(businessId, userId, stars, computedStars);
	}
}
