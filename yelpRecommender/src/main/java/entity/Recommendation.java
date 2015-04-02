package entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Recommendation {

	private final String businessId;
	private final Business business;
	private final String userId;
	private final String userName;
	private final int stars;
	private final double computedStars;

	@JsonCreator
	public Recommendation(@JsonProperty("businessId") String businessId, @JsonProperty("business") Business business,
			@JsonProperty("userId") String userId, @JsonProperty("userName") String userName, @JsonProperty("stars") int stars,
			@JsonProperty("computedStars") double computedStars) {
		this.businessId = businessId;
		this.userId = userId;
		this.stars = stars;
		this.computedStars = computedStars;
		this.userName = userName;
		this.business = business;
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

	public Business getBusiness() {
		return business; 
	}

	public String getUserName() {
		return userName;
	}
}
