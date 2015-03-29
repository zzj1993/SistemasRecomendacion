package business;

import recommender.CollaborativeRecommender.CollaborativeRecommender;

public class ConfigureRecommendersBusiness {
	
	private final CollaborativeRecommender recommender;
	
	public ConfigureRecommendersBusiness(CollaborativeRecommender recommender){
		this.recommender = recommender;
	}
	
	public void trainCollaborativeRecommender(int size){
		if(size != recommender.getLastSize())
			recommender.init(size);
	}
}
