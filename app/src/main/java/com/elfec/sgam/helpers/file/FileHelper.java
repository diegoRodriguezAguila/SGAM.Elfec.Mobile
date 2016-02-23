package com.elfec.sgam.helpers.file;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import java.io.File;

/**
 * Helper para archivos y carpetas
 */
public class FileHelper {

    /**
     * Obtiene la primera memoria externa conectada al dispositivo
     *
     * @param context context
     * @return {@link File} carpeta que hace referencia a la memoria externa, retorna null si es que
     * el dispositivo no tiene ninguna memoria externa conectada
     */
    public static File getExternalSDCardDirectory(Context context) {
        File firstSdCard = context.getExternalFilesDir(null);
        File[] files = ContextCompat.getExternalFilesDirs(context, null);
        for (File file : files) {
            if (file != null && file.compareTo(firstSdCard) != 0) {
                return file;
            }
        }
        return null;
    }
}
