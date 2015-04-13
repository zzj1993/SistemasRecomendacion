package recommender.evaluator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import business.ConfigureRecommendersBusiness;
import business.Correlations;
import business.EvaluationBusiness;
import business.Recommenders;
import entity.EvaluationStatistics;

public class RecommenderEvaluator {

	private ConfigureRecommendersBusiness configure;
	private EvaluationBusiness evaluation;

	public RecommenderEvaluator(ConfigureRecommendersBusiness configure, EvaluationBusiness evaluation) {
		this.configure = configure;
		this.evaluation = evaluation;
	}

	public void evaluate() throws IOException {
//		evaluateCollaborative();
//		evaluateItem();
//		evaluateText();
		evaluateNeigh();
		evaluateDayTime();
	}

	private void evaluateText() throws IOException {
		String dir = "/Users/Pisco/Downloads/yelp/Results/";
		FileWriter fw = new FileWriter(new File(dir + "text.csv"));
		String str = "Size,Recommender,Correlacion,MAE,RMSE,Precision,Recall,Training Time,Recommendation Time\r\n";
		fw.write(str);

		List<EvaluationStatistics> statistics = evaluation.getStatistics();
		for (EvaluationStatistics e : statistics) {
			if (e.getName().equals(Recommenders.TEXT_RECOMMENDER)) {
				str = e.getSize() + "," + Recommenders.TEXT_RECOMMENDER + ",Nada," + e.getMae() + "," + e.getRmse() + ","
						+ e.getPrecision() + "," + e.getRecall() + "," + e.getTrainingTime() + "," + e.getRecommendationTime()
						+ "\r\n";
				fw.write(str);
			}
		}

		fw.close();
	}

	private void evaluateDayTime() throws IOException {
		String dir = "/Users/Pisco/Downloads/yelp/Results/";
		FileWriter fw = new FileWriter(new File(dir + "day.csv"));
		FileWriter fw1 = new FileWriter(new File(dir + "hybridDay.csv"));
		String str = "Size,Recommender,Correlacion,MAE,RMSE,Precision,Recall,Training Time,Recommendation Time\r\n";
		fw.write(str);
		fw1.write(str);
		String[] cs = { Correlations.COSINE_DISTANCE, Correlations.EUCLIDEAN_DISTANCE, Correlations.JACCARD_DISTANCE,
				Correlations.PEARSON_DISTANCE };
		// COLLAB
		for (int i = 10; i < 110; i += 10) {
			System.out.println("DAY: " + i);
			configure.trainCollaborativeRecommender(i);
			configure.trainDayTimeRecommender(Recommenders.COLLABORATIVE_RECOMMENDER);
			configure.trainHybridRecommender("1#1#1");
			List<EvaluationStatistics> statistics = evaluation.getStatistics();
			for (EvaluationStatistics e : statistics) {
				if (e.getName().equals(Recommenders.DAYTIME_RECOMMENDER)) {
					str = e.getSize() + "," + Recommenders.COLLABORATIVE_RECOMMENDER + ",Nada," + e.getMae() + "," + e.getRmse()
							+ "," + e.getPrecision() + "," + e.getRecall() + "," + e.getTrainingTime() + ","
							+ e.getRecommendationTime() + "\r\n";
					fw.write(str);
				} else if (e.getName().equals(Recommenders.HYBRID_RECOMMENDER)) {
					str = e.getSize() + "," + Recommenders.DAYTIME_RECOMMENDER + "," + Recommenders.COLLABORATIVE_RECOMMENDER
							+ "," + e.getMae() + "," + e.getRmse() + "," + e.getPrecision() + "," + e.getRecall() + ","
							+ e.getTrainingTime() + "," + e.getRecommendationTime() + "\r\n";
					fw1.write(str);
				}
			}
		}

		// ITEM
		for (String c : cs) {
			for (int i = 10; i < 110; i += 10) {
				System.out.println("DAY 2: " + i);
				configure.trainItemRecommender(i, c);
				configure.trainDayTimeRecommender(Recommenders.ITEM_RECOMMENDER);
				configure.trainHybridRecommender("1#1#1");
				List<EvaluationStatistics> statistics = evaluation.getStatistics();
				for (EvaluationStatistics e : statistics) {
					if (e.getName().equals(Recommenders.DAYTIME_RECOMMENDER)) {
						str = e.getSize() + "," + Recommenders.ITEM_RECOMMENDER + "," + c + "," + e.getMae() + "," + e.getRmse()
								+ "," + e.getPrecision() + "," + e.getRecall() + "," + e.getTrainingTime() + ","
								+ e.getRecommendationTime() + "\r\n";
						fw.write(str);
					} else if (e.getName().equals(Recommenders.HYBRID_RECOMMENDER)) {
						str = e.getSize() + "," + Recommenders.DAYTIME_RECOMMENDER + "," + Recommenders.ITEM_RECOMMENDER + ","
								+ e.getMae() + "," + e.getRmse() + "," + e.getPrecision() + "," + e.getRecall() + ","
								+ e.getTrainingTime() + "," + e.getRecommendationTime() + "\r\n";
						fw1.write(str);
					}
				}
			}
		}

		fw.close();
		fw1.close();
	}

