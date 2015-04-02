package entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ShortReview {

	private final String userName;
	private final int stars;

	@JsonCreator
	public ShortReview(@JsonProperty("userName") String userName, @JsonProperty("stars") int stars) {
		this.userName = userName;
		this.stars = stars;
	}

	public String getUserName() {
		return userName;
	}

	public int getStars() {
		return stars;
	}
}