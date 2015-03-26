package converter.jsonConverter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class UserConverter {

	public static void convert(String fileIn, String fileOut)
			throws IOException, ParseException {
		// Print header
		FileWriter fw = new FileWriter(new File(fileOut));
		StringBuilder lineaW = new StringBuilder();
		lineaW.append(Constants.TYPE).append(Constants.SEPARADOR)
				.append(Constants.USER_ID).append(Constants.SEPARADOR)
				.append(Constants.NAME).append(Constants.SEPARADOR)
				.append(Constants.REVIEW_COUNT).append(Constants.SEPARADOR)
				.append(Constants.AVERAGE_STARS).append(Constants.SEPARADOR)
				.append(Constants.FUNNY).append(Constants.SEPARADOR)
				.append(Constants.USEFUL).append(Constants.SEPARADOR)
				.append(Constants.COOL).append(Constants.SEPARADOR)
				.append(Constants.FRIENDS).append(Constants.SEPARADOR)
				.append(Constants.ELITE).append(Constants.SEPARADOR)
				.append(Constants.YELPING_SINCE).append(Constants.SEPARADOR)
				.append(Constants.PROFILE).append(Constants.SEPARADOR)
				.append(Constants.CUTE).append(Constants.SEPARADOR)
				.append(Constants.FUNNY).append(Constants.SEPARADOR)
				.append(Constants.PLAIN).append(Constants.SEPARADOR)
				.append(Constants.WRITER).append(Constants.SEPARADOR)
				.append(Constants.LIST).append(Constants.SEPARADOR)
				.append(Constants.NOTE).append(Constants.SEPARADOR)
				.append(Constants.PHOTOS).append(Constants.SEPARADOR)
				.append(Constants.HOT).append(Constants.SEPARADOR)
				.append(Constants.COOL).append(Constants.SEPARADOR)
				.append(Constants.MORE).append(Constants.SEPARADOR)
				.append(Constants.FANS).append(Constants.SEPARADOR)
				.append(Constants.CRLF);
		fw.write(lineaW.toString());

		FileReader fr = new FileReader(new File(fileIn));
		BufferedReader bf = new BufferedReader(fr);
		JSONParser parser = new JSONParser();
		String linea = bf.readLine();
		while (linea != null) {
			JSONObject o = (JSONObject) parser.parse(linea);
			String type = (String) o.get(Constants.TYPE);
			String userId = (String) o.get(Constants.USER_ID);
			String name = (String) o.get(Constants.NAME);
			Long reviewCount = (Long) o.get(Constants.REVIEW_COUNT);
			Double averageStars = (Double) o.get(Constants.AVERAGE_STARS);
			JSONObject jsonVotes = (JSONObject) o.get(Constants.VOTES);
			Long funny = (Long) jsonVotes.get(Constants.FUNNY);
			Long useful = (Long) jsonVotes.get(Constants.USEFUL);
			Long cool = (Long) jsonVotes.get(Constants.COOL);
			JSONArray friends = (JSONArray) o.get(Constants.FRIENDS);// List
			JSONArray elite = (JSONArray) o.get(Constants.ELITE);// List
			String yelpingSince = (String) o.get(Constants.YELPING_SINCE);
			JSONObject jsonCompliments = (JSONObject) o
					.get(Constants.COMPLIMENTS);
			Long profile = jsonCompliments.get(Constants.PROFILE) == null ? 0L
					: (Long) jsonCompliments.get(Constants.PROFILE);
			Long cute = jsonCompliments.get(Constants.CUTE) == null ? 0L
					: (Long) jsonCompliments.get(Constants.CUTE);
			Long cFunny = jsonCompliments.get(Constants.FUNNY) == null ? 0L
					: (Long) jsonCompliments.get(Constants.FUNNY);
			Long plain = jsonCompliments.get(Constants.PLAIN) == null ? 0L
					: (Long) jsonCompliments.get(Constants.PLAIN);
			Long writer = jsonCompliments.get(Constants.WRITER) == null ? 0L
					: (Long) jsonCompliments.get(Constants.WRITER);
			Long list = jsonCompliments.get(Constants.LIST) == null ? 0L
					: (Long) jsonCompliments.get(Constants.LIST);
			Long note = jsonCompliments.get(Constants.NOTE) == null ? 0L
					: (Long) jsonCompliments.get(Constants.NOTE);
			Long photos = jsonCompliments.get(Constants.PHOTOS) == null ? 0L
					: (Long) jsonCompliments.get(Constants.PHOTOS);
			Long hot = jsonCompliments.get(Constants.HOT) == null ? 0L
					: (Long) jsonCompliments.get(Constants.HOT);
			Long cCool = jsonCompliments.get(Constants.COOL) == null ? 0L
					: (Long) jsonCompliments.get(Constants.COOL);
			Long more = jsonCompliments.get(Constants.MORE) == null ? 0L
					: (Long) jsonCompliments.get(Constants.MORE);
			Long fans = o.get(Constants.FANS) == null ? 0L : (Long) o
					.get(Constants.FANS);

			lineaW = new StringBuilder();
			lineaW.append(type)
					.append(Constants.SEPARADOR)
					.append(userId)
					.append(Constants.SEPARADOR)
					.append(name)
					.append(Constants.SEPARADOR)
					.append(reviewCount)
					.append(Constants.SEPARADOR)
					.append(averageStars)
					.append(Constants.SEPARADOR)
					.append(funny)
					.append(Constants.SEPARADOR)
					.append(useful)
					.append(Constants.SEPARADOR)
					.append(cool)
					.append(Constants.SEPARADOR)
					.append(friends.toString().replace("\r", "")
							.replace("\n", "")).append(Constants.SEPARADOR)
					.append(elite).append(Constants.SEPARADOR)
					.append(yelpingSince).append(Constants.SEPARADOR)
					.append(profile).append(Constants.SEPARADOR).append(cute)
					.append(Constants.SEPARADOR).append(cFunny)
					.append(Constants.SEPARADOR).append(plain)
					.append(Constants.SEPARADOR).append(writer)
					.append(Constants.SEPARADOR).append(list)
					.append(Constants.SEPARADOR).append(note)
					.append(Constants.SEPARADOR).append(photos)
					.append(Constants.SEPARADOR).append(hot)
					.append(Constants.SEPARADOR).append(cCool)
					.append(Constants.SEPARADOR).append(more)
					.append(Constants.SEPARADOR).append(fans)
					.append(Constants.SEPARADOR).append(Constants.CRLF);
			fw.write(lineaW.toString());
			linea = bf.readLine();
		}

		fw.close();
		bf.close();
		fr.close();
	}

	public static void main(String[] args) throws IOException, ParseException {
		String fileIn = "/Users/Pisco/Downloads/yelp_dataset_challenge_academic_dataset/yelp_academic_dataset_user.json";
		String fileOut = "/Users/Pisco/Downloads/yelp_dataset_challenge_academic_dataset/user.csv";
		convert(fileIn, fileOut);
	}
}