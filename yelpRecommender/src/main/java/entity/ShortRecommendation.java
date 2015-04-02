package entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ShortRecommendation {

	private final String userId;
	private final String businessId;
	private final String businessName;;
	private final int stars;
	private final int itemStars;
	private final int collaborativeStars;

	@JsonCreator
	public ShortRecommendation(@JsonProperty("userId") String userId, @JsonProperty("businessId") String businessId,
			@JsonProperty("businessName") String businessName, @JsonProperty("stars") int stars,
			@JsonProperty("itemStars") int itemStars, @JsonProperty("collaborativeStars") int collaborativeStars) {
		this.userId = userId;
		this.businessId = businessId;
		this.businessName = businessName;
		this.stars = stars;
		this.itemStars = itemStars;
		this.collaborativeStars = collaborativeStars;
	}

	public String getBusinessId() {
		return businessId;
	}

	public String getBusinessName() {
		return businessName;
	}

	public int getStars() {
		return stars;
	}

	public int getItemStars() {
		return itemStars;
	}

	public int getCollaborativeStars() {
		return collaborativeStars;
	}

	public String getUserId() {
		return userId;
	}
}