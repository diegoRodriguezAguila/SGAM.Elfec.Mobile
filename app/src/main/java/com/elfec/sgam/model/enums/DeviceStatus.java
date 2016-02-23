package com.elfec.sgam.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum que representa los posibles estados de un dispositivo
 */
public enum DeviceStatus {
    /**
     * Estado de dispositivo inhabilitado para el uso del sistema
     */
    UNAUTHORIZED,
    /**
     * Estado de dispositivo habilitado para el uso del sistema
     */
    AUTHORIZED,
    /**
     * Estado de dispositvo pendiente de autorización
     */
    AUTH_PENDING
    ;
    /**
     * Obtiene el estado del dispositivo, equivalente al short provisto
     *
     * @param status estado
     * @return estado del dispositivo
     */
    public static DeviceStatus get(short status) {
        return DeviceStatus.values()[status];
    }

    /**
     * Convierte el estado del dispositivo a su short equivalente
     *
     * @return Short equivalente al estado
     */
    @JsonValue
    public short toShort() {
        return (short) this.ordinal();
    }
}
