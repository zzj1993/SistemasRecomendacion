package business;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.util.BytesRef;

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
			Set<String> terms = new HashSet<>();
			for(ScoreDoc d : docs){
				Document hitDoc = searcher.doc(d.doc);
				Terms vector = reader.getTermVector(d.doc, IndexFields.TEXT);
		        TermsEnum termsEnum = null;
		        termsEnum = vector.iterator(termsEnum);
		        Map<String, Integer> frequencies = new HashMap<>();
		        BytesRef textb = null;
		        while ((textb = termsEnum.next()) != null) {
		            String term = textb.utf8ToString();
		            int freq = (int) termsEnum.totalTermFreq();
		            frequencies.put(term, freq);
		            terms.add(term);
		        }
				System.out.println(hitDoc.get(IndexFields.BUSINESS_ID));
				System.out.println(frequencies.toString());
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}