package uniandes.recomendadorPeliculas;

import java.io.File;
import java.io.IOException;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import uniandes.recomendadorPeliculas.business.Recommenders;

public class ModelTest {
	public static void main(String[] args) throws IOException, TasteException {
		
		DataModel model = new FileDataModel(new File("data/ratings.dat"),"::");
		DataModel model2 = new FileDataModel(new File("data/ratings2.dat"),"::");
		RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		
		//Jaccard - 10 vecinos - UserBased
		RecommenderBuilder builder = new Recommender1(10);
		double result = evaluator.evaluate(builder, null, model, 0.9, 1.0);
		double result2 = evaluator.evaluate(builder, null, model2, 0.9, 1.0);
		
		//Jaccard - 100 vecinos - UserBased
		builder = new Recommender1(100);
		double result3 = evaluator.evaluate(builder, null, model, 0.9, 1.0);
		double result4 = evaluator.evaluate(builder, null, model2, 0.9, 1.0);
		
		//Distancia Cos - 10 vecinos - UserBased
		builder = new Recommender2(10);
		double resul5 = evaluator.evaluate(builder, null, model, 0.9, 1.0);
		double result6 = evaluator.evaluate(builder, null, model2, 0.9, 1.0);
		
		//Distancia Cos - 100 vecinos - UserBased
		builder = new Recommender2(100);
		double resul7 = evaluator.evaluate(builder, null, model, 0.9, 1.0);
		double result8 = evaluator.evaluate(builder, null, model2, 0.9, 1.0);
		
		//Pearson - 10 vecinos - UserBased
		builder = new Recommender3(10);
		double result9 = evaluator.evaluate(builder, null, model, 0.9, 1.0);
		double result10 = evaluator.evaluate(builder, null, model2, 0.9, 1.0);
		
		//Pearson - 100 vecinos - UserBased
		builder = new Recommender3(100);
		double result11 = evaluator.evaluate(builder, null, model, 0.9, 1.0);
		double result12 = evaluator.evaluate(builder, null, model2, 0.9, 1.0);
		
		//Jaccard -  ItemBased
		builder = new Recommender4();
		double result13 = evaluator.evaluate(builder, null, model, 0.9, 1.0);
		double result14 = evaluator.evaluate(builder, null, model2, 0.9, 1.0);
		
		//Distancia Cos -  ItemBased
		builder = new Recommender5();
		double result15 = evaluator.evaluate(builder, null, model, 0.9, 1.0);
		double result16 = evaluator.evaluate(builder, null, model2, 0.9, 1.0);
		
		//Pearson -  ItemBased
		builder = new Recommender6();
		double result17 = evaluator.evaluate(builder, null, model, 0.9, 1.0);
		double result18 = evaluator.evaluate(builder, null, model2, 0.9, 1.0);
		
		System.out.println("Error promedio Jaccard - 10 vecinos - UserBased: "+result+"(380k+ ratings), "+result2+"(280k+ ratings)");
		System.out.println("Error promedio Jaccard - 100 vecinos - UserBased: "+result3+"(380k+ ratings), "+result4+"(280k+ ratings)");
		System.out.println("Error promedio Distancia Cos - 10 vecinos - UserBased: "+resul5+"(380k+ ratings), "+result6+"(280k+ ratings)");
		System.out.println("Error promedio Distancia Cos - 100 vecinos - UserBased: "+resul7+"(380k+ ratings), "+result8+"(280k+ ratings)");
		System.out.println("Error promedio Pearson - 10 vecinos - UserBased: "+result9+"(380k+ ratings), "+result10+"(280k+ ratings)");
		System.out.println("Error promedio Pearson - 100 vecinos - UserBased: "+result11+"(380k+ ratings), "+result12+"(280k+ ratings)");
		System.out.println("Error promedio Jaccard - ItemBased: "+result13+"(380k+ ratings), "+result14+"(280k+ ratings)");
		System.out.println("Error promedio Distancia Cos - ItemBased: "+result15+"(380k+ ratings), "+result16+"(280k+ ratings)");
		System.out.println("Error promedio Pearson - ItemBased: "+result17+"(380k+ ratings), "+result18+"(280k+ ratings)");
		
		
	}
	
	public static class Recommender1 implements RecommenderBuilder{
		
		int vecinos;
		
		public Recommender1(int nvecinos) {
			vecinos = nvecinos;
		}
		@Override
		public Recommender buildRecommender(DataModel dataModel)
				throws TasteException {
			UserSimilarity similarity =new TanimotoCoefficientSimilarity(dataModel);
			UserNeighborhood neighborhood = new NearestNUserNeighborhood(vecinos,similarity,dataModel);
			return new GenericUserBasedRecommender(dataModel, neighborhood, similarity);			
		}		
	}
	public static class Recommender2 implements RecommenderBuilder{
		
		int vecinos;
		
		public Recommender2(int nvecinos) {
			vecinos = nvecinos;
		}
		@Override
		public Recommender buildRecommender(DataModel dataModel)
				throws TasteException {
			UserSimilarity similarity =  new UncenteredCosineSimilarity(dataModel);
			UserNeighborhood neighborhood = new NearestNUserNeighborhood(vecinos,similarity,dataModel);
			return new GenericUserBasedRecommender(dataModel, neighborhood, similarity);			
		}		
	}
	public static class Recommender3 implements RecommenderBuilder{
		
		int vecinos;
		
		public Recommender3(int nvecinos) {
			vecinos = nvecinos;
		}
		@Override
		public Recommender buildRecommender(DataModel dataModel)
				throws TasteException {
			UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
			UserNeighborhood neighborhood = new NearestNUserNeighborhood(vecinos,similarity,dataModel);
			return new GenericUserBasedRecommender(dataModel, neighborhood, similarity);			
		}		
	}
	public static class Recommender4 implements RecommenderBuilder{		
		
		@Override
		public Recommender buildRecommender(DataModel dataModel)
				throws TasteException {
			ItemSimilarity similarity = new TanimotoCoefficientSimilarity(dataModel);		
			ItemBasedRecommender recommender = new GenericItemBasedRecommender(	dataModel,similarity);
			return new GenericItemBasedRecommender(dataModel, similarity);			
		}		
	}
	public static class Recommender5 implements RecommenderBuilder{		
		
		@Override
		public Recommender buildRecommender(DataModel dataModel)
				throws TasteException {
			ItemSimilarity similarity = new UncenteredCosineSimilarity(dataModel);	
			ItemBasedRecommender recommender = new GenericItemBasedRecommender(	dataModel,similarity);
			return new GenericItemBasedRecommender(dataModel, similarity);			
		}		
	}
	public static class Recommender6 implements RecommenderBuilder{		
		
		@Override
		public Recommender buildRecommender(DataModel dataModel)
				throws TasteException {
			ItemSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);	
			ItemBasedRecommender recommender = new GenericItemBasedRecommender(	dataModel,similarity);
			return new GenericItemBasedRecommender(dataModel, similarity);			
		}		
	}
	

	

}
