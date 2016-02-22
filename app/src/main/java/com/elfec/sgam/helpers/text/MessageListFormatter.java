package com.elfec.sgam.helpers.text;

import android.text.Html;
import android.text.Spanned;

/**
 * Formatea las listas de objetos en una lista html
 * 
 * @author drodriguez
 *
 */
public class MessageListFormatter {

	/**
	 * Formatea una lista de errores una lista de errores en html
	 * 
	 * @param errors errores
	 * @return Mensaje de lista de errores
	 */
	public static Spanned formatHTMLFromErrors(Exception... errors) {
		return formatHTMLFromObjectList(Throwable::getMessage, errors);
	}

	/**
	 * Formatea una lista de objetos una lista (en cadena) en html
	 * 
	 * @param objects lista de objetos
	 * @return Mensaje de lista de objetos
	 */
    @SafeVarargs
	public static <T> Spanned formatHTMLFromObjectList(AttributePicker<String, T>
															   attributePicker, T... objects) {
		StringBuilder str = new StringBuilder();
		int size = objects.length;
		if (size == 1)
			return Html.fromHtml(str.append(
                    attributePicker.pickAttribute(objects[0])).toString());
		for (int i = 0; i < size; i++) {
			str.append("\u25CF ").append(
					attributePicker.pickAttribute(objects[i]));
			str.append((i < size - 1 ? "<br/>" : ""));
		}
		return Html.fromHtml(str.toString());
	}

	/**
	 * Formatea una lista de mensajes una lista (en cadena) en html
	 * 
	 * @param messages lista de mensajes
	 * @return Mensaje de lista de mensajes
	 */
	public static Spanned formatHTMLFromStringList(String... messages) {
		return formatHTMLFromObjectList(str -> str, messages);
	}
}
