package recommender.text;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.benchmark.quality.Judge;
import org.apache.lucene.benchmark.quality.QualityBenchmark;
import org.apache.lucene.benchmark.quality.QualityQuery;
import org.apache.lucene.benchmark.quality.QualityQueryParser;
import org.apache.lucene.benchmark.quality.QualityStats;
import org.apache.lucene.benchmark.quality.trec.TrecJudge;
import org.apache.lucene.benchmark.quality.trec.TrecTopicsReader;
import org.apache.lucene.benchmark.quality.utils.SimpleQQParser;
import org.apache.lucene.benchmark.quality.utils.SubmissionReport;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;

import recommender.utils.RecommendersInformation;
import utils.IndexFields;
import utils.TextUtils;
import entity.Prediction;

public class TextRecommender {

	private final RecommendersInformation recommendersInformation;

	private IndexSearcher searcher;
	private long recommendationTime;
	private int recommendationCount;
	private long trainingTime;
	private double precision;
	private double recall;
	private double rmse;
	private double mae;

	public TextRecommender(RecommendersInformation recommendersInformation) {
		this.recommendersInformation = recommendersInformation;
		try {
			DirectoryReader reader = DirectoryReader.open(recommendersInformation.getLuceneDirectory());
			searcher = new IndexSearcher(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<Prediction> recommendItems(String text) {
		List<Prediction> result = new ArrayList<Prediction>();
		long ini = System.currentTimeMillis();
		text = TextUtils.cleanText(text);
		try {
			Analyzer analyzer = new StandardAnalyzer();
			QueryParser parser = new QueryParser(IndexFields.TEXT, analyzer);
			Query query = parser.parse(text);
			ScoreDoc[] docs = searcher.search(query, 20).scoreDocs;
			float min = Float.MAX_VALUE;
			float max = 0;

			for (ScoreDoc d : docs) {
				min = Math.min(min, d.score);
				max = Math.max(max, d.score);
			}

			for (ScoreDoc d : docs) {
				Document hitDoc = searcher.doc(d.doc);
				String businessId = hitDoc.get(IndexFields.BUSINESS_ID);
				double value = estimatePreference(d, min, max);
				result.add(new Prediction(businessId, value));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		recommendationCount++;
		recommendationTime += (System.currentTimeMillis()) - ini;
		return result;
	}

	public double estimatePreference(ScoreDoc d, float min, float max) {
		double value = 1 + ((d.score - min) * 4) / (max - min);
		return value;
	}

	public void init() throws Exception {
		Analyzer analyzer = new StandardAnalyzer();
		QueryParser parser = new QueryParser(IndexFields.TEXT, analyzer);
		Query query = parser.parse("Goldberg");
		ScoreDoc[] docs = searcher.search(query, 20).scoreDocs;

		for (ScoreDoc d : docs) {
			Document hitDoc = searcher.doc(d.doc);
			String businessId = hitDoc.get(IndexFields.BUSINESS_ID);
			System.out.println(businessId);
		}
		System.out.println("------------TEXT RECO------------");
		long ini = System.currentTimeMillis();
		String topics = "<top>\r\n" + "<num> Number: 0\r\n" + "<title>Goldberg\r\n" + "</top>";
		String qqrels = "0	0	vcNAWiLM4dR7D2nwwJ7nCA	1\r\n"
				+ "0	0	e8kqqWU9247Nn5VAF5sDkQ	0\r\n"
				+ "0	0	BcaG79yRTstTnmdz7e5l8g	0\r\n"
				+ "0	0	IXnTbbzgUGe-gTs56nJWYw	0\r\n"
				+ "0	0	z-xZn8w20sg2jLkZKuiq3Q	0\r\n"
				+ "0	0	HpSBHeixIP2YxLy4L1f-HA	0\r\n"
				+ "0	0	ONUBVBJIHXs1XKO3F_NK8g	0\r\n"
				+ "0	0	Xhg93cMdemu5pAMkDoEdtQ	0\r\n"
				+ "0	0	LDRAVhMLceQ8fBLI44E8Ew	0\r\n"
				+ "0	0	AtjsjFzalWqJ7S9DUFQ4bw	0\r\n"
				+ "0	0	hkeYoEfdlCks_rFeIWHOkQ	0\r\n"
				+ "0	0	ShtGA_xC3QVSQ4hCGBHHDg	0\r\n"
				+ "0	0	z-vuSxK4jQvRBdsk4lFCAw	0\r\n";
		PrintWriter logger = new PrintWriter(System.out,true);
		
		TrecTopicsReader qReader = new TrecTopicsReader();
		QualityQuery qqs[] = qReader.readQueries(new BufferedReader(new StringReader(topics)));
		Judge judge = new TrecJudge(new BufferedReader(new StringReader(qqrels)));
		judge.validateData(qqs, logger);
		QualityQueryParser qqParser = new SimpleQQParser("title", "body");
		
		QualityBenchmark qrun = new QualityBenchmark(qqs, qqParser, searcher, IndexFields.BUSINESS_ID);
	    SubmissionReport submitLog = null;
	    QualityStats stats[] = qrun.execute(judge, submitLog, logger);
	   
	    QualityStats avg = QualityStats.average(stats);
	    avg.log("SUMMARY",2,logger, "  ");
		trainingTime = System.currentTimeMillis() - ini;
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
		return trainingTime / 1000.0;
	}

	public double getRecommendationTime() {
		if (recommendationCount == 0)
			return 0;
		return recommendationTime / (double) recommendationCount;
	}
}