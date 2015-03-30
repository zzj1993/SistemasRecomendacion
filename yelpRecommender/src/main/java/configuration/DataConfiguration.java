package configuration;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataConfiguration {
	@NotNull
	@NotEmpty
	@JsonProperty
	private String dir;

	@NotNull
	@NotEmpty
	@JsonProperty
	private String collaborativeFile;
	
	@NotNull
	@NotEmpty
	@JsonProperty
	private String fileGeneratorInDir;
	
	@NotNull
	@NotEmpty
	@JsonProperty
	private String fileGeneratorOutDir;

	public String getDir() {
		return dir;
	}

	public String getCollaborativeFile() {
		return dir+collaborativeFile;
	}

	public String getFileGeneratorInDir() {
		return fileGeneratorInDir;
	}

	public String getFileGeneratorOutDir() {
		return fileGeneratorOutDir;
	}
}