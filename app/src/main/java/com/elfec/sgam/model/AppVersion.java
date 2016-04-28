package com.elfec.sgam.model;

import android.net.Uri;

import com.elfec.sgam.model.enums.ApiStatus;

/**
 * Model for versions of the applications
 */
public class AppVersion {

    private String version;
    private int versionCode;
    private Uri url;
    private Uri iconUrl;
    private ApiStatus status;

    //region Getters Setters
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public Uri getUrl() {
        return url;
    }

    public void setUrl(Uri url) {
        this.url = url;
    }

    public Uri getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(Uri iconUrl) {
        this.iconUrl = iconUrl;
    }

    public ApiStatus getStatus() {
        return status;
    }

    public void setStatus(ApiStatus status) {
        this.status = status;
    }

    //endregion
}
