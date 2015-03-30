package recommender.CollaborativeRecommender;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.ItemAverageRecommender;
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
import business.Correlations;
import entity.Prediction;

public class ItemRecommender {
	
	private String dir;
	private DataModel dataModel;
	private ItemSimilarity similarity;
	private ItemBasedRecommender recommender;
	private FileGenerator generator;
	
	private long trainingTime;
	private long recommendationTime;
	private int recommendationCount;
	private int lastSize;
	private String lastCorrelation;
	private double mae;
	private double rmse;
	private double precision;
	private double recall;
	
	public ItemRecommender(String dir, FileGenerator generator){
		this.dir = dir;
		this.generator = generator;
	}
	
	public void buildDataModel(int size, String correlation){
		System.out.println("ItemRecommender: Training with "+size+"%");
		lastSize = size;
		lastCorrelation = correlation;
		long ini = System.currentTimeMillis();
		try {
			dataModel = new FileDataModel(new File(getFile(size)), ";");
			DataModel testDataModel = new FileDataModel(new File(getFile(100)), ";");
			similarity = getCorrelation(correlation);
			recommender = new GenericItemBasedRecommender(dataModel, similarity);
			RecommenderBuilder builder = new RecommenderBuilder() {
				public Recommender buildRecommender(DataModel dataModel)
						throws TasteException {
					ItemSimilarity sim = new PearsonCorrelationSimilarity(dataModel);
					return new GenericItemBasedRecommender(dataModel, sim);
				}
			};
			evaluateMAE(testDataModel, builder);
			evaluateRMSE(testDataModel, builder);
//			evaluatePrecisionRecall(testDataModel, builder);
			//TODO
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TasteException e) {
			e.printStackTrace();
		}
		trainingTime = System.currentTimeMillis()-ini;
		System.out.println("ItemRecommender: End Training");
	}
	
	private ItemSimilarity getCorrelation(String correlation) throws TasteException {
		if(correlation != null && !correlation.isEmpty()){
			if(Correlations.COSINE_DISTANCE.equals(correlation)){
				return new UncenteredCosineSimilarity(dataModel);
			}else if(Correlations.EUCLIDEAN_DISTANCE.equals(correlation)){
				return new EuclideanDistanceSimilarity(dataModel);
			}else if(Correlations.JACCARD_DISTANCE.equals(correlation)){
				return new TanimotoCoefficientSimilarity(dataModel);
			}else if(Correlations.PEARSON_DISTANCE.equals(correlation)){
				new PearsonCorrelationSimilarity(dataModel);
			}
		}
		return new PearsonCorrelationSimilarity(dataModel);
	}

	private void evaluatePrecisionRecall(DataModel testDataModel, RecommenderBuilder builder) throws TasteException, IOException {
		RecommenderIRStatsEvaluator evaluator = new GenericRecommenderIRStatsEvaluator ();
		IRStatistics stats = evaluator.evaluate(builder, null, testDataModel, null, 10, 4.0, 0.1);
		precision = stats.getPrecision();
		recall = stats.getRecall();
	}

	private void evaluateRMSE(DataModel testDataModel, RecommenderBuilder builder) throws TasteException, IOException {
		RMSEEvaluator rmseEvaluator = new RMSEEvaluator();
		rmse = rmseEvaluator.evaluate(builder, null, testDataModel, 0.9, 1.0);
	}

	private void evaluateMAE(DataModel testDataModel, RecommenderBuilder builder) throws TasteException, IOException {
		AverageAbsoluteDifferenceRecommenderEvaluator maeEvaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		mae = maeEvaluator.evaluate(builder, null, testDataModel, 0.9, 1.0);
	}

	private String getFile(int size) {
		// /dir/10_itemRecommender.csv
		return dir+size+"_itemRecommender.csv";
	}

	public List<Prediction> recommendItems(String userId, int size){
		long ini = System.currentTimeMillis();
		List<Prediction> result = new ArrayList<Prediction>();
		try {
			List<RecommendedItem> items = recommender.recommend(generator.getUserGeneratedId(userId), size);
			if(items != null && !items.isEmpty()){
				for(RecommendedItem r : items){
					result.add(new Prediction(generator.getBusinessId((int) r.getItemID()), r.getValue()));
				}
			}
		} catch (TasteException e) {
			e.printStackTrace();
		}
		recommendationCount++;
		recommendationTime += (System.currentTimeMillis())-ini;
		return result;
	}
	
	public void addRating(String userId, String itemId, float value){
		try {
			dataModel.setPreference(generator.getUserGeneratedId(userId), generator.getBusinessGeneratedId(itemId), value);
			recommender.setPreference(generator.getUserGeneratedId(userId), generator.getBusinessGeneratedId(itemId), value);
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
	
	public int getLastSize(){
		return lastSize;
	}
	
	public int getDatasetSize(){
		return (generator.getReviewsSize()*lastSize)/100;
	}

	public String getLastCorrelation() {
		return lastCorrelation;
	}
}