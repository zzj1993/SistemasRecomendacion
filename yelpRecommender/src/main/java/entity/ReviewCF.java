package entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReviewCF {

	private final String businessId;
	private final String userId;
	private final int stars;
	private final int computedStars;
	private final int itemStars;

	@JsonCreator
	public ReviewCF(@JsonProperty("businessId") String businessId, @JsonProperty("userId") String userId,
			@JsonProperty("stars") int stars, @JsonProperty("computedStars") int computedStars,
			@JsonProperty("itemStars") int itemStars) {
		this.businessId = businessId;
		this.userId = userId;
		this.stars = stars;
		this.computedStars = computedStars;
		this.itemStars = itemStars;
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

	public int getComputedStars() {
		return computedStars;
	}

	public int getItemStars() {
		return itemStars;
	}
}