package uniandes.recomendadorPeliculas.DAO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import uniandes.recomendadorPeliculas.config.DataConfig;
import uniandes.recomendadorPeliculas.entities.Movie;

public class MovieDAO {

//	public User getUserByMail(Connection dbConnection, String email) {
//		User retrievedUser = null;
//		String getUserByMailSQL = "SELECT * FROM USERS WHERE email = ?;";
//		try {
//			PreparedStatement prepareStatement = dbConnection.prepareStatement(getUserByMailSQL);
//			prepareStatement.setString(1, email);
//			ResultSet resultSet = prepareStatement.executeQuery();
//			email = resultSet.getString("email");
//			String name = resultSet.getString("name");
//			String password = resultSet.getString("password");
//			retrievedUser = new User(email, name, password);
//			resultSet.close();
//			prepareStatement.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return retrievedUser;
//	}

	private int createTableIfDoesNotExist(Connection dbConnection) {
		int result = 0;
		String createTableIfDoesNotExistSQL = "CREATE TABLE IF NOT EXISTS MOVIE ("
				+ "id BIGINT PRIMARY KEY, title VARCHAR(255) NOT NULL, GENRES VARCHAR(255) NOT NULL);";
		try {
			PreparedStatement prepareStatement = dbConnection.prepareStatement(createTableIfDoesNotExistSQL);
			result = prepareStatement.executeUpdate();
			prepareStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private int createMoviesIntoTable(Connection dbConnection, Movie movie) {
		int affectedRows = 0;
		String createUserSQL = "INSERT INTO USERS (id, title, genres) VALUES (? ,?, ?);";
		try {
			PreparedStatement prepareStatement = dbConnection.prepareStatement(createUserSQL);
			prepareStatement.setLong(1, movie.getId());
			prepareStatement.setString(2, movie.getTitle());
			prepareStatement.setString(3, movie.getGenres());
			affectedRows = prepareStatement.executeUpdate();
			prepareStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return affectedRows;
	}

	public int createTablesIfNeeded(Connection dbConnection, DataConfig dataConfig) {
		int result = 0;
		int createUsersTableTableIfDoesNotExistResult = createTableIfDoesNotExist(dbConnection);
		if (createUsersTableTableIfDoesNotExistResult == 1) {
			try {
				loadData(dbConnection, dataConfig);
			} catch (IOException e) {
				e.printStackTrace();
			}
			result = 1;
		}
		return result;
	}
	
	private void loadData(Connection dbConnection, DataConfig dataConfig) throws IOException{
		String dir = dataConfig.getDir();
		String file = dataConfig.getMovies();
		BufferedReader bf = new BufferedReader(new FileReader(dir+file));
		String str = bf.readLine();
		while(str != null){
			String[] split = str.split("::");
			Long id = Long.parseLong(split[0]);
			String title = split[1];
			String genres = split[2];
			createMoviesIntoTable(dbConnection, new Movie(id, title, genres));
			str = bf.readLine();
		}
		bf.close();
	}
}
