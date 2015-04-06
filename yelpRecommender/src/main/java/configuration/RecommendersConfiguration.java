package configuration;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RecommendersConfiguration {
	@NotNull
	@NotEmpty
	@JsonProperty
	private String cfInitialSize;

	@NotNull
	@NotEmpty
	@JsonProperty
	private String itemInitialSize;
	
	@NotNull
	@NotEmpty
	@JsonProperty
	private String randomUsers;
	
	@NotNull
	@NotEmpty
	@JsonProperty
	private String neighborhoodSize;
	
	@NotNull
	@NotEmpty
	@JsonProperty
	private String rmseMaeSize;
	
	@NotNull
	@NotEmpty
	@JsonProperty
	private String userNeighborhoodSize;

	public String getCfInitialSize() {
		return cfInitialSize;
	}

	public String getItemInitialSize() {
		return itemInitialSize;
	}

	public String getRandomUsers() {
		return randomUsers;
	}

	public String getNeighborhoodSize() {
		return neighborhoodSize;
	}

	public String getRmseMaeSize() {
		return rmseMaeSize;
	}

	public String getUserNeighborhoodSize() {
		return userNeighborhoodSize;
	}
}
