package com.elfec.sgam.model;

import android.net.Uri;

import com.elfec.sgam.model.enums.ApiStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Model for applciations
 */
public class ElfecApplication {
    private String name;
    @JsonProperty("package") //as package is a java reserved word
    private String packageName;
    private String latestVersion;
    private int latestVersionCode;
    private Uri url;
    private Uri iconUrl;
    private List<AppVersion> appVersions;
    private ApiStatus status;

    //region Getters Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public int getLatestVersionCode() {
        return latestVersionCode;
    }

    public void setLatestVersionCode(int latestVersionCode) {
        this.latestVersionCode = latestVersionCode;
    }

    public Uri getUrl() {
        return url;
    }

    public void setUrl(Uri url) {
        this.url = url;
    }

    public List<AppVersion> getAppVersions() {
        return appVersions;
    }

    public void setAppVersions(List<AppVersion> appVersions) {
        this.appVersions = appVersions;
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
