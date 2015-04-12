package business;

import recommender.CollaborativeRecommender.CollaborativeRecommender;
import recommender.CollaborativeRecommender.ItemRecommender;
import recommender.dayTimeRecommender.DayTimeRecommender;
import recommender.hybrid.HybridRecommender;
import recommender.neighborhoodRecommender.NeighborhoodRecommender;
import entity.TrainingProgress;

public class ConfigureRecommendersBusiness {

	private final CollaborativeRecommender collaborativeRecommender;
	private final ItemRecommender itemRecommender;
	private final NeighborhoodRecommender nRecommender;
	private final DayTimeRecommender dayTimeRecommender;
	private final HybridRecommender hybridRecommender;

	public ConfigureRecommendersBusiness(CollaborativeRecommender collaborativeRecommender, ItemRecommender itemRecommender,
			NeighborhoodRecommender nRecommender, DayTimeRecommender dayTimeRecommender, HybridRecommender hybridRecommender) {
		this.collaborativeRecommender = collaborativeRecommender;
		this.itemRecommender = itemRecommender;
		this.nRecommender = nRecommender;
		this.dayTimeRecommender = dayTimeRecommender;
		this.hybridRecommender = hybridRecommender;
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
		} else if(Recommenders.HYBRID_RECOMMENDER.equals(name)){
			int progress = hybridRecommender.getTrainingProgress();
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

	public void trainHybridRecommender(String correlation) {
		String[] values = correlation.split("#");
		double nWeight = Double.parseDouble(values[0]);
		double dtWeight = Double.parseDouble(values[1]);
		double tWeight = Double.parseDouble(values[2]);
		hybridRecommender.init(nWeight, dtWeight, tWeight);
	}
}
