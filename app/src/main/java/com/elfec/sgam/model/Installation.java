package com.elfec.sgam.model;

import android.os.Bundle;

import com.elfec.sgam.model.enums.InstallationStatus;

/**
 * Created by drodriguez on 07/06/2016.
 * Model installation
 */
public class Installation {
    private String id;
    private String packageName;
    private String version;
    private String imei;
    private InstallationStatus status;

    public Installation() {
    }

    public Installation(Bundle bundle) {
        id = bundle.getString("id");
        packageName = bundle.getString("package");
        version = bundle.getString("version");
        setStatus(bundle.getString("status"));
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

    //region Getters Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
