package recommender.CollaborativeRecommender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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
					return new GenericItemBasedRecommender(dataModel, similarity);
				}
			};
			evaluateMAE(recommendersInformation.getTestDataModel(), builder);
			evaluateRMSE(recommendersInformation.getTestDataModel(), builder);
			System.out.println("Item Recommender: Precision Recall");
			evaluatePrecisionRecall();
			trainingTime = System.currentTimeMillis() - ini;
			System.out.println("ItemRecommender: End Training");
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (TasteException e) {
			System.err.println(e.getMessage());
		}
		trainingProgress = 100;
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
		List<String> randomUsers = recommendersInformation.getRandomUsers();
		int goodBusiness = recommendersInformation.getAllGoodBusinessSize();
		precision = 0.0;
		recall = 0.0;
		int i = 1;
		for (String u : randomUsers) {
			List<Prediction> items = recommendItems(u, 10);
			long goodRecommendations = items.parallelStream().filter(p -> Double.compare(p.getValue(), 4.0D) >= 0).count();
			if (items != null && !items.isEmpty()) {
				precision += (double) goodRecommendations / (double) items.size();
			}
			recall += (double) goodRecommendations / (double) goodBusiness;
			System.out.println("Item Recommender: Precision Recall: " + i + " de " + randomUsers.size());
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
			System.err.println(e.getMessage());
		}
		recommendationCount++;
		recommendationTime += (System.currentTimeMillis()) - ini;

		List<String> businessReviewed = recommendersInformation.getBusinessReviewed(userId);

		for (Iterator<Prediction> iterator = result.iterator(); iterator.hasNext();) {
			Prediction p = iterator.next();
			if (businessReviewed.contains(p.getKey())) {
				iterator.remove();
			}
		}

		return result;
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
			return pref;
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return 0;
	}

	public int getTrainingProgress() {
		return trainingProgress;
	}
}