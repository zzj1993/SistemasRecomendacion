package uniandes.recomendadorPeliculas.business;

import java.sql.Connection;
import java.util.Hashtable;

import org.apache.commons.dbcp2.BasicDataSource;

import uniandes.recomendadorPeliculas.DAO.UserDAO;
import uniandes.recomendadorPeliculas.entities.Signup;
import uniandes.recomendadorPeliculas.entities.User;
import uniandes.recomendadorPeliculas.utils.SqlUtils;

public class SignupBusiness {

	private final BasicDataSource dataSource;
	private final UserDAO userDAO;
	private final Hashtable<String, Long> sessions;

	public SignupBusiness(BasicDataSource dataSource, UserDAO userDAO, Hashtable<String, Long> sessions) {
		this.userDAO = userDAO;
		this.dataSource = dataSource;
		this.sessions = sessions;
	}

	public boolean signup(Signup signup) {

		boolean userWasCreated = false;
		Connection dbConnection = null;
		try {
			dbConnection = dataSource.getConnection();
			dbConnection.setAutoCommit(false);
			int affectedRows = userDAO.createUser(dbConnection, new User(signup.getEmail(), signup.getName(), signup.getPassword())); 
			if(affectedRows == 1){
				dbConnection.commit();
				userWasCreated = true;
				sessions.put(signup.getEmail(), System.currentTimeMillis());
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