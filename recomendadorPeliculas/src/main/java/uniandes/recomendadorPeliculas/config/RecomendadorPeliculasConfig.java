package uniandes.recomendadorPeliculas.config;

import io.dropwizard.Configuration;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RecomendadorPeliculasConfig extends Configuration {
	
	@NotNull
	@JsonProperty
	private H2Config h2Config;
	
	@NotNull
	@JsonProperty
	private DataConfig dataConfig;

	public H2Config getH2Config() {
		return h2Config;
	}

	public DataConfig getDataConfig() {
		return dataConfig;
	}
}