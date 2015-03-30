package recommender.CollaborativeRecommender;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

import recommender.evaluator.RMSEEvaluator;
import entity.Prediction;

public class ItemRecommender {
	
	private String dir;
	private DataModel dataModel;
	private ItemSimilarity similarity;
	private ItemBasedRecommender recommender;
	private FileGenerator generator;
	private RecommenderBuilder builder;
	private IRStatistics stats;
	
	private long trainingTime;
	private long recommendationTime;
	private int recommendationCount;
	private int lastSize;
	private double mae;
	private double rmse;
	
	public ItemRecommender(String dir, FileGenerator generator){
		this.dir = dir;
		this.generator = generator;
	}
	
	public void buildDataModel(int size){
		System.out.println("ItemRecommender: Training with "+size+"%");
		long ini = System.currentTimeMillis();
		try {
			dataModel = new FileDataModel(new File(getFile(size)), ";");
			similarity = new PearsonCorrelationSimilarity(dataModel);
			recommender = new GenericItemBasedRecommender(dataModel, similarity);
			AverageAbsoluteDifferenceRecommenderEvaluator maeEvaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
			RMSEEvaluator rmseEvaluator = new RMSEEvaluator();

			builder = new RecommenderBuilder() {
				public Recommender buildRecommender(DataModel dataModel)
						throws TasteException {
					ItemSimilarity sim = new PearsonCorrelationSimilarity(dataModel);
					return new GenericItemBasedRecommender(dataModel, sim);
				}
			};
			mae = maeEvaluator.evaluate(builder, null, new FileDataModel(new File(getFile(100)), ";"), 0.9, 1.0);
			rmse = rmseEvaluator.evaluate(builder, null, new FileDataModel(new File(getFile(100)), ";"), 0.9, 1.0);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TasteException e) {
			e.printStackTrace();
		}
		trainingTime = System.currentTimeMillis()-ini;
		System.out.println("ItemRecommender: End Training");
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
	
	public double getRMSE() {
		return rmse;
	}

	public double getMAE() {
		return mae;
	}

	public double getPrecision() {
		return stats.getPrecision();
	}

	public double getRecall() {
		return stats.getRecall();
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
}