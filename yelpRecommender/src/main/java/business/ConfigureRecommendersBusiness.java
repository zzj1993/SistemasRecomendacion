package business;

import recommender.CollaborativeRecommender.CollaborativeRecommender;
import recommender.CollaborativeRecommender.ItemRecommender;
import recommender.dayTimeRecommender.DayTimeRecommender;
import recommender.neighborhoodRecommender.NeighborhoodRecommender;
import entity.TrainingProgress;

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
		}
	}

	public TrainingProgress getTrainingProgress(String name) {
		if (Recommenders.COLLABORATIVE_RECOMMENDER.equals(name)) {
			return new TrainingProgress(collaborativeRecommender.getTrainingProgress());
		} else if (Recommenders.ITEM_RECOMMENDER.equals(name)) {
			int itemRecommenderProgress = itemRecommender.getTrainingProgress();
			return new TrainingProgress(itemRecommenderProgress);
		} else if (Recommenders.NEIGHBORHOOD_RECOMMENDER.equals(name)) {
			int progress = nRecommender.getTrainingProgress();
			return new TrainingProgress(progress);
		} else if (Recommenders.DAYTIME_RECOMMENDER.equals(name)) {
			int progress = dayTimeRecommender.getTrainingProgress();
			return new TrainingProgress(progress);
		}
		return new TrainingProgress(100);
	}

	public void trainNeighborhoodRecommender(String correlation) {
		if (!correlation.equals(nRecommender.getLastRecommender())) {
			nRecommender.buildDataModel(correlation);
		}
	}

	public void trainDayTimeRecommender(String correlation) {
		if (!correlation.equals(dayTimeRecommender.getLastRecommender())) {
			dayTimeRecommender.buildDataModel(correlation);
		}
	}
}
