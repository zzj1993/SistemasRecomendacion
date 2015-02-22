package uniandes.recomendadorPeliculas.DAO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import uniandes.recomendadorPeliculas.config.DataConfig;
import uniandes.recomendadorPeliculas.entities.Movie;
import uniandes.recomendadorPeliculas.entities.MovieRating;

public class MovieDAO {

	public List<MovieRating> getAll(Connection dbConnection) {
		List<MovieRating> retrievedMovies = new ArrayList<MovieRating>();
		String sql = "SELECT * FROM MOVIE m, AVERAGERATINGS a WHERE m.id = a.itemid;";
		try {
			PreparedStatement prepareStatement = dbConnection.prepareStatement(sql);
			ResultSet resultSet = prepareStatement.executeQuery();	
			while(resultSet.next()){
				Long id = resultSet.getLong("id");
				String title = resultSet.getString("title");
				String genres = resultSet.getString("GENRES");
				Long sum = resultSet.getLong("SUM");
				Long count = resultSet.getLong("COUNT");
				retrievedMovies.add(new MovieRating(id, title, genres, (int) (sum/count)));				
			}
			resultSet.close();
			prepareStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retrievedMovies;
	}
	
	public int getMovieRating(Connection dbConnection, Movie movie) {
		String sql = "SELECT SUM(RATTING) AS SUM, COUNT(RATTING) AS COUNT FROM RATTING  WHERE ITEMID = ? GROUP BY ITEMID;";
		int result = 1;
		try {
			PreparedStatement prepareStatement = dbConnection.prepareStatement(sql);
			prepareStatement.setLong(1, movie.getId());
			ResultSet resultSet = prepareStatement.executeQuery();
			if(resultSet.next()){
				Long sum = resultSet.getLong("SUM");
				Long count = resultSet.getLong("COUNT");
				result = (int) (sum/count);
			}
			resultSet.close();
			prepareStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	

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
		String createUserSQL = "INSERT INTO MOVIE (id, title, genres) VALUES (? ,?, ?);";
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
		String file = dataConfig.getMovies();
		BufferedReader bf = new BufferedReader(new FileReader(dir+file));
		String str = bf.readLine();
		while(str != null){
			String[] split = str.split("::");
			Long id = Long.parseLong(split[0]);
			String title = split[1];
			String genres = split.length == 3 ? split[2] : "";
			createMoviesIntoTable(dbConnection, new Movie(id, title, genres));
			str = bf.readLine();
		}
		bf.close();
	}
}
