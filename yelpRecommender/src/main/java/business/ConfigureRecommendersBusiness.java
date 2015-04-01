package business;

import entity.TrainingProgress;
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
	
	public TrainingProgress getTrainingProgress(String name){
		if(Recommenders.COLLABORATIVE_RECOMMENDER.equals(name)){
			return new TrainingProgress(collaborativeRecommender.getTrainingProgress());
		}else if(Recommenders.ITEM_RECOMMENDER.equals(name)){
			int itemRecommenderProgress = itemRecommender.getTrainingProgress();
			int nRecommenderProgress = nRecommender.getTrainingProgress();
			int dayTimeRecommenderProgress = dayTimeRecommender.getTrainingProgress();
			int progress = (itemRecommenderProgress + nRecommenderProgress + dayTimeRecommenderProgress) / 3;
			return new TrainingProgress(progress);
		}
		return new TrainingProgress(100);
	}
}
