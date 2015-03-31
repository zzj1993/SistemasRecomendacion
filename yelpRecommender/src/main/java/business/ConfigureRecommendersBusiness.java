package business;

import recommender.CollaborativeRecommender.CollaborativeRecommender;
import recommender.CollaborativeRecommender.ItemRecommender;
import recommender.dayTimeRecommender.DayTimeRecommender;
import recommender.neighborhoodRecommender.NeighborhoodRecommender;

public class ConfigureRecommendersBusiness {

	private final CollaborativeRecommender collaborativeRecommender;
	private final ItemRecommender itemRecommender;
	private final NeighborhoodRecommender nRecommender;
	private final DayTimeRecommender dayTimeRecommender;

	public ConfigureRecommendersBusiness(CollaborativeRecommender collaborativeRecommender, ItemRecommender itemRecommender,
			NeighborhoodRecommender nRecommender, DayTimeRecommender dayTimeRecommender) {
		this.collaborativeRecommender = collaborativeRecommender;
		this.itemRecommender = itemRecommender;
		this.nRecommender = nRecommender;
		this.dayTimeRecommender = dayTimeRecommender;
	}

	public void trainCollaborativeRecommender(int size) {
		if (size != collaborativeRecommender.getLastSize())
			collaborativeRecommender.init(size);
	}

	public void trainItemRecommender(int size, String correlation) {
		if (size != itemRecommender.getLastSize() || !correlation.equals(itemRecommender.getLastCorrelation())) {
			itemRecommender.buildDataModel(size, correlation);
			nRecommender.buildDataModel();
			dayTimeRecommender.buildDataModel();
		}
	}
}
