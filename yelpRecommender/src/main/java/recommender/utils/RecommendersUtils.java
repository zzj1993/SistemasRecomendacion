package recommender.utils;

import java.util.ArrayList;
import java.util.List;

import entity.Prediction;
import entity.ReviewCF;

public class RecommendersUtils {

	public static List<Prediction> normalizeScore(List<Prediction> predictions) {
		double min = Double.MAX_VALUE;
		double max = 0;
		List<Prediction> result = new ArrayList<Prediction>();
		for (Prediction p : predictions) {
			min = Math.min(min, p.getValue());
			max = Math.max(max, p.getValue());
		}
		if (Double.compare(min, max) == 0) {
			min = 0;
		}

		for (Prediction p : predictions) {
			double value = 0.0D;
			if (Double.compare(p.getValue(), 0.0D) >= 0 && Double.compare(p.getValue(), 5.0D) <= 0) {
				value = p.getValue();
			} else {
				value = ((p.getValue() - min) * 5.0) / (max - min);
			}
			result.add(new Prediction(p.getKey(), value));
		}
		return result;
	}

	public static List<ReviewCF> normalizeReviewsScore(List<ReviewCF> reviews) {
		double min = Double.MAX_VALUE;
		double max = 0;
		List<ReviewCF> result = new ArrayList<ReviewCF>();
		for (ReviewCF p : reviews) {
			min = Math.min(min, p.getComputedStars());
			max = Math.max(max, p.getComputedStars());
		}
		for (ReviewCF p : reviews) {
			double value = 0.0D;
			if (Double.compare(p.getComputedStars(), 0.0D) >= 0 && Double.compare(p.getComputedStars(), 5.0D) <= 0) {
				value = p.getComputedStars();
			} else {
				value = 1.0 + ((p.getComputedStars() - min) * 4.0) / (max - min);
			}
			result.add(new ReviewCF(p.getBusinessId(), p.getUserId(), p.getStars(), (int) value, p.getItemStars()));
		}
		return result;
	}
}