package com.elfec.sgam.business_logic;

import android.accounts.Account;
import android.accounts.AccountManager;
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
import com.elfec.sgam.model.Device;

import java.io.File;
import java.math.BigDecimal;

/**
 * Se encarga de construir este dispositivo con sus datos reales
 */
@SuppressWarnings("deprecation")
public class PhysicalDeviceBuilder {

    private Context mContext;

    public PhysicalDeviceBuilder(Context context){
        this.mContext = context;
    }

    /**
     * Construye un model de dispositivo
     * con la información del dispositivo real
     *
     * @return Device
     */
    public Device buildDevice(){
        BluetoothAdapter thisDevice = BluetoothAdapter.getDefaultAdapter();
        WifiInfo wifiInfo = ((WifiManager) mContext.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        String screenResolution = "" + dm.widthPixels + "x" + dm.heightPixels;

        return new Device(thisDevice.getName(), getDeviceIdentifier(), Build.SERIAL, wifiInfo.getMacAddress(),
                thisDevice.getAddress(), Build.VERSION.RELEASE,
                Build.getRadioVersion(), Build.BRAND, Build.MODEL, getScreenSize(), screenResolution,
                getBackCameraResolutionInMp(), getSDMemoryCardSize(), getPrimaryGmail());
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
    private String getImei() {
        return ((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE))
                .getDeviceId();
    }

    /**
     * Obtiene el Android Id del dispositivo
     *
     * @return Android Id
     */
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
        Account[] accounts = accountManager.getAccountsByType("com.google");
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
        if (extSDCard != null) {
            StatFs stat = new StatFs(extSDCard.getPath());
            long bytesTotal = (long) stat.getBlockSize() * (long) stat.getBlockCount();
            return Math.round((bytesTotal / 1073741824d) * 100) / 100.0d;
        }
        return null;
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
