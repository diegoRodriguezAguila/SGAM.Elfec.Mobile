package com.elfec.sgam.model.data_access;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Clase que representa la base de datos SGAM
 */
@Database(name = SGAMDataBase.NAME, version = SGAMDataBase.VERSION)
public class SGAMDataBase {
    public static final String NAME = "SGAMDataBase";

    public static final int VERSION = 1;
}
