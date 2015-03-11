package uniandes.recomendadorPeliculas.business;

import java.sql.Connection;

import org.apache.commons.dbcp2.BasicDataSource;

import uniandes.recomendadorPeliculas.DAO.UserDAO;
import uniandes.recomendadorPeliculas.entities.User;
import uniandes.recomendadorPeliculas.utils.SqlUtils;

public class SignupBusiness {

	private final BasicDataSource dataSource;
	private final UserDAO userDAO;

	public SignupBusiness(BasicDataSource dataSource, UserDAO userDAO) {
		this.userDAO = userDAO;
		this.dataSource = dataSource;
	}

	public boolean signup(User user) {

		boolean userWasCreated = false;
		Connection dbConnection = null;
		try {
			dbConnection = dataSource.getConnection();
			dbConnection.setAutoCommit(false);
			int affectedRows = userDAO.createUser(dbConnection, user); 
			if(affectedRows == 1){
				dbConnection.commit();
				userWasCreated = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			SqlUtils.rollbackTransaction(dbConnection);
		} finally {
			SqlUtils.closeDbConnection(dbConnection);
		}
		return userWasCreated;
	}
}