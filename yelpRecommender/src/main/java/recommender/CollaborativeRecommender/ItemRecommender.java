package recommender.CollaborativeRecommender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

import recommender.evaluator.RMSEEvaluator;
import recommender.utils.RecommendersInformation;
import business.Correlations;
import entity.Prediction;

public class ItemRecommender {

	private DataModel dataModel;
	private ItemSimilarity similarity;
	private ItemBasedRecommender recommender;
	private final RecommendersInformation recommendersInformation;

	private long trainingTime;
	private long recommendationTime;
	private int recommendationCount;
	private int lastSize;
	private String lastCorrelation;
	private double mae;
	private double rmse;
	private double precision;
	private double recall;
	private int trainingProgress;

	public ItemRecommender(RecommendersInformation recommendersInformation) {
		this.recommendersInformation = recommendersInformation;
	}

	public void buildDataModel(int size, String correlation) {
		System.out.println("ItemRecommender: Training with " + size + "%");
		lastSize = size;
		lastCorrelation = correlation;
		trainingProgress = 0;
		try {
			dataModel = recommendersInformation.getDataModel(size);
			long ini = System.currentTimeMillis();
			similarity = getCorrelation(correlation);
			recommender = new GenericItemBasedRecommender(dataModel, similarity);
			RecommenderBuilder builder = new RecommenderBuilder() {
				public Recommender buildRecommender(DataModel dataModel) throws TasteException {
					ItemSimilarity sim = new PearsonCorrelationSimilarity(dataModel);
					return new GenericItemBasedRecommender(dataModel, sim);
				}
			};
			evaluateMAE(recommendersInformation.getTestDataModel(), builder);
			evaluateRMSE(recommendersInformation.getTestDataModel(), builder);
			System.out.println("Item Recommender: Precision Recall");
			evaluatePrecisionRecall();
			trainingTime = System.currentTimeMillis() - ini;
			System.out.println("ItemRecommender: End Training");
			trainingProgress = 100;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TasteException e) {
			e.printStackTrace();
		}
	}

	private ItemSimilarity getCorrelation(String correlation) throws TasteException {
		if (correlation != null && !correlation.isEmpty()) {
			if (Correlations.COSINE_DISTANCE.equals(correlation)) {
				return new UncenteredCosineSimilarity(dataModel);
			} else if (Correlations.EUCLIDEAN_DISTANCE.equals(correlation)) {
				return new EuclideanDistanceSimilarity(dataModel);
			} else if (Correlations.JACCARD_DISTANCE.equals(correlation)) {
				return new TanimotoCoefficientSimilarity(dataModel);
			} else if (Correlations.PEARSON_DISTANCE.equals(correlation)) {
				new PearsonCorrelationSimilarity(dataModel);
			}
		}
		return new PearsonCorrelationSimilarity(dataModel);
	}

	private void evaluatePrecisionRecall() {
		List<String> randomUsers = recommendersInformation.getRandomUsers(0.3);
		int goodBusiness = recommendersInformation.getAllGoodBusinessSize();
		precision = 0.0;
		recall = 0.0;
		int i = 1;
		for (String u : randomUsers) {
			int goodRecommendations = 0;
			List<Prediction> items = recommendItems(u, 10);
			for (Prediction p : items) {
				if (Double.compare(p.getValue(), 4.0D) >= 0) {
					goodRecommendations++;
				}
			}
			if(items != null && !items.isEmpty()){
				precision += (double) goodRecommendations / (double) items.size();				
			}
			recall += (double) goodRecommendations / (double) goodBusiness;
			System.out.println("Item Recommender: Precision Recall: "+i+" de "+randomUsers.size());
			trainingProgress = i * 100 / randomUsers.size();
			i++;
		}

		precision = precision / (double) randomUsers.size();
		recall = recall / (double) randomUsers.size();
	}

	private void evaluateRMSE(DataModel testDataModel, RecommenderBuilder builder) throws TasteException, IOException {
		RMSEEvaluator rmseEvaluator = new RMSEEvaluator();
		rmse = rmseEvaluator.evaluate(builder, null, testDataModel, 0.9, 1.0);
	}

	private void evaluateMAE(DataModel testDataModel, RecommenderBuilder builder) throws TasteException, IOException {
		AverageAbsoluteDifferenceRecommenderEvaluator maeEvaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		mae = maeEvaluator.evaluate(builder, null, testDataModel, 0.9, 1.0);
	}

	public List<Prediction> recommendItems(String userId, int size) {
		long ini = System.currentTimeMillis();
		List<Prediction> result = new ArrayList<Prediction>();
		try {
			List<RecommendedItem> items = recommender.recommend(recommendersInformation.getUserGeneratedId(userId), size);
			if (items != null && !items.isEmpty()) {
				for (RecommendedItem r : items) {
					result.add(new Prediction(recommendersInformation.getBusinessId((int) r.getItemID()), r.getValue()));
				}
			}
		} catch (TasteException e) {
			e.printStackTrace();
		}
		recommendationCount++;
		recommendationTime += (System.currentTimeMillis()) - ini;
		return result;
	}

	public void addRating(String userId, String itemId, float value) {
		try {
			dataModel.setPreference(recommendersInformation.getUserGeneratedId(userId),
					recommendersInformation.getBusinessGeneratedId(itemId), value);
			recommender.setPreference(recommendersInformation.getUserGeneratedId(userId),
					recommendersInformation.getBusinessGeneratedId(itemId), value);
		} catch (TasteException e) {
			e.printStackTrace();
		}
	}

	public double getRMSE() {
		return rmse;
	}

	public double getMAE() {
		return mae;
	}

	public double getPrecision() {
		return precision;
	}

	public double getRecall() {
		return recall;
	}

	public double getTrainingTime() {
		return (double) trainingTime / 1000.0;
	}

	public double getRecommendationTime() {
		if (recommendationCount == 0)
			return 0;
		return recommendationTime / (double) recommendationCount;
	}

	public int getLastSize() {
		return lastSize;
	}

	public int getDatasetSize() {
		return (recommendersInformation.getReviewsSize() * lastSize) / 100;
	}

	public String getLastCorrelation() {
		return lastCorrelation;
	}

	public double estimatePreference(long userID, long itemID) {
		try {
			float pref = recommender.estimatePreference(userID, itemID);
			recommender.removePreference(userID, itemID);
			float newPref = recommender.estimatePreference(userID, itemID);
			recommender.setPreference(userID, itemID, pref);
			if(pref == newPref)
				System.out.println("Son iguales: "+pref+" - "+ newPref);
			return newPref;
		} catch (Exception e) {
		}
		return 0;
	}
	
	public int getTrainingProgress(){
		return trainingProgress;
	}
	
	public void deleteReview(long userID, long businessID){
		boolean error = false;
		try {
			dataModel.removePreference(userID, businessID);
			recommender.removePreference(userID, businessID);
		} catch (Exception e) {
			error = true;
		}
		if(!error)
			buildDataModel(lastSize, lastCorrelation);
	}
}