package com.elfec.sgam.business_logic;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.elfec.sgam.helpers.file.FileHelper;
import com.elfec.sgam.helpers.utils.MemoryUtils;
import com.elfec.sgam.model.Device;
import com.elfec.sgam.settings.AppPreferences;

import java.io.File;
import java.math.BigDecimal;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * Se encarga de construir este dispositivo con sus datos reales
 */
@SuppressWarnings("deprecation")
public class PhysicalDeviceBuilder {

    private Context mContext;
    private static final String GOOGLE_ACCOUNT = "com.google";
    private static final String DEFAULT_MAC_ADDRESS = "02:00:00:00:00:00";

    /**
     * Construye un nuevo {@link PhysicalDeviceBuilder}
     * utilizando el contexto de la applicacion:
     * {@link AppPreferences#getApplicationContext()}
     */
    public static PhysicalDeviceBuilder standard() {
        return new PhysicalDeviceBuilder();
    }

    /**
     * Construye un nuevo {@link PhysicalDeviceBuilder}
     * utilizando el contexto de la applicacion.
     * Para utilizarlo usar el metodo {@link #standard()}
     */
    private PhysicalDeviceBuilder() {
        this.mContext = AppPreferences.getApplicationContext();
    }

    /**
     * Construye un nuevo {@link PhysicalDeviceBuilder}
     * con el context proporcionado
     *
     * @param context contexto
     */
    public PhysicalDeviceBuilder(Context context) {
        this.mContext = context;
    }

    /**
     * Construye un model de dispositivo
     * con la información del dispositivo real
     *
     * @return Device
     */
    public Device buildDevice() {
        BluetoothAdapter thisDevice = BluetoothAdapter.getDefaultAdapter();

        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        String screenResolution = "" + dm.widthPixels + "x" + dm.heightPixels;

        return new Device(thisDevice.getName(), getDeviceIdentifier(), Build.SERIAL,
                getWifiMacAddress(), getBluetoothMacAddress(thisDevice), Build.VERSION.RELEASE,
                Build.getRadioVersion(), Build.BRAND, Build.MODEL, getScreenSize(), screenResolution,
                getBackCameraResolutionInMp(), getSDMemoryCardSize(), getPrimaryGmail());
    }

    /**
     * Gets the device's bluetooth mac address
     * @param thisDevice bluetooth adapter
     * @return mac address
     */
    @SuppressLint("HardwareIds")
    private String getBluetoothMacAddress(BluetoothAdapter thisDevice) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return thisDevice.getAddress();
        }
        try {
            return android.provider.Settings.Secure.getString(mContext.getContentResolver(),
                    "bluetooth_address");
        } catch (Throwable t) {
            return DEFAULT_MAC_ADDRESS;
        }
    }

    /**
     * Gets the device's wifi mac address
     * @return mac address
     */
    @SuppressLint("HardwareIds")
    private String getWifiMacAddress(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            WifiInfo wifiInfo = ((WifiManager) mContext
                    .getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
            wifiInfo.getMacAddress();
        }
        return getWifiMacAddressApi23();
    }

    @TargetApi(23)
    private String getWifiMacAddressApi23() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            return DEFAULT_MAC_ADDRESS;
        }
        return DEFAULT_MAC_ADDRESS;
    }

    /**
     * Obtienen el tamaño de la pantalal en inchs
     *
     * @return tamaño en inchs de la pantalla
     */
    private double getScreenSize() {
        Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        int width;
        int height;
        float densityX;
        float densityY;

        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            display.getRealSize(size);
            width = size.x;
            height = size.y;
            densityX = dm.xdpi;
            densityY = dm.ydpi;
        } else {
            width = dm.widthPixels;
            height = dm.heightPixels;
            densityX = dm.densityDpi;
            densityY = dm.densityDpi;
        }

        double x = Math.pow(width / densityX, 2);
        double y = Math.pow(height / densityY, 2);
        double screenInches = Math.sqrt(x + y);
        BigDecimal bd = new BigDecimal(Double.toString(screenInches));
        bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Obtiene un identificador unico para el dispositivo
     * En la mayoría de dispositivos este es el IMEI, pero
     * en aquellos dispositivos sin chip se utiliza el android Id
     *
     * @return Identificador para el dispositivo
     */
    public String getDeviceIdentifier() {
        String imei = getImei();
        if (imei == null)
            return getAndroidId();
        return imei;
    }

    /**
     * Obtiene la versión de la aplicación
     *
     * @return entero representando la version en el manifest
     */
    @SuppressLint("HardwareIds")
    private String getImei() {
        return ((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE))
                .getDeviceId();
    }

    /**
     * Obtiene el Android Id del dispositivo
     *
     * @return Android Id
     */
    @SuppressLint("HardwareIds")
    private String getAndroidId() {
        return Settings.Secure.getString(mContext.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    /**
     * Obtiene la cuenta primaria de gmail del dispositivo
     *
     * @return cuenta gmail
     */
    private String getPrimaryGmail() {
        AccountManager accountManager = AccountManager.get(mContext);
        Account[] accounts = accountManager.getAccountsByType(GOOGLE_ACCOUNT);
        if (accounts.length > 0)
            return accounts[0].name.trim().toLowerCase();
        return null;
    }

    /**
     * Obtiene el tamaño de la memory card en GB
     *
     * @return tamaño memory card en GigaBytes, null si no hay memoria conectada
     */
    private Double getSDMemoryCardSize() {
        File extSDCard = FileHelper.getExternalSDCardDirectory(mContext);
        if (extSDCard == null) {
            return null;
        }
        StatFs stat = new StatFs(extSDCard.getPath());
        long bytesTotal = (long) stat.getBlockSize() * (long) stat.getBlockCount();
        double realSize = Math.round((bytesTotal / 1073741824d) * 100) / 100.0d;
        return MemoryUtils.getRetailMemorySize(realSize);
    }

    /**
     * Obtiene la resolución en megapixeles de la cámara trasera
     *
     * @return resolución en megapixeles
     */
    private double getBackCameraResolutionInMp() {
        int noOfCameras = Camera.getNumberOfCameras();
        double maxResolution = -1;
        long pixelCount = -1;
        for (int i = 0; i < noOfCameras; i++) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(i, cameraInfo);

            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                Camera camera = Camera.open(i);
                Camera.Parameters cameraParams = camera.getParameters();
                for (int j = 0; j < cameraParams.getSupportedPictureSizes().size(); j++) {
                    long pixelCountTemp = cameraParams.getSupportedPictureSizes().get(j).width
                            * cameraParams.getSupportedPictureSizes().get(j).height;
                    if (pixelCountTemp > pixelCount) {
                        pixelCount = pixelCountTemp;
                        maxResolution = ((double) pixelCountTemp) / (1000000.0d);
                    }
                }

                camera.release();
            }
        }

        return Math.ceil(maxResolution * 10) / 10.0d;
    }
}
