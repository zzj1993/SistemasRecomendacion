package business;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;

import recommender.utils.RecommendersInformation;
import utils.IndexFields;
import utils.TextUtils;

public class SearchBusiness {
	
	private final RecommendersInformation recommendersInformation;
	
	public SearchBusiness(RecommendersInformation recommendersInformation){
		this.recommendersInformation = recommendersInformation;
	}
	
	public void search(String text){
		text = TextUtils.cleanText(text);
		try {
			DirectoryReader reader = DirectoryReader.open(recommendersInformation.getLuceneDirectory());
			IndexSearcher searcher = new IndexSearcher(reader);
			Analyzer analyzer = new StandardAnalyzer();
			QueryParser parser = new QueryParser(IndexFields.TEXT, analyzer);
			Query query = parser.parse(text);
			ScoreDoc[] docs = searcher.search(query, 10).scoreDocs;
			
			for(ScoreDoc d : docs){
				Document hitDoc = searcher.doc(d.doc);
				System.out.println(hitDoc.get(IndexFields.BUSINESS_ID));
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}