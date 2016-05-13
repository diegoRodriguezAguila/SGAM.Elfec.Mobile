package com.elfec.sgam.model.web_services;

import com.elfec.sgam.model.enums.DeviceSessionStatus;

/**
 * Session especifica de un dispositivo
 */
public class DeviceSession {
    private String id;
    private String username;
    private String imei;
    private DeviceSessionStatus status;

    //region Getters Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public DeviceSessionStatus getStatus() {
        return status;
    }

    public void setStatus(DeviceSessionStatus status) {
        this.status = status;
    }

    //endregion
}
