package business;

import java.util.List;

import recommender.utils.RecommendersInformation;

public class NeighborhoodBusiness {
	
	private final RecommendersInformation recommendersInformation;
	
	public NeighborhoodBusiness(RecommendersInformation recommendersInformation){
		this.recommendersInformation = recommendersInformation;
	}

	public List<String> getAllNeighborhoods(){
		return recommendersInformation.getNeighborhoods();
	}
}
