package converter.jsonConverter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ReviewConverter {

	public static void convert(String fileIn, String fileOut)
			throws IOException, ParseException {
		// Print header
		FileWriter fw = new FileWriter(new File(fileOut));
		StringBuilder lineaW = new StringBuilder();
		lineaW.append(Constants.TYPE).append(Constants.SEPARADOR)
				.append(Constants.BUSINESS_ID).append(Constants.SEPARADOR)
				.append(Constants.USER_ID).append(Constants.SEPARADOR)
				.append(Constants.STARS).append(Constants.SEPARADOR)
				.append(Constants.TEXT).append(Constants.SEPARADOR)
				.append(Constants.DATE).append(Constants.SEPARADOR)
				.append(Constants.FUNNY).append(Constants.SEPARADOR)
				.append(Constants.USEFUL).append(Constants.SEPARADOR)
				.append(Constants.COOL).append(Constants.SEPARADOR)
				.append(Constants.CRLF);
		fw.write(lineaW.toString());

		FileReader fr = new FileReader(new File(fileIn));
		BufferedReader bf = new BufferedReader(fr);
		JSONParser parser = new JSONParser();
		String linea = bf.readLine();
		while (linea != null) {
			JSONObject o = (JSONObject) parser.parse(linea);
			String type = (String) o.get(Constants.TYPE);
			String businessId = (String) o.get(Constants.BUSINESS_ID);
			String userId = (String) o.get(Constants.USER_ID);
			Long stars = o.get(Constants.STARS) == null ? 0L : (Long) o.get(Constants.STARS);
			String text = (String) o.get(Constants.TEXT);
			String date = (String) o.get(Constants.DATE);
			JSONObject jsonVotes = (JSONObject) o.get(Constants.VOTES);
			Long funny = jsonVotes.get(Constants.FUNNY) == null ? 0L : (Long) jsonVotes.get(Constants.FUNNY);
			Long useful = jsonVotes.get(Constants.USEFUL) == null ? 0L : (Long) jsonVotes.get(Constants.USEFUL);
			Long cool = jsonVotes.get(Constants.COOL) == null ? 0L : (Long) jsonVotes.get(Constants.COOL);

			lineaW = new StringBuilder();
			lineaW.append(type).append(Constants.SEPARADOR)
					.append(businessId).append(Constants.SEPARADOR)
					.append(userId).append(Constants.SEPARADOR)
					.append(stars).append(Constants.SEPARADOR)
					.append(text.replace("\n", "").replace("\r", "").replace(";", "")).append(Constants.SEPARADOR)
					.append(date).append(Constants.SEPARADOR)
					.append(funny).append(Constants.SEPARADOR)
					.append(useful).append(Constants.SEPARADOR)
					.append(cool).append(Constants.SEPARADOR)
					.append(Constants.CRLF);
			fw.write(lineaW.toString());
			linea = bf.readLine();
		}
		fw.close();
		bf.close();
		fr.close();
	}

	public static void main(String[] args) throws IOException, ParseException {
		String fileIn = "/Users/Pisco/Downloads/yelp_dataset_challenge_academic_dataset/yelp_academic_dataset_review.json";
		String fileOut = "/Users/Pisco/Downloads/yelp_dataset_challenge_academic_dataset/review.csv";
		convert(fileIn, fileOut);
		
		int file = 0;
		long counter = 0;
		long max = 200000;
		BufferedReader bf = new BufferedReader(new FileReader(new File(fileOut)));
		FileWriter fw = new FileWriter(new File("/Users/Pisco/Downloads/yelp_dataset_challenge_academic_dataset/review"+file+".csv"));
		String str = bf.readLine();
		while(str!=null){
			if(counter == max){
				counter = 0;
				file++;
				fw.close();
				fw = new FileWriter(new File("/Users/Pisco/Downloads/yelp_dataset_challenge_academic_dataset/review"+file+".csv"));
				StringBuilder lineaW = new StringBuilder();
				lineaW.append(Constants.TYPE).append(Constants.SEPARADOR)
						.append(Constants.BUSINESS_ID).append(Constants.SEPARADOR)
						.append(Constants.USER_ID).append(Constants.SEPARADOR)
						.append(Constants.STARS).append(Constants.SEPARADOR)
						.append(Constants.TEXT).append(Constants.SEPARADOR)
						.append(Constants.DATE).append(Constants.SEPARADOR)
						.append(Constants.FUNNY).append(Constants.SEPARADOR)
						.append(Constants.USEFUL).append(Constants.SEPARADOR)
						.append(Constants.COOL).append(Constants.SEPARADOR)
						.append(Constants.CRLF);
				fw.write(lineaW.toString());
			}
			fw.write(str+Constants.CRLF);
			counter++;
			str = bf.readLine();
		}
		fw.close();
		bf.close();
	}
}
