package converter.jsonConverter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TipConverter {

	public static void convert(String fileIn, String fileOut)
			throws IOException, ParseException {
		// Print header
		FileWriter fw = new FileWriter(new File(fileOut));
		StringBuilder lineaW = new StringBuilder();
		lineaW.append(Constants.TYPE).append(Constants.SEPARADOR)
				.append(Constants.TEXT).append(Constants.SEPARADOR)
				.append(Constants.BUSINESS_ID).append(Constants.SEPARADOR)
				.append(Constants.USER_ID).append(Constants.SEPARADOR)
				.append(Constants.DATE).append(Constants.SEPARADOR)
				.append(Constants.LIKES).append(Constants.SEPARADOR)
				.append(Constants.CRLF);
		fw.write(lineaW.toString());

		FileReader fr = new FileReader(new File(fileIn));
		BufferedReader bf = new BufferedReader(fr);
		JSONParser parser = new JSONParser();
		String linea = bf.readLine();
		while (linea != null) {
			lineaW = new StringBuilder();
			JSONObject o = (JSONObject) parser.parse(linea);
			String type = (String) o.get(Constants.TYPE);
			String text = (String) o.get(Constants.TEXT);
			String businessId = (String) o.get(Constants.BUSINESS_ID);
			String userId = (String) o.get(Constants.USER_ID);
			String date = (String) o.get(Constants.DATE);
			Long likes = (Long) o.get(Constants.LIKES);

			lineaW.append(type).append(Constants.SEPARADOR)
					.append(text.trim().replace("\n", "").replace(";", ""))
					.append(Constants.SEPARADOR).append(businessId)
					.append(Constants.SEPARADOR).append(userId)
					.append(Constants.SEPARADOR).append(date)
					.append(Constants.SEPARADOR).append(likes)
					.append(Constants.SEPARADOR).append(Constants.CRLF);
			fw.write(lineaW.toString());
			linea = bf.readLine();
		}
		fw.close();
		bf.close();
		fr.close();
	}

	public static void main(String[] args) throws IOException, ParseException {
		String fileIn = "/Users/Pisco/Downloads/yelp_dataset_challenge_academic_dataset/yelp_academic_dataset_tip.json";
		String fileOut = "/Users/Pisco/Downloads/yelp_dataset_challenge_academic_dataset/tip.csv";
		convert(fileIn, fileOut);
	}
}