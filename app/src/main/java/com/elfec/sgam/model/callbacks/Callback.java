package com.elfec.sgam.model.callbacks;

/**
 * Callback generica para el procesamiento de datos
 */
interface Callback {
    void onFailure(Exception... errors);
}
