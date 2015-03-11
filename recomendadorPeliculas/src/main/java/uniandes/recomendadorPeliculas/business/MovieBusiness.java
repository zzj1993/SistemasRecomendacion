package uniandes.recomendadorPeliculas.business;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.mahout.cf.taste.common.TasteException;

import uniandes.recomendadorPeliculas.DAO.MovieDAO;
import uniandes.recomendadorPeliculas.DAO.RatingDAO;
import uniandes.recomendadorPeliculas.config.DataConfig;
import uniandes.recomendadorPeliculas.entities.MovieRating;
import uniandes.recomendadorPeliculas.entities.Rating;
import uniandes.recomendadorPeliculas.utils.SqlUtils;

public class MovieBusiness {
	
	private final BasicDataSource dataSource;
	private final MovieDAO movieDAO;
	private final RatingDAO ratingDAO;
	private final DataConfig dataConfig;
	private final Recommenders recommenders;
	
	public MovieBusiness(BasicDataSource dataSource, MovieDAO movieDAO, RatingDAO ratingDAO, DataConfig dataConfig, Recommenders recommenders) {
		this.dataSource = dataSource;
		this.movieDAO = movieDAO;
		this.ratingDAO = ratingDAO;
		this.dataConfig = dataConfig;
		this.recommenders = recommenders;
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
			addRatingToFile(rating);
			recommenders.reBuildModel(dataConfig);
		} catch (SQLException e) {
			e.printStackTrace();
			response = false;
			SqlUtils.rollbackTransaction(dbConnection);
		}
		return response;
	}
	
	private void addRatingToFile(Rating rating) {
		String dir = dataConfig.getDir();
		String file = dataConfig.getRatings();
		try {
			Writer w = new BufferedWriter(new FileWriter(dir+file, true));
			w.append("\r\n"+rating.getUser()+"::"+rating.getItem()+"::"+rating.getRating()+"::"+System.currentTimeMillis());
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<MovieRating> getAllUserMovies(Long userid){
		Connection dbConnection = null;
		try {
			dbConnection = dataSource.getConnection();
			List<MovieRating> m = movieDAO.getAllUserMovies(dbConnection, userid);
			SqlUtils.closeDbConnection(dbConnection);
			return m;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<MovieRating> getAllUserRatedMovies(Long userid){
		Connection dbConnection = null;
		try {
			dbConnection = dataSource.getConnection();
			List<MovieRating> m = movieDAO.getAllUserMovies(dbConnection, userid);
			SqlUtils.closeDbConnection(dbConnection);
			return m;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<MovieRating> getAllRatedMoviesWithRating(Long userid, int modelType, int neighborhoodSize,
			int nRecommendations, int type) {
		List<MovieRating> m = getAllUserRatedMovies(userid);
		for (int i = 0; i < m.size(); i++) {
			MovieRating r = m.get(i);
			try {
				r.setPredictedRating(recommenders
						.estimatePreference(userid, r.getId(), modelType,
								neighborhoodSize, nRecommendations, type));
				m.set(i, r);
			} catch (TasteException e) {
				e.printStackTrace();
			}
		}
		return m;
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
