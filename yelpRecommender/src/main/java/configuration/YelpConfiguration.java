package configuration;

import io.dropwizard.Configuration;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class YelpConfiguration extends Configuration{
	@NotNull
	@JsonProperty
	private DataConfiguration dataConfiguration;
	
	@NotNull
	@JsonProperty
	private RecommendersConfiguration recommendersConfiguration;
	
	@NotNull
	@JsonProperty
	private H2Config h2Configuration;
	
	public DataConfiguration getDataConfiguration() {
		return dataConfiguration;
	}

	public RecommendersConfiguration getRecommendersConfiguration() {
		return recommendersConfiguration;
	}

	public H2Config getH2Configuration() {
		return h2Configuration;
	}
}
