package com.elfec.sgam.model.exceptions;

/**
 * Se lanza cuando una clase que requeria inicialización se utiliza en su estado
 * actual
 * 
 * @author drodriguez
 *
 */
public class InitializationException extends RuntimeException {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = -5084877272342523516L;
	private String className;

    /**
     * Inicializa la excepción con el nombre de la clase que no fue inicializada
     * @param mClass clase que no fue inicializada correctamente
     */
    public InitializationException(Class<?> mClass) {
        this.className = mClass.getName();
    }

    @Override
	public String getMessage() {
		return "La clase "+className+"no se inicializó correctamente antes de utilizarse";
	}

}
