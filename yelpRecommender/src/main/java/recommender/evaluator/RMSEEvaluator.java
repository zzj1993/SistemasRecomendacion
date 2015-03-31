package recommender.evaluator;

import org.apache.mahout.cf.taste.impl.common.FullRunningAverage;
import org.apache.mahout.cf.taste.impl.common.RunningAverage;
import org.apache.mahout.cf.taste.impl.eval.AbstractDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.model.Preference;

public class RMSEEvaluator extends AbstractDifferenceRecommenderEvaluator {

	private RunningAverage average;

	@Override
	protected void reset() {
		average = new FullRunningAverage();
	}

	@Override
	protected void processOneEstimate(float estimatedPreference, Preference realPref) {
		average.addDatum((estimatedPreference - realPref.getValue()) * (estimatedPreference - realPref.getValue()));
	}

	@Override
	protected double computeFinalEvaluation() {
		return average.getAverage();
	}

	@Override
	public String toString() {
		return "RMSEEvaluator";
	}
}