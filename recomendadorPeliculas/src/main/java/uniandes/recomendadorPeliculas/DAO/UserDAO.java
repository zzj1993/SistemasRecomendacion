package uniandes.recomendadorPeliculas.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import uniandes.recomendadorPeliculas.entities.User;

public class UserDAO {
	
	public final static String ID = "ID";
	public final static String USER_ID = "USER_ID";
	
	public int createUser(Connection dbConnection, User user) {
		int result = 0;
		if(getUserByUserId(dbConnection, user.getUserId())==null){			
			if (user != null) {
				int createUserIntoUsersTableResult = createUserIntoUsersTable(dbConnection, user);
				if (createUserIntoUsersTableResult == 1) {
					result = 1;
				}
			}
		}
		return result;
	}
	
	public User getUserByUserId(Connection dbConnection, Long userId) {
		User retrievedUser = null;
		String getUserByMailSQL = "SELECT * FROM MOVIE_USER WHERE USER_ID = ?;";
		try {
			PreparedStatement prepareStatement = dbConnection.prepareStatement(getUserByMailSQL);
			prepareStatement.setLong(1, userId);
			ResultSet resultSet = prepareStatement.executeQuery();
			if(resultSet.next()){
				Long id = resultSet.getLong(ID);
				userId = resultSet.getLong(USER_ID);
				retrievedUser = new User(id, userId);				
			}
			resultSet.close();
			prepareStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retrievedUser;
	}

	private int createUsersTableTableIfDoesNotExist(Connection dbConnection) {
		int result = 0;
		String createUsersTableTableIfDoesNotExistSQL = "CREATE TABLE IF NOT EXISTS MOVIE_USER ("
				+ "ID BIGINT auto_increment PRIMARY KEY, USER_ID BIGINT NOT NULL);";
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
		String createUserSQL = "INSERT INTO MOVIE_USER (USER_ID) VALUES (?);";
		try {
			PreparedStatement prepareStatement = dbConnection.prepareStatement(createUserSQL);
			prepareStatement.setLong(1, user.getUserId());
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
