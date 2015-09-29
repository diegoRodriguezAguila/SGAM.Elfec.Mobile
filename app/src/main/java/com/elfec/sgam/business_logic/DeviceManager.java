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
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.elfec.sgam.helpers.file.FileHelper;
import com.elfec.sgam.model.Device;
import com.elfec.sgam.model.callbacks.ResultCallback;
import com.elfec.sgam.model.exceptions.AuthPendingDeviceException;
import com.elfec.sgam.model.exceptions.UnauthorizedDeviceException;
import com.elfec.sgam.security.SessionManager;
import com.elfec.sgam.settings.AppPreferences;
import com.elfec.sgam.web_services.RestEndpointFactory;
import com.elfec.sgam.web_services.RetrofitErrorInterpreter;
import com.elfec.sgam.web_services.api_endpoints.IDevicesEndpoint;

import org.apache.http.HttpStatus;

import java.io.File;
import java.math.BigDecimal;

import retrofit.RetrofitError;

/**
 * Lógica de negocio de dispositivo
 */
@SuppressWarnings("deprecation")
public class DeviceManager {

    /**
     * Valida si este dispositivo está registrado y habilitado para ingresar al sistema
     *
     * @param cb callback
     */
    public void validateDevice(final ResultCallback<Device> cb) {
        final String username = SessionManager.instance().getLoggedInUsername();
        final String authToken = SessionManager.instance().getLoggedInToken();
        try {
            Device device = RestEndpointFactory.create(IDevicesEndpoint.class, username, authToken)
                    .getDevice(getImei(AppPreferences.getApplicationContext()));
            checkDeviceStatus(device);
            cb.onSuccess(device);
        } catch (RetrofitError error) {
            if (error.getResponse().getStatus() != HttpStatus.SC_NOT_FOUND)
                cb.onFailure(RetrofitErrorInterpreter.interpretException(error));
            else {
                registerDevice(new ResultCallback<Device>() {
                    @Override
                    public void onSuccess(Device result) {
                        cb.onFailure(new AuthPendingDeviceException());
                    }

                    @Override
                    public void onFailure(Exception... errors) {
                        cb.onFailure(new UnauthorizedDeviceException(), errors[0]);
                    }
                });
            }
        } catch (Exception e) {
            cb.onFailure(e);
        }
    }

    /**
     * Realiza las llamadas remotas para registrar este dispositivo en el servidor
     *
     * @param cb callback
     */
    public void registerDevice(final ResultCallback<Device> cb) {
        final String username = SessionManager.instance().getLoggedInUsername();
        final String authToken = SessionManager.instance().getLoggedInToken();
        try {
            Device device = RestEndpointFactory.create(IDevicesEndpoint.class, username, authToken)
                    .registerDevice(createDevice());
            cb.onSuccess(device);
        } catch (RetrofitError error) {
            cb.onFailure(RetrofitErrorInterpreter.interpretException(error));
        }
    }


    /**
     * Verifica el estado del dispositivo, si es que no está autorizado lanza la debida excepción
     *
     * @param device {@link Device} dispositivo
     * @throws UnauthorizedDeviceException
     * @throws AuthPendingDeviceException
     */
    private void checkDeviceStatus(Device device) throws UnauthorizedDeviceException, AuthPendingDeviceException {
        switch (device.getStatus()) {
            case UNAUTHORIZED:
                throw new UnauthorizedDeviceException();
            case AUTH_PENDING:
                throw new AuthPendingDeviceException();
        }
    }

    /**
     * Crea una representación de este dispositivo
     *
     * @return {@link Device} que representa este dispositivo
     */
    public Device createDevice() {
        Context context = AppPreferences.getApplicationContext();
        BluetoothAdapter thisDevice = BluetoothAdapter.getDefaultAdapter();
        WifiInfo wifiInfo = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        String screenResolution = "" + dm.widthPixels + "x" + dm.heightPixels;

        return new Device(thisDevice.getName(), getImei(context), Build.SERIAL, wifiInfo.getMacAddress(),
                thisDevice.getAddress(), Build.VERSION.RELEASE,
                Build.getRadioVersion(), Build.BRAND, Build.MODEL, getScreenSize(context), screenResolution,
                getBackCameraResolutionInMp(), getSDMemoryCardSize(context), getPrimaryGmail(context));
    }

    /**
     * Obtienen el tamaño de la pantalal en inchs
     *
     * @param context context
     * @return tamaño en inchs de la pantalla
     */
    public double getScreenSize(Context context) {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        int width;
        int height;
        float densityX;
        float densityY;

        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        if (Build.VERSION.SDK_INT >= 17){
            display.getRealSize(size);
            width = size.x;
            height = size.y;
            densityX = dm.xdpi;
            densityY = dm.ydpi;
        }
        else{
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
     * Obtiene el imei del dispositivo
     *
     * @param context context
     * @return imei dispositivo
     */
    private String getImei(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    /**
     * Obtiene la cuenta primaria de gmail del dispositivo
     *
     * @param context context
     * @return cuenta gmail
     */
    private String getPrimaryGmail(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType("com.google");
        if (accounts.length > 0)
            return accounts[0].name.trim().toLowerCase();
        return null;
    }

    /**
     * Obtiene el tamaño de la memory card en GB
     *
     * @param context context
     * @return tamaño memory card en GigaBytes, null si no hay memoria conectada
     */
    private Double getSDMemoryCardSize(Context context) {
        File extSDCard = FileHelper.getExternalSDCardDirectory(context);
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
