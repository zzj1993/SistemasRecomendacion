package uniandes.recomendadorPeliculas.business;

import java.util.Hashtable;

import org.apache.commons.dbcp2.BasicDataSource;

import uniandes.recomendadorPeliculas.DAO.UserDAO;
import uniandes.recomendadorPeliculas.entities.Login;


public class LoginBusiness {
	
	private final BasicDataSource dataSource;
	private final UserDAO userDAO;
	private final Hashtable<String, Long> sessions;
	
	public LoginBusiness(BasicDataSource dataSource, UserDAO userDAO, Hashtable<String, Long>  sessions){
		this.userDAO = userDAO;
		this.dataSource = dataSource;
		this.sessions = sessions;
	}

	public boolean existeUsuario(Login login) {
		return true;
	}

	private boolean esPasswordValido(Login login) {
		return true;
	}

	public boolean esUsuarioValido(Login login) {
		return existeUsuario(login)
				&& esPasswordValido(login);
	}
}