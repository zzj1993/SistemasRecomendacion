package business;

import java.util.ArrayList;
import java.util.List;

import entity.Neighborhood;
import recommender.utils.RecommendersInformation;

public class NeighborhoodBusiness {
	
	private final RecommendersInformation recommendersInformation;
	
	public NeighborhoodBusiness(RecommendersInformation recommendersInformation){
		this.recommendersInformation = recommendersInformation;
	}

	public List<Neighborhood> getAllNeighborhoods(){
		List<String> neighborhoods = recommendersInformation.getNeighborhoods();
		List<Neighborhood> result = new ArrayList<Neighborhood>();
		for(String s : neighborhoods){
			result.add(new Neighborhood(s));
		}
		return result;
	}
}
