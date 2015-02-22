package uniandes.recomendadorPeliculas.DAO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import uniandes.recomendadorPeliculas.config.DataConfig;
import uniandes.recomendadorPeliculas.entities.Ratting;
import uniandes.recomendadorPeliculas.entities.User;

public class RattingsDAO {
	//	public User getUserByMail(Connection dbConnection, String email) {
	//	User retrievedUser = null;
	//	String getUserByMailSQL = "SELECT * FROM USERS WHERE email = ?;";
	//	try {
	//		PreparedStatement prepareStatement = dbConnection.prepareStatement(getUserByMailSQL);
	//		prepareStatement.setString(1, email);
	//		ResultSet resultSet = prepareStatement.executeQuery();
	//		email = resultSet.getString("email");
	//		String name = resultSet.getString("name");
	//		String password = resultSet.getString("password");
	//		retrievedUser = new User(email, name, password);
	//		resultSet.close();
	//		prepareStatement.close();
	//	} catch (Exception e) {
	//		e.printStackTrace();
	//	}
	//	return retrievedUser;
	//}

	private int createTableIfDoesNotExist(Connection dbConnection) {
		int result = 0;
		String createTableIfDoesNotExistSQL = "CREATE TABLE IF NOT EXISTS RATTING ("
				+ "userId VARCHAR(255) NOT NULL, itemId BIGINT NOT NULL, RATTING INTEGER NOT NULL);";
		try {
			PreparedStatement prepareStatement = dbConnection.prepareStatement(createTableIfDoesNotExistSQL);
			result = prepareStatement.executeUpdate();
			prepareStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private int createRattingsIntoTable(Connection dbConnection, Ratting ratting) {
		int affectedRows = 0;
		String createUserSQL = "INSERT INTO RATTING (userId, itemId, RATTING) VALUES (? ,?, ?);";
		try {
			PreparedStatement prepareStatement = dbConnection.prepareStatement(createUserSQL);
			prepareStatement.setString(1, ratting.getUser());
			prepareStatement.setLong(2, ratting.getItem());
			prepareStatement.setInt(3, ratting.getRatting());
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
		String file = dataConfig.getRattings();
		BufferedReader bf = new BufferedReader(new FileReader(dir+file));
		String str = bf.readLine();

		while(str != null){
			String[] split = str.split("::");
			String user = split[0];
			Long item = Long.parseLong(split[1]);
			Integer ratting = Integer.parseInt(split[2]);
			UserDAO userDAO = new UserDAO();
			User u = new User(user, user, user);
			userDAO.createUser(dbConnection, u);
			createRattingsIntoTable(dbConnection, new Ratting(user, item, ratting));
			str = bf.readLine();
		}
		bf.close();
	}
}
