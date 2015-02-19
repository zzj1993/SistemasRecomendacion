package uniandes.recomendadorPeliculas.business;

import uniandes.recomendadorPeliculas.entities.UserInformation;

public class LoginBusiness {

	private boolean existeUsuario(UserInformation userInformation) {
		return true;
	}

	private boolean esPasswordValido(UserInformation userInformation) {
		return true;
	}

	public boolean esUsuarioValido(UserInformation userInformation) {
		return existeUsuario(userInformation)
				&& esPasswordValido(userInformation);
	}
}