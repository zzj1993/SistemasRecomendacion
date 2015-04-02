package entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Business {

	private final String businessId;
	private final String name;
	private final String full_address;
	private final String city;
	private final String state;
	private final int review_count;
	private final List<String> neighborhoods;

	@JsonCreator
	public Business(@JsonProperty("businessId") String businessId, @JsonProperty("name") String name,
			@JsonProperty("full_address") String full_address, @JsonProperty("city") String city,
			@JsonProperty("state") String state, @JsonProperty("review_count") int review_count,
			@JsonProperty("neighborhoods") List<String> neighborhoods) {
		this.businessId = businessId;
		this.name = name;
		this.full_address = full_address;
		this.city = city;
		this.state = state;
		this.review_count = review_count;
		this.neighborhoods = neighborhoods;
	}

	public String getBusinessId() {
		return businessId;
	}

	public String getName() {
		return name;
	}

	public String getFull_address() {
		return full_address;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public int getReview_count() {
		return review_count;
	}

	public List<String> getNeighborhoods() {
		return neighborhoods;
	}
}