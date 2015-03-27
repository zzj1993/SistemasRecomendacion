package sentimentAnalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import weka.classifiers.bayes.NaiveBayesMultinomialText;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import com.google.inject.internal.util.Join;

public class NaiveBayes {

	private NaiveBayesMultinomialText classifier;
	private String modelFile;
	private Instances dataRaw;

	public NaiveBayes(String outputModel) throws Exception {
		init(outputModel);
		try{
			loadModel(outputModel);			
		}catch(Exception e){
			train();
		}
	}
	
	private void init(String outputModel) throws Exception {
		classifier = new NaiveBayesMultinomialText();
		modelFile = outputModel;
		ArrayList<Attribute> atts = new ArrayList<Attribute>(2);
		ArrayList<String> classVal = new ArrayList<String>();
		classVal.add(SentimentClass.ThreeWayClazz.NEGATIVE.name());
		classVal.add(SentimentClass.ThreeWayClazz.NEUTRAL.name());
		classVal.add(SentimentClass.ThreeWayClazz.POSITIVE.name());
		atts.add(new Attribute("content", (ArrayList<String>) null));
		atts.add(new Attribute("@@class@@", classVal));
		dataRaw = new Instances("TrainingInstances", atts, 10);
	}

	private void addTrainingInstance(SentimentClass.ThreeWayClazz threeWayClazz,
			String[] words) {
		double[] instanceValue = new double[dataRaw.numAttributes()];
		instanceValue[0] = dataRaw.attribute(0).addStringValue(
				Join.join(" ", words));
		instanceValue[1] = threeWayClazz.ordinal();
		dataRaw.add(new DenseInstance(1.0, instanceValue));
		dataRaw.setClassIndex(1);
	}

	public void trainModel() throws Exception {
		classifier.buildClassifier(dataRaw);
	}

	public void testModel() throws Exception {
		Evaluation eTest = new Evaluation(dataRaw);
		eTest.evaluateModel(classifier, dataRaw);
		String strSummary = eTest.toSummaryString();
		System.out.println(strSummary);
	}

	public void showInstances() {
		System.out.println(dataRaw);
	}

	public Instances getDataRaw() {
		return dataRaw;
	}

	public void saveModel() throws Exception {
		weka.core.SerializationHelper.write(modelFile, classifier);
	}

	public void loadModel(String _modelFile) throws Exception {
		NaiveBayesMultinomialText classifier = (NaiveBayesMultinomialText) weka.core.SerializationHelper
				.read(_modelFile);
		this.classifier = classifier;
	}

	public SentimentClass.ThreeWayClazz classify(String sentence)
			throws Exception {
		double[] instanceValue = new double[dataRaw.numAttributes()];
		instanceValue[0] = dataRaw.attribute(0).addStringValue(sentence);

		Instance toClassify = new DenseInstance(1.0, instanceValue);
		dataRaw.setClassIndex(1);
		toClassify.setDataset(dataRaw);

		double prediction = this.classifier.classifyInstance(toClassify);

		double distribution[] = this.classifier
				.distributionForInstance(toClassify);

		if (distribution[0] != distribution[1])
			return SentimentClass.ThreeWayClazz.values()[(int) prediction];
		else
			return SentimentClass.ThreeWayClazz.NEUTRAL;
	}
	
	public void train() throws Exception{
		String[] files = {
				"/Users/Pisco/Downloads/yelp/new/very_bad_reviews.csv",
				"/Users/Pisco/Downloads/yelp/new/bad_reviews.csv",
				"/Users/Pisco/Downloads/yelp/new/neutral_reviews.csv",
				"/Users/Pisco/Downloads/yelp/new/good_reviews.csv",
				"/Users/Pisco/Downloads/yelp/new/very_good_reviews.csv" };

		for (String s : files) {
			System.out.println(s);
			BufferedReader bf = new BufferedReader(new FileReader(new File(s)));
			String str = bf.readLine();// encabezado
			str = bf.readLine();

			while (str != null) {
				String[] linea = str.split(";");
				// "stars";"text"
				int stars = Integer.parseInt(linea[1].replace("\"", ""));
				String txt = stem(removeStopWords(linea[2].replace("\"", "")));

				SentimentClass.ThreeWayClazz value = SentimentClass.ThreeWayClazz
						.values()[1];
				if (stars < 3) {
					value = SentimentClass.ThreeWayClazz.values()[0];
				} else {
					value = SentimentClass.ThreeWayClazz.values()[2];
				}
				addTrainingInstance(value, txt.split("\\s+"));
				str = bf.readLine();
			}
			bf.close();
		}

		trainModel();
		saveModel();
		System.out.println("Testing model");
		testModel();
	}

	private String removeStopWords(String text) throws IOException {
		StringBuilder result = new StringBuilder();
		Analyzer analyzer = new StandardAnalyzer();
		TokenStream ts = analyzer
				.tokenStream("myfield", new StringReader(text));
		CharTermAttribute charTermAttribute = ts
				.addAttribute(CharTermAttribute.class);
		StopFilter stop = new StopFilter(ts,
				StopAnalyzer.ENGLISH_STOP_WORDS_SET);
		try {
			ts.reset(); // Resets this stream to the beginning. (Required)
			while (stop.incrementToken()) {
				String token = charTermAttribute.toString().toString();
				result.append(token).append(" ");
			}
			ts.end();
		} finally {
			ts.close();
		}
		return result.toString();
	}

	private String stem(String text) throws IOException {
		StringBuilder result = new StringBuilder();
		Analyzer analyzer = new StandardAnalyzer();
		TokenStream ts = analyzer
				.tokenStream("myfield", new StringReader(text));
		CharTermAttribute charTermAttribute = ts
				.addAttribute(CharTermAttribute.class);
		PorterStemFilter stem = new PorterStemFilter(ts);
		try {
			ts.reset(); // Resets this stream to the beginning. (Required)
			while (stem.incrementToken()) {
				String token = charTermAttribute.toString().toString();
				result.append(token).append(" ");
			}
			ts.end();
		} finally {
			ts.close();
		}
		return result.toString();
	}

	public static void main(String[] args) throws Exception {
		String outputModel = "data/sentiment.model";
		NaiveBayes trainer = new NaiveBayes(outputModel);
		System.out.println(trainer.classify("Went to the bridal show, it waqs ok"));
	}
}
