package com.elfec.sgam.model;

import com.elfec.sgam.model.enums.DeviceStatus;

/**
 * Modelo de dispositivo
 */
public class Device {

    private String name;
    private String imei;
    private String serial;
    private String wifiMacAddress;
    private String bluetoothMacAddress;
    private String platform;
    private String os_version;
    private String basebandVersion;
    private String brand;
    private String model;
    private String phoneNumber;
    private String idCiscoAsa;
    private Double screenSize;
    private String screenResolution;
    private Double camera;
    private Double sdMemoryCard;
    private String gmailAccount;
    private String comments;
    private short status;

    public Device(){}

    public Device(String name, String imei, String serial, String wifiMacAddress,
                  String bluetoothMacAddress, String os_version,
                  String basebandVersion, String brand, String model, Double screenSize,
                  String screenResolution, Double camera, Double sdMemoryCard, String gmailAccount) {
        this.name = name;
        this.imei = imei;
        this.serial = serial;
        this.wifiMacAddress = wifiMacAddress;
        this.bluetoothMacAddress = bluetoothMacAddress;
        this.os_version = os_version;
        this.basebandVersion = basebandVersion;
        this.brand = brand;
        this.model = model;
        this.screenSize = screenSize;
        this.screenResolution = screenResolution;
        this.camera = camera;
        this.sdMemoryCard = sdMemoryCard;
        this.gmailAccount = gmailAccount;
        this.status = 2;
    }

    //region Getters Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getWifiMacAddress() {
        return wifiMacAddress;
    }

    public void setWifiMacAddress(String wifiMacAddress) {
        this.wifiMacAddress = wifiMacAddress;
    }

    public String getBluetoothMacAddress() {
        return bluetoothMacAddress;
    }

    public void setBluetoothMacAddress(String bluetoothMacAddress) {
        this.bluetoothMacAddress = bluetoothMacAddress;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getOs_version() {
        return os_version;
    }

    public void setOs_version(String os_version) {
        this.os_version = os_version;
    }

    public String getBasebandVersion() {
        return basebandVersion;
    }

    public void setBasebandVersion(String basebandVersion) {
        this.basebandVersion = basebandVersion;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getIdCiscoAsa() {
        return idCiscoAsa;
    }

    public void setIdCiscoAsa(String idCiscoAsa) {
        this.idCiscoAsa = idCiscoAsa;
    }

    public Double getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(Double screenSize) {
        this.screenSize = screenSize;
    }

    public Double getCamera() {
        return camera;
    }

    public void setCamera(Double camera) {
        this.camera = camera;
    }

    public String getScreenResolution() {
        return screenResolution;
    }

    public void setScreenResolution(String screenResolution) {
        this.screenResolution = screenResolution;
    }

    public Double getSdMemoryCard() {
        return sdMemoryCard;
    }

    public void setSdMemoryCard(Double sdMemoryCard) {
        this.sdMemoryCard = sdMemoryCard;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getGmailAccount() {
        return gmailAccount;
    }

    public void setGmailAccount(String gmailAccount) {
        this.gmailAccount = gmailAccount;
    }

    public DeviceStatus getStatus() {
        return DeviceStatus.get(status);
    }

    public void setStatus(DeviceStatus status) {
        this.status = status.toShort();
    }
    //endregion

}
