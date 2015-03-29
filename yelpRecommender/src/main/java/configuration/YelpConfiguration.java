package configuration;

import io.dropwizard.Configuration;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class YelpConfiguration extends Configuration{
	@NotNull
	@JsonProperty
	private DataConfiguration dataConfiguration;
	
	public DataConfiguration getDataConfiguration() {
		return dataConfiguration;
	}

}
