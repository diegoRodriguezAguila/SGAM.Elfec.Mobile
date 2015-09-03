package com.elfec.sgam.model.callbacks;

/**
 * Callback para el procesamiento con resultados
 */
public interface ResultCallback<T> extends Callback {
    void onSuccess(T result);
}
