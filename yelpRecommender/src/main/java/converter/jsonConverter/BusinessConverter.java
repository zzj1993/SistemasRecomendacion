package converter.jsonConverter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class BusinessConverter {

	public static void convert(String fileIn, String fileOut)
			throws IOException, ParseException {
		String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday",
				"Friday", "Saturday", "Sunday" };
		JSONParser parser = new JSONParser();
		HashSet<String> attributesSet = new HashSet<String>();
		HashSet<String> neighborhoodsSet = new HashSet<String>();
		HashSet<String> categoriesSet = new HashSet<String>();

		FileReader fr = new FileReader(new File(fileIn));
		BufferedReader bf = new BufferedReader(fr);
		String linea = bf.readLine();
		while (linea != null) {
			JSONObject o = (JSONObject) parser.parse(linea);
			JSONArray neighborhoods = (JSONArray) o
					.get(Constants.NEIGHBORHOODS);
			for (int i = 0; i < neighborhoods.size(); i++) {
				String neighborhood = (String) neighborhoods.get(i);
				if (!neighborhoodsSet.contains(neighborhood)) {
					neighborhoodsSet.add(neighborhood);
				}
			}

			JSONArray categories = (JSONArray) o.get(Constants.CATEGORIES);
			for (int i = 0; i < categories.size(); i++) {
				String category = (String) categories.get(i);
				if (!categoriesSet.contains(category)) {
					categoriesSet.add(category);
				}
			}

			JSONObject jsonAttributes = (JSONObject) o
					.get(Constants.ATTRIBUTES);
			Iterator it = jsonAttributes.keySet().iterator();
			while (it.hasNext()) {
				String k = (String) it.next();
				if (jsonAttributes.get(k) instanceof JSONObject) {
					JSONObject nested = (JSONObject) jsonAttributes.get(k);
					Iterator it2 = nested.keySet().iterator();
					while (it2.hasNext()) {
						String nestedK = (String) it2.next();
						if (!attributesSet.contains(k + "#" + nestedK)) {
							attributesSet.add(k + "#" + nestedK);
						}
					}
				} else {
					if (!attributesSet.contains(k)) {
						attributesSet.add(k);
					}
				}
			}
			linea = bf.readLine();
		}
		bf.close();
		fr.close();

		// Print header
		FileWriter fw = new FileWriter(new File(fileOut));
		StringBuilder lineaW = new StringBuilder();
		lineaW.append(Constants.TYPE).append(Constants.SEPARADOR)
				.append(Constants.BUSINESS_ID).append(Constants.SEPARADOR)
				.append(Constants.NAME).append(Constants.SEPARADOR);

		Object[] neighborhoods = neighborhoodsSet.toArray();
		Arrays.sort(neighborhoods);
		for (int i = 0; i < neighborhoods.length; i++) {
			lineaW.append(neighborhoods[i]).append(Constants.SEPARADOR);
		}

		lineaW.append(Constants.FULL_ADDRESS).append(Constants.SEPARADOR)
				.append(Constants.CITY).append(Constants.SEPARADOR)
				.append(Constants.STATE).append(Constants.SEPARADOR)
				.append(Constants.LATITUDE).append(Constants.SEPARADOR)
				.append(Constants.LONGITUDE).append(Constants.SEPARADOR)
				.append(Constants.STARS).append(Constants.SEPARADOR)
				.append(Constants.REVIEW_COUNT).append(Constants.SEPARADOR);

		Object[] categories = categoriesSet.toArray();
		Arrays.sort(categories);
		for (int i = 0; i < categories.length; i++) {
			lineaW.append(categories[i]).append(Constants.SEPARADOR);
		}

		lineaW.append(Constants.OPEN).append(Constants.SEPARADOR);
		for (int i = 0; i < days.length; i++) {
			lineaW.append(days[i]).append("-").append(Constants.OPEN)
					.append(Constants.SEPARADOR);
			lineaW.append(days[i]).append("-").append(Constants.CLOSE)
					.append(Constants.SEPARADOR);
		}
		Object[] attributes = attributesSet.toArray();
		Arrays.sort(attributes);
		for (int i = 0; i < attributes.length; i++) {
			lineaW.append(attributes[i].toString()).append(Constants.SEPARADOR);
		}
		lineaW.append(Constants.CRLF);
		fw.write(lineaW.toString());

		fr = new FileReader(new File(fileIn));
		bf = new BufferedReader(fr);
		linea = bf.readLine();
		while (linea != null) {
			JSONObject o = (JSONObject) parser.parse(linea);
			String type = (String) o.get(Constants.TYPE);
			String business_id = (String) o.get(Constants.BUSINESS_ID);
			String name = (String) o.get(Constants.NAME);

			HashMap<String, String> neighborhoodsValues = new HashMap<String, String>();
			JSONArray jsonNeighborhoods = (JSONArray) o
					.get(Constants.NEIGHBORHOODS);
			for (int i = 0; i < jsonNeighborhoods.size(); i++) {
				String k = (String) jsonNeighborhoods.get(i);
				if (!neighborhoodsValues.containsKey(k)) {
					neighborhoodsValues.put(k, "TRUE");
				}
			}

			String fullAddress = (String) o.get(Constants.FULL_ADDRESS);
			String city = (String) o.get(Constants.CITY);
			String state = (String) o.get(Constants.STATE);
			Double latitude = o.get(Constants.LATITUDE) == null ? 0D
					: (Double) o.get(Constants.LATITUDE);
			Double longitude = o.get(Constants.LONGITUDE) == null ? 0D
					: (Double) o.get(Constants.LONGITUDE);
			Double stars = o.get(Constants.STARS) == null ? 0D : (Double) o
					.get(Constants.STARS);
			Long reviewCount = o.get(Constants.REVIEW_COUNT) == null ? 0L
					: (Long) o.get(Constants.REVIEW_COUNT);

			HashMap<String, String> categoriesValues = new HashMap<String, String>();
			JSONArray jsonCategories = (JSONArray) o.get(Constants.CATEGORIES);
			for (int i = 0; i < jsonCategories.size(); i++) {
				String k = (String) jsonCategories.get(i);
				if (!categoriesValues.containsKey(k)) {
					categoriesValues.put(k, "TRUE");
				}
			}

			Boolean open = (Boolean) o.get(Constants.OPEN);
			JSONObject jsonHours = (JSONObject) o.get(Constants.HOURS);
			String[] daysValues = new String[days.length];
			for (int i = 0; i < days.length; i++) {
				String day = days[i];
				JSONObject jsonDay = (JSONObject) jsonHours.get(day);
				if (jsonDay != null) {
					String open1 = jsonDay.get(Constants.OPEN) == null ? " "
							: (String) jsonDay.get(Constants.OPEN);
					String close = jsonDay.get(Constants.CLOSE) == null ? " "
							: (String) jsonDay.get(Constants.CLOSE);
					daysValues[i] = open1 + "-" + close;
				}
			}
			JSONObject jsonAttributes = (JSONObject) o
					.get(Constants.ATTRIBUTES);
			HashMap<String, String> attributeValues = new HashMap<String, String>();
			Iterator it = jsonAttributes.keySet().iterator();
			while (it.hasNext()) {
				String k = (String) it.next();
				if (jsonAttributes.get(k) instanceof JSONObject) {
					JSONObject nested = (JSONObject) jsonAttributes.get(k);
					Iterator it2 = nested.keySet().iterator();
					while (it2.hasNext()) {
						String nestedK = (String) it2.next();
						if (!attributeValues.containsKey(k + "#" + nestedK)) {
							attributeValues.put(k + "#" + nestedK,
									nested.get(nestedK).toString());
						}
					}
				} else {
					if (!attributeValues.containsKey(k)) {
						attributeValues
								.put(k, jsonAttributes.get(k).toString());
					}
				}
			}

			lineaW = new StringBuilder();
			lineaW.append(type).append(Constants.SEPARADOR).append(business_id)
					.append(Constants.SEPARADOR).append(name)
					.append(Constants.SEPARADOR);

			for (int i = 0; i < neighborhoods.length; i++) {
				String key = (String) neighborhoods[i];
				if (neighborhoodsValues.containsKey(key)) {
					lineaW.append("1").append(Constants.SEPARADOR);
				} else {
					lineaW.append("0").append(Constants.SEPARADOR);
				}
			}

			lineaW.append(
					fullAddress.replace("\n", "").replace("\r", "")
							.replace(";", "")).append(Constants.SEPARADOR)
					.append(city).append(Constants.SEPARADOR).append(state)
					.append(Constants.SEPARADOR).append(latitude)
					.append(Constants.SEPARADOR).append(longitude)
					.append(Constants.SEPARADOR).append(stars)
					.append(Constants.SEPARADOR).append(reviewCount)
					.append(Constants.SEPARADOR);

			for (int i = 0; i < categories.length; i++) {
				String key = (String) categories[i];
				if (categoriesValues.containsKey(key)) {
					lineaW.append("1").append(Constants.SEPARADOR);
				} else {
					lineaW.append("0").append(Constants.SEPARADOR);
				}
			}

			if (open.equals(Boolean.TRUE)) {
				lineaW.append("1").append(Constants.SEPARADOR);
			} else {
				lineaW.append("0").append(Constants.SEPARADOR);
			}

			for (int i = 0; i < days.length; i++) {
				if (daysValues[i] != null) {
					String[] hours = daysValues[i].split("-");
					lineaW.append(hours[0].trim()).append(Constants.SEPARADOR);
					lineaW.append(hours[1].trim()).append(Constants.SEPARADOR);
				} else {
					lineaW.append("").append(Constants.SEPARADOR).append("")
							.append(Constants.SEPARADOR);
				}
			}
			for (int i = 0; i < attributes.length; i++) {
				String value = attributeValues.get(attributes[i]);
				if (value != null) {
					lineaW.append(value).append(Constants.SEPARADOR);
				} else {
					lineaW.append("").append(Constants.SEPARADOR);
				}
			}
			lineaW.append(Constants.CRLF);
			fw.write(lineaW.toString());
			linea = bf.readLine();
		}
		bf.close();
		fw.close();
		fr.close();
	}

	public static void main(String[] args) throws IOException, ParseException {
		String fileIn = "/Users/Pisco/Downloads/yelp_dataset_challenge_academic_dataset/yelp_academic_dataset_business.json";
		String fileOut = "/Users/Pisco/Downloads/yelp_dataset_challenge_academic_dataset/business.csv";
		convert(fileIn, fileOut);
	}
}
