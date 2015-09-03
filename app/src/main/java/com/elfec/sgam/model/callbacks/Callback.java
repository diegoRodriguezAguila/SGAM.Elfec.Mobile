package com.elfec.sgam.model.callbacks;

import java.util.List;

/**
 * Callback generica para el procesamiento de datos
 */
interface Callback {
    void onFailure(List<Exception> errors);
}
