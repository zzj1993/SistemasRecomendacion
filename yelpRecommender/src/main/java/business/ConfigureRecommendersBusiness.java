package business;

import recommender.CollaborativeRecommender.CollaborativeRecommender;
import recommender.CollaborativeRecommender.ItemRecommender;

public class ConfigureRecommendersBusiness {

	private final CollaborativeRecommender collaborativeRecommender;
	private final ItemRecommender itemRecommender;

	public ConfigureRecommendersBusiness(
			CollaborativeRecommender collaborativeRecommender,
			ItemRecommender itemRecommender) {
		this.collaborativeRecommender = collaborativeRecommender;
		this.itemRecommender = itemRecommender;
	}

	public void trainCollaborativeRecommender(int size) {
		if (size != collaborativeRecommender.getLastSize())
			collaborativeRecommender.init(size);
	}

	public void trainItemRecommender(int size) {
		if (size != itemRecommender.getLastSize()) {
			itemRecommender.buildDataModel(size);
		}
	}
}
