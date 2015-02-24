package uniandes.recomendadorPeliculas;

import java.io.File;
import java.io.IOException;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
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

import uniandes.recomendadorPeliculas.ModelTest.Recommender2;

public class ModelTest2 {
	public static void main(String[] args) throws IOException, TasteException {

		File folder = new File("data/snapshots/");
		File[] listOfFiles = folder.listFiles();
		int[] tamañosVecinos = { 10, 50, 100, 500, 1000 };
		RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();

		for (File f : listOfFiles) {
			File data = new File(f.getAbsolutePath()+"/ratings.dat");
			DataModel model = new FileDataModel(data, "::");
			RecommenderBuilder builder = null;
			double result = 0.0;
			for (int i = 0; i < tamañosVecinos.length; i++) {
				builder = new Recommender1(tamañosVecinos[i]);
				result = evaluator.evaluate(builder, null, model, 0.9,
						1.0);
				System.out.println("$$DataSet:" + f.getName()
						+ ":Recommender:User - TanimotoCoefficientSimilarity:tamanio:"
						+ tamañosVecinos[i] + ":Result:" + result);

				builder = new Recommender2(tamañosVecinos[i]);
				result = evaluator.evaluate(builder, null, model, 0.9, 1.0);
				System.out.println("$$DataSet:" + f.getName()
						+ ":Recommender:User - UncenteredCosineSimilarity:tamanio:"
						+ tamañosVecinos[i] + ":Result:" + result);
				
				builder = new Recommender3(tamañosVecinos[i]);
				result = evaluator.evaluate(builder, null, model, 0.9, 1.0);
				System.out.println("$$DataSet:" + f.getName()
						+ ":Recommender:User - PearsonCorrelationSimilarity:tamanio:"
						+ tamañosVecinos[i] + ":Result:" + result);
			}
			builder = new Recommender4();
			result = evaluator.evaluate(builder, null, model, 0.9, 1.0);
			System.out.println("$$DataSet:" + f.getName()
					+ ":Recommender:Item - UncenteredCosineSimilarity"+ ":Result:" + result);
			
			builder = new Recommender5();
			result = evaluator.evaluate(builder, null, model, 0.9, 1.0);
			System.out.println("$$DataSet:" + f.getName()
					+ ":Recommender:Item - UncenteredCosineSimilarity"+ ":Result:" + result);
			
			builder = new Recommender6();
			result = evaluator.evaluate(builder, null, model, 0.9, 1.0);
			System.out.println("$$DataSet:" + f.getName()
					+ ":Recommender:Item - UncenteredCosineSimilarity"+":Result:" + result);
		}
	}

	public static class Recommender1 implements RecommenderBuilder {

		int vecinos;

		public Recommender1(int nvecinos) {
			vecinos = nvecinos;
		}

		@Override
		public Recommender buildRecommender(DataModel dataModel)
				throws TasteException {
			UserSimilarity similarity = new TanimotoCoefficientSimilarity(
					dataModel);
			UserNeighborhood neighborhood = new NearestNUserNeighborhood(
					vecinos, similarity, dataModel);
			return new GenericUserBasedRecommender(dataModel, neighborhood,
					similarity);
		}
	}

	public static class Recommender2 implements RecommenderBuilder {

		int vecinos;

		public Recommender2(int nvecinos) {
			vecinos = nvecinos;
		}

		@Override
		public Recommender buildRecommender(DataModel dataModel)
				throws TasteException {
			UserSimilarity similarity = new UncenteredCosineSimilarity(
					dataModel);
			UserNeighborhood neighborhood = new NearestNUserNeighborhood(
					vecinos, similarity, dataModel);
			return new GenericUserBasedRecommender(dataModel, neighborhood,
					similarity);
		}
	}

	public static class Recommender3 implements RecommenderBuilder {

		int vecinos;

		public Recommender3(int nvecinos) {
			vecinos = nvecinos;
		}

		@Override
		public Recommender buildRecommender(DataModel dataModel)
				throws TasteException {
			UserSimilarity similarity = new PearsonCorrelationSimilarity(
					dataModel);
			UserNeighborhood neighborhood = new NearestNUserNeighborhood(
					vecinos, similarity, dataModel);
			return new GenericUserBasedRecommender(dataModel, neighborhood,
					similarity);
		}
	}

	public static class Recommender4 implements RecommenderBuilder {

		@Override
		public Recommender buildRecommender(DataModel dataModel)
				throws TasteException {
			ItemSimilarity similarity = new TanimotoCoefficientSimilarity(
					dataModel);
			ItemBasedRecommender recommender = new GenericItemBasedRecommender(
					dataModel, similarity);
			return recommender;
		}
	}

	public static class Recommender5 implements RecommenderBuilder {

		@Override
		public Recommender buildRecommender(DataModel dataModel)
				throws TasteException {
			ItemSimilarity similarity = new UncenteredCosineSimilarity(
					dataModel);
			ItemBasedRecommender recommender = new GenericItemBasedRecommender(
					dataModel, similarity);
			return recommender;
		}
	}

	public static class Recommender6 implements RecommenderBuilder {

		@Override
		public Recommender buildRecommender(DataModel dataModel)
				throws TasteException {
			ItemSimilarity similarity = new PearsonCorrelationSimilarity(
					dataModel);
			ItemBasedRecommender recommender = new GenericItemBasedRecommender(
					dataModel, similarity);
			return recommender;
		}
	}
}