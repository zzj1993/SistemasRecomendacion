package uniandes.recomendadorPeliculas.DAO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import uniandes.recomendadorPeliculas.config.DataConfig;
import uniandes.recomendadorPeliculas.entities.Rating;
import uniandes.recomendadorPeliculas.entities.User;

public class RatingDAO {
	


//CREATE TABLE AVERAGERATING (ITEMID BIGINT, SUM BIGINT, COUNT BIGINT);
//INSERT INTO AVERAGERATING (ITEMID, SUM, COUNT) 
//	SELECT ITEMID, SUM(RATING) AS SUM, COUNT(RATING) AS COUNT FROM RATING GROUP BY ITEMID
	

	private int createTableIfDoesNotExist(Connection dbConnection) {
		int result = 0;
		String createTableIfDoesNotExistSQL = "CREATE TABLE IF NOT EXISTS RATING ("
				+ "userId BIGINT NOT NULL, itemId BIGINT NOT NULL, RATING INTEGER NOT NULL);"
				+ "CREATE UNIQUE INDEX IDXUNIQUE ON RATING(userId, itemId);";
		try {
			PreparedStatement prepareStatement = dbConnection.prepareStatement(createTableIfDoesNotExistSQL);
			result = prepareStatement.executeUpdate();
			prepareStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int createRatingsIntoTable(Connection dbConnection, Rating rating) {
		int affectedRows = 0;
		String createUserSQL = "INSERT INTO RATING (userId, itemId, RATING) VALUES (? ,?, ?);";
		try {
			PreparedStatement prepareStatement = dbConnection.prepareStatement(createUserSQL);
			prepareStatement.setLong(1, rating.getUser());
			prepareStatement.setLong(2, rating.getItem());
			prepareStatement.setInt(3, rating.getRating());
			affectedRows = prepareStatement.executeUpdate();
			prepareStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return affectedRows;
	}
	
	public int updateRatingCount(Connection dbConnection, Rating rating) {
		int affectedRows = 0;
		String createUserSQL = "UPDATE AVERAGERATING SET SUM = SUM + ?, COUNT = COUNT + 1 WHERE ITEMID = ?;";
		try {
			PreparedStatement prepareStatement = dbConnection.prepareStatement(createUserSQL);
			prepareStatement.setInt(1, rating.getRating());
			prepareStatement.setLong(2, rating.getItem());
			affectedRows = prepareStatement.executeUpdate();
			prepareStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return affectedRows;
	}

	public int createTablesIfNeeded(Connection dbConnection, DataConfig dataConfig) {
		int result = 0;
		createTableIfDoesNotExist(dbConnection);
		try {
			loadData(dbConnection, dataConfig);				
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	private void loadData(Connection dbConnection, DataConfig dataConfig) throws IOException{
		String dir = dataConfig.getDir();
		String file = dataConfig.getRatings();
		BufferedReader bf = new BufferedReader(new FileReader(dir+file));
		String str = bf.readLine();

		while(str != null){
			String[] split = str.split("::");
			Long user = Long.parseLong(split[0]);
			Long item = Long.parseLong(split[1]);
			Integer rating = Integer.parseInt(split[2]);
			UserDAO userDAO = new UserDAO();
			User u = new User(user, user);
			userDAO.createUser(dbConnection, u);
			createRatingsIntoTable(dbConnection, new Rating(user, item, rating));
			str = bf.readLine();
		}
		bf.close();
	}
}
