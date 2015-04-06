package utils;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class TextUtils {
	
	public static String cleanText(String text){
		try {
			return stem(removeStopWords(text));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static String stem(String text) throws IOException {
		StringBuilder result = new StringBuilder();
		Analyzer analyzer = new StandardAnalyzer();
		TokenStream ts = analyzer.tokenStream("myfield", new StringReader(text));
		CharTermAttribute charTermAttribute = ts.addAttribute(CharTermAttribute.class);
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

	private static String removeStopWords(String text) throws IOException {
		StringBuilder result = new StringBuilder();
		Analyzer analyzer = new StandardAnalyzer();
		TokenStream ts = analyzer.tokenStream("myfield", new StringReader(text));
		CharTermAttribute charTermAttribute = ts.addAttribute(CharTermAttribute.class);
		StopFilter stop = new StopFilter(ts, StopAnalyzer.ENGLISH_STOP_WORDS_SET);
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

}
