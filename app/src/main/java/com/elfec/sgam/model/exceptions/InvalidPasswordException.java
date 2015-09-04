package com.elfec.sgam.model.exceptions;

/**
 * Excepción que se lanza cuando un usuario que intenta logearse proporcionó un
 * password incorrecto
 * 
 * @author drodriguez
 *
 */
public class InvalidPasswordException extends Exception {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = -1448313192669063322L;

	@Override
	public String getMessage() {
		return "El Usuario o password proporcionados no son válidos, " +
				"por favor revise los datos e inténtelo nuevamente";
	}
}
