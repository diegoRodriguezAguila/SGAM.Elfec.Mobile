package com.elfec.sgam.model;

import android.net.Uri;
import android.os.Bundle;

import com.elfec.sgam.model.enums.InstallationStatus;

/**
 * Created by drodriguez on 07/06/2016.
 * Model installation
 */
public class Installation {
    private String id;
    private String appName;
    private String packageName;
    private String version;
    private Uri iconUrl;
    private String imei;
    private InstallationStatus status;

    public Installation() {
    }

    public Installation(Bundle bundle) {
        id = bundle.getString("id");
        appName = bundle.getString("app_name");
        packageName = bundle.getString("package");
        version = bundle.getString("version");
        setStatus(bundle.getString("status"));
        iconUrl = Uri.parse(bundle.getString("icon_url"));
    }

    /**
     * Gets the supposed file name for this apk version
     * @return file name
     */
    public String getFileName(){
        return getInstallationFileName(packageName, version);
    }

    /**
     * Gets the supposed file name for this apk version
     * @return file name
     */
    public static String getInstallationFileName(String packageName, String version){
        return String.format("%s1 - v.%s2.apk", packageName, version);
    }

    /**
     * Gets the app's name with the version
     * @return ie. <i>Cool App v.1.0.3</i>
     */
    public String getVersionedAppName(){
        return String.format("%s v.%s", appName, version);
    }

    //region Getters Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Uri getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(Uri iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public InstallationStatus getStatus() {
        return status;
    }

    public void setStatus(InstallationStatus status) {
        this.status = status;
    }

    public void setStatus(String status) {
        if (status == null)
            return;
        this.status = InstallationStatus.valueOf(status.toUpperCase());
    }
    //endregion
}
