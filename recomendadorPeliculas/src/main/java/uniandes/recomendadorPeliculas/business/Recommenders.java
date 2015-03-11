package uniandes.recomendadorPeliculas.business;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
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
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import uniandes.recomendadorPeliculas.config.DataConfig;

public class Recommenders {

	public static final int JACCARD = 1;

	public static final int COS_SIM = 2;

	public static final int PEARSON = 3;

	public static final int USERS_TYPE = 1;

	public static final int ITEM_TYPE = 2;

	private DataModel model;

	public Recommenders(DataConfig dataConfig) {
		reBuildModel(dataConfig);
	}

	public void reBuildModel(DataConfig dataConfig) {
		try {
			File f = new File(dataConfig.getDir() + dataConfig.getRatings());
			this.model = new FileDataModel(f, "::");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public float estimatePreference(Long userID, Long itemID, int modelType,
			int neighborhoodSize, int nRecommendations, int type)
			throws TasteException {
		Recommender recommender = getRecommender(type, modelType,
				neighborhoodSize);
		float preference = recommender.estimatePreference(userID, itemID);
		return preference;
	}

	public List<RecommendedItem> getRecommendation(int userid, int modelType,
			int neighborhoodSize, int nRecommendations, int type)
			throws TasteException {
		Recommender recommender = getRecommender(type, modelType,
				neighborhoodSize);
		List<RecommendedItem> recommendations = recommender.recommend(userid,
				nRecommendations);
		return recommendations;
	}

	private Recommender getRecommender(int type, int modelType,
			int neighborhoodSize) throws TasteException {
		if (type == USERS_TYPE) {
			return getUserRecommender(neighborhoodSize, modelType);
		} else {
			return getItemRecommender(modelType);
		}
	}

	private Recommender getItemRecommender(int modelType) throws TasteException {
		ItemSimilarity similarity = getItemsSimilarity(modelType);
		ItemBasedRecommender recommender = new GenericItemBasedRecommender(
				model, similarity);
		return recommender;
	}

	private Recommender getUserRecommender(int neighborhoodSize, int modelType)
			throws TasteException {
		UserSimilarity similarity = getUsersSimilarity(modelType);
		UserNeighborhood neighborhood = new NearestNUserNeighborhood(
				neighborhoodSize, similarity, model);
		UserBasedRecommender recommender = new GenericUserBasedRecommender(
				model, neighborhood, similarity);
		return recommender;
	}

	private UserSimilarity getUsersSimilarity(int modeltype)
			throws TasteException {
		UserSimilarity similarity = null;

		if (modeltype == JACCARD) {
			similarity = new TanimotoCoefficientSimilarity(model);
		} else if (modeltype == COS_SIM) {
			similarity = new UncenteredCosineSimilarity(model);
		}

		else if (modeltype == PEARSON) {
			similarity = new PearsonCorrelationSimilarity(model);
		}

		return similarity;
	}

	private ItemSimilarity getItemsSimilarity(int modeltype)
			throws TasteException {
		ItemSimilarity similarity = null;

		if (modeltype == JACCARD) {
			similarity = new TanimotoCoefficientSimilarity(model);
		} else if (modeltype == COS_SIM) {
			similarity = new UncenteredCosineSimilarity(model);
		}

		else if (modeltype == PEARSON) {
			similarity = new PearsonCorrelationSimilarity(model);
		}

		return similarity;
	}

	public static final List<Long> Recommended2Id(List<RecommendedItem> list) {
		List<Long> resp = new ArrayList<Long>();
		for (RecommendedItem recommendation : list) {
			resp.add((recommendation.getItemID()));
		}
		return resp;
	}
}
