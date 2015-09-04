package com.elfec.sgam.model.exceptions;

/**
 * Excepci�n que se lanza cuando un usuario que intenta logearse proporcion� un
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
		return "El Usuario o password proporcionados no son v�lidos, " +
				"por favor revise los datos e int�ntelo nuevamente";
	}
}
