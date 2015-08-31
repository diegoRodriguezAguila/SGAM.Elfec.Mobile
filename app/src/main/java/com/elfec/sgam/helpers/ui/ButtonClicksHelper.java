package com.elfec.sgam.helpers.ui;

import android.os.SystemClock;

/**
 * Helper para el control de múltiples clicks seguidos a un mismo botón
 * 
 * @author drodriguez
 *
 */
public class ButtonClicksHelper {
	/**
	 * Tiempo mínimo entre clicks a un boton
	 */
	private static final int TIME_BETWEEN_CLICKS = 650;
	/**
	 * último click realizado
	 */
	private static long lastClickTime = 0;

	/**
	 * Verifica si pasó el mínimo tiempo requerido entre clicks
	 * {@link #TIME_BETWEEN_CLICKS} para realizar un click
	 * 
	 * @return true si es que se permite hacer un click
	 */
	public static boolean canClickButton() {
		if (SystemClock.elapsedRealtime() - lastClickTime > TIME_BETWEEN_CLICKS) {
			lastClickTime = SystemClock.elapsedRealtime();
			return true;
		}
		return false;
	}
}
