package converter.jsonConverter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CheckinConverter {

	public static void convert(String fileIn, String fileOut)
			throws IOException, ParseException {
		FileReader fr = new FileReader(new File(fileIn));
		JSONParser parser = new JSONParser();
		JSONArray json = (JSONArray) parser.parse(fr);
		List<String> horaDia = new ArrayList<String>();
		for (int j = 0; j < 7; j++) {//day of week
			for (int i = 0; i < 24; i++) {//hour of day
				horaDia.add(i + "-" + j);
			}
		}

		//Print header
		FileWriter fw = new FileWriter(new File(fileOut));
		StringBuilder linea = new StringBuilder();
		linea.append(Constants.BUSINESS_ID).append(Constants.SEPARADOR).append(Constants.TYPE)
				.append(Constants.SEPARADOR);
		for (int i = 0; i < horaDia.size(); i++) {
			linea.append(horaDia.get(i)).append(Constants.SEPARADOR);
		}
		linea.append(Constants.CRLF);
		fw.write(linea.toString());

		//Print data
		for (Object o : json) {
			linea = new StringBuilder();
			JSONObject checkin = (JSONObject) o;
			String type = (String) checkin.get(Constants.TYPE);
			String business_id = (String) checkin.get(Constants.BUSINESS_ID);
			linea.append(business_id).append(Constants.SEPARADOR).append(type)
					.append(Constants.SEPARADOR);
			JSONObject checkin_info = (JSONObject) checkin.get(Constants.CHECKIN_INFO);
			checkin_info = (JSONObject) parser.parse(checkin_info.toString());
			for (int i = 0; i < horaDia.size(); i++) {
				Long value = (Long) checkin_info.get(horaDia.get(i));
				if (value == null)
					value = 0L;
				linea.append(value).append(Constants.SEPARADOR);
			}
			linea.append(Constants.CRLF);
			fw.write(linea.toString());
		}
		fr.close();
		fw.close();
	}

	public static void main(String[] args) throws IOException, ParseException {
		String fileIn = "/Users/Pisco/Downloads/yelp_dataset_challenge_academic_dataset/yelp_academic_dataset_checkin.json";
		String fileOut = "/Users/Pisco/Downloads/yelp_dataset_challenge_academic_dataset/checkin.csv";
		convert(fileIn, fileOut);
	}
}