package uniandes.recomendadorPeliculas.business;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;

import uniandes.recomendadorPeliculas.DAO.MovieDAO;
import uniandes.recomendadorPeliculas.DAO.RatingDAO;
import uniandes.recomendadorPeliculas.entities.MovieRating;
import uniandes.recomendadorPeliculas.entities.Rating;
import uniandes.recomendadorPeliculas.utils.SqlUtils;

public class MovieBusiness {
	
	private final BasicDataSource dataSource;
	private final MovieDAO movieDAO;
	private final RatingDAO ratingDAO;
	
	public MovieBusiness(BasicDataSource dataSource, MovieDAO movieDAO, RatingDAO ratingDAO) {
		this.dataSource = dataSource;
		this.movieDAO = movieDAO;
		this.ratingDAO = ratingDAO;
	}

	public List<MovieRating> getAllMovies(){
		Connection dbConnection = null;
		try {
			dbConnection = dataSource.getConnection();
			List<MovieRating> m = movieDAO.getAll(dbConnection);
			SqlUtils.closeDbConnection(dbConnection);
			return m;
		} catch (SQLException e) {
			e.printStackTrace();
			SqlUtils.rollbackTransaction(dbConnection);
		}
		return null;
	}
	
	public boolean createNewRating(Rating rating){
		Connection dbConnection = null;
		boolean response = true;
		try {
			dbConnection = dataSource.getConnection();
			ratingDAO.createRatingsIntoTable(dbConnection, rating);
			ratingDAO.updateRatingCount(dbConnection, rating);
			SqlUtils.closeDbConnection(dbConnection);
		} catch (SQLException e) {
			e.printStackTrace();
			response = false;
			SqlUtils.rollbackTransaction(dbConnection);
		}
		return response;
	}
	
	public List<MovieRating> getAllUserMovies(String userid){
		Connection dbConnection = null;
		try {
			dbConnection = dataSource.getConnection();
			List<MovieRating> m= movieDAO.getAllUserMovies(dbConnection, userid);
			SqlUtils.closeDbConnection(dbConnection);
			return m;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public MovieRating getMovieRating(Long itemid){
		Connection dbConnection = null;
		try {
			dbConnection = dataSource.getConnection();
			MovieRating m = movieDAO.getMovieRating(dbConnection, itemid);
			SqlUtils.closeDbConnection(dbConnection);
			return m;
		} catch (SQLException e) {
			SqlUtils.closeDbConnection(dbConnection);
			e.printStackTrace();
		}
		return null;
	}
}
