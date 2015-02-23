package uniandes.recomendadorPeliculas.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RecommendationParam {

	private final Integer userid;
	private final Integer modeltype;
	private final Integer neighborhoodSize;
	private final Integer nRecommendations;

	@JsonCreator
	public RecommendationParam(@JsonProperty("userid") Integer userid,
			@JsonProperty("modeltype") Integer modeltype,
			@JsonProperty("neighborhoodSize") Integer neighborhoodSize,
			@JsonProperty("nRecommendations") Integer nRecommendations) {
		this.userid = userid;
		this.modeltype = modeltype;
		this.neighborhoodSize = neighborhoodSize;
		this.nRecommendations = nRecommendations;
	}

	public Integer getUserid() {
		return userid;
	}

	public Integer getModeltype() {
		return modeltype;
	}

	public Integer getNeighborhoodSize() {
		return neighborhoodSize;
	}

	public Integer getnRecommendations() {
		return nRecommendations;
	}
}