	private void evaluateNeigh() throws IOException {
		String dir = "/Users/Pisco/Downloads/yelp/Results/";
		FileWriter fw = new FileWriter(new File(dir + "neigh.csv"));
		FileWriter fw1 = new FileWriter(new File(dir + "hybridNeigh.csv"));
		String str = "Size,Recommender,Correlacion,MAE,RMSE,Precision,Recall,Training Time,Recommendation Time\r\n";
		fw.write(str);
		fw1.write(str);
		String[] cs = { Correlations.COSINE_DISTANCE, Correlations.EUCLIDEAN_DISTANCE, Correlations.JACCARD_DISTANCE,
				Correlations.PEARSON_DISTANCE };
		// COLLAB
		for (int i = 10; i < 110; i += 10) {
			System.out.println("NEIGH: " + i);
			configure.trainCollaborativeRecommender(i);
			configure.trainNeighborhoodRecommender(Recommenders.COLLABORATIVE_RECOMMENDER);
			configure.trainHybridRecommender("1#1#1");
			List<EvaluationStatistics> statistics = evaluation.getStatistics();
			for (EvaluationStatistics e : statistics) {
				if (e.getName().equals(Recommenders.NEIGHBORHOOD_RECOMMENDER)) {
					str = e.getSize() + "," + Recommenders.COLLABORATIVE_RECOMMENDER + ",Nada," + e.getMae() + "," + e.getRmse()
							+ "," + e.getPrecision() + "," + e.getRecall() + "," + e.getTrainingTime() + ","
							+ e.getRecommendationTime() + "\r\n";
					fw.write(str);
				}else if (e.getName().equals(Recommenders.HYBRID_RECOMMENDER)) {
					str = e.getSize() + "," + Recommenders.NEIGHBORHOOD_RECOMMENDER + "," + Recommenders.COLLABORATIVE_RECOMMENDER
							+ "," + e.getMae() + "," + e.getRmse() + "," + e.getPrecision() + "," + e.getRecall() + ","
							+ e.getTrainingTime() + "," + e.getRecommendationTime() + "\r\n";
					fw1.write(str);
				}
			}
		}

		// ITEM
		for (String c : cs) {
			for (int i = 10; i < 110; i += 10) {
				System.out.println("NEIGH 2: " + i);
				configure.trainItemRecommender(i, c);
				configure.trainNeighborhoodRecommender(Recommenders.ITEM_RECOMMENDER);
				configure.trainHybridRecommender("1#1#1");
				List<EvaluationStatistics> statistics = evaluation.getStatistics();
				for (EvaluationStatistics e : statistics) {
					if (e.getName().equals(Recommenders.NEIGHBORHOOD_RECOMMENDER)) {
						str = e.getSize() + "," + Recommenders.ITEM_RECOMMENDER + "," + c + "," + e.getMae() + "," + e.getRmse()
								+ "," + e.getPrecision() + "," + e.getRecall() + "," + e.getTrainingTime() + ","
								+ e.getRecommendationTime() + "\r\n";
						fw.write(str);
					}else if (e.getName().equals(Recommenders.HYBRID_RECOMMENDER)) {
						str = e.getSize() + "," + Recommenders.NEIGHBORHOOD_RECOMMENDER + "," + Recommenders.ITEM_RECOMMENDER + ","
								+ e.getMae() + "," + e.getRmse() + "," + e.getPrecision() + "," + e.getRecall() + ","
								+ e.getTrainingTime() + "," + e.getRecommendationTime() + "\r\n";
						fw1.write(str);
					}
				}
			}
		}

		fw.close();
		fw1.close();
	}

	private void evaluateItem() throws IOException {
		String dir = "/Users/Pisco/Downloads/yelp/Results/";
		FileWriter fw = new FileWriter(new File(dir + "item.csv"));
		String str = "Size,Correlacion,MAE,RMSE,Precision,Recall,Training Time,Recommendation Time\r\n";
		fw.write(str);
		String[] cs = { Correlations.COSINE_DISTANCE, Correlations.EUCLIDEAN_DISTANCE, Correlations.JACCARD_DISTANCE,
				Correlations.PEARSON_DISTANCE };
		for (String c : cs) {
			for (int i = 10; i < 110; i += 10) {
				System.out.println("ITEM: " + i);
				configure.trainItemRecommender(i, c);
				List<EvaluationStatistics> statistics = evaluation.getStatistics();
				for (EvaluationStatistics e : statistics) {
					if (e.getName().equals(Recommenders.ITEM_RECOMMENDER)) {
						str = e.getSize() + "," + c + "," + e.getMae() + "," + e.getRmse() + "," + e.getPrecision() + ","
								+ e.getRecall() + "," + e.getTrainingTime() + "," + e.getRecommendationTime() + "\r\n";
						fw.write(str);
					}
				}
			}
		}
		fw.close();
	}

	private void evaluateCollaborative() throws IOException {
		String dir = "/Users/Pisco/Downloads/yelp/Results/";
		FileWriter fw = new FileWriter(new File(dir + "collaborative.csv"));
		String str = "Size,MAE,RMSE,Precision,Recall,Training Time,Recommendation Time\r\n";
		fw.write(str);
		for (int i = 10; i < 110; i += 10) {
			System.out.println("COLLABORATIVE: " + i);
			configure.trainCollaborativeRecommender(i);
			List<EvaluationStatistics> statistics = evaluation.getStatistics();
			for (EvaluationStatistics e : statistics) {
				if (e.getName().equals(Recommenders.COLLABORATIVE_RECOMMENDER)) {
					str = e.getSize() + "," + e.getMae() + "," + e.getRmse() + "," + e.getPrecision() + "," + e.getRecall() + ","
							+ e.getTrainingTime() + "," + e.getRecommendationTime() + "\r\n";
					fw.write(str);
				}
			}
		}
		fw.close();
	}
}