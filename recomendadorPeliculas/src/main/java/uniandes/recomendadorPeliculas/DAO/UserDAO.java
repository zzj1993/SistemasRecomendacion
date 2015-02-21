package uniandes.recomendadorPeliculas.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import uniandes.recomendadorPeliculas.entities.User;

public class UserDAO {
	
	public int createUser(Connection dbConnection, User user) {
		int result = 0;
		if (user != null) {
			int createUserIntoUsersTableResult = createUserIntoUsersTable(dbConnection, user);
			if (createUserIntoUsersTableResult == 1) {
				result = 1;
			}
		}
		return result;
	}
	
	public User getUserByMail(Connection dbConnection, String email) {
		User retrievedUser = null;
		String getUserByMailSQL = "SELECT * FROM USERS WHERE email = ?;";
		try {
			PreparedStatement prepareStatement = dbConnection.prepareStatement(getUserByMailSQL);
			prepareStatement.setString(1, email);
			ResultSet resultSet = prepareStatement.executeQuery();
			email = resultSet.getString("email");
			String name = resultSet.getString("name");
			String password = resultSet.getString("password");
			retrievedUser = new User(email, name, password);
			resultSet.close();
			prepareStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retrievedUser;
	}

	private int createUsersTableTableIfDoesNotExist(Connection dbConnection) {
		int result = 0;
		String createUsersTableTableIfDoesNotExistSQL = "CREATE TABLE IF NOT EXISTS USERS ("
				+ "email VARCHAR(255) PRIMARY KEY, name VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL);";
		try {
			PreparedStatement prepareStatement = dbConnection.prepareStatement(createUsersTableTableIfDoesNotExistSQL);
			result = prepareStatement.executeUpdate();
			prepareStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private int createUserIntoUsersTable(Connection dbConnection, User user) {
		int affectedRows = 0;
		String createUserSQL = "INSERT INTO USERS (email, name, password) VALUES (? ,?, ?);";
		try {
			PreparedStatement prepareStatement = dbConnection.prepareStatement(createUserSQL);
			prepareStatement.setString(1, user.getEmail());
			prepareStatement.setString(2, user.getName());
			prepareStatement.setString(3, user.getPassword());
			affectedRows = prepareStatement.executeUpdate();
			prepareStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return affectedRows;
	}

	public int createUsersTablesIfNeeded(Connection dbConnection) {
		int result = 0;
		int createUsersTableTableIfDoesNotExistResult = createUsersTableTableIfDoesNotExist(dbConnection);

		if (createUsersTableTableIfDoesNotExistResult == 1) {
			result = 1;
		}

		return result;
	}
}
