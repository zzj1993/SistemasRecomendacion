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

	public String getCfInitialSize() {
		return cfInitialSize;
	}

	public String getItemInitialSize() {
		return itemInitialSize;
	}
}
