package uniandes.recomendadorPeliculas.business;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;

import uniandes.recomendadorPeliculas.DAO.MovieDAO;
import uniandes.recomendadorPeliculas.entities.MovieRating;
import uniandes.recomendadorPeliculas.utils.SqlUtils;

public class MovieBusiness {
	
	private final BasicDataSource dataSource;
	private final MovieDAO movieDAO;
	
	public MovieBusiness(BasicDataSource dataSource, MovieDAO movieDAO) {
		this.dataSource = dataSource;
		this.movieDAO = movieDAO;
	}

	public List<MovieRating> getAllMovies(){
		Connection dbConnection = null;
		try {
			dbConnection = dataSource.getConnection();
			return movieDAO.getAll(dbConnection);
		} catch (SQLException e) {
			e.printStackTrace();
			SqlUtils.rollbackTransaction(dbConnection);
		}
		return null;
	}
}
