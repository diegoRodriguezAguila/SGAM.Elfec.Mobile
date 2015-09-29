package com.elfec.sgam.business_logic;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.hardware.Camera;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

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

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Lógica de negocio de dispositivo
 */
public class DeviceManager {

    /**
     * Valida si este dispositivo está registrado y habilitado para ingresar al sistema
     *
     * @param cb callback
     */
    public void validateDevice(final ResultCallback<Device> cb) {
        final String username = SessionManager.instance().getLoggedInUsername();
        final String authToken = SessionManager.instance().getLoggedInToken();
        RestEndpointFactory.create(IDevicesEndpoint.class, username, authToken)
                .getDevice(getImei(AppPreferences.getApplicationContext()),
                        new Callback<Device>() {
                            @Override
                            public void success(Device device, Response response) {
                                try {
                                    checkDeviceStatus(device);
                                    cb.onSuccess(device);
                                } catch (Exception e) {
                                    cb.onFailure(e);
                                }
                            }
                            @Override
                            public void failure(RetrofitError error) {
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
                                            cb.onFailure(new UnauthorizedDeviceException());
                                        }
                                    });
                                }
                            }
                        });
    }

    /**
     * Realiza las llamadas remotas para registrar este dispositivo en el servidor
     *
     * @param cb callback
     */
    public void registerDevice(final ResultCallback<Device> cb) {
        final String username = SessionManager.instance().getLoggedInUsername();
        final String authToken = SessionManager.instance().getLoggedInToken();
        RestEndpointFactory.create(IDevicesEndpoint.class, username, authToken)
                .registerDevice(createDevice(), new Callback<Device>() {
                    @Override
                    public void success(Device device, Response response) {
                        cb.onSuccess(device);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        cb.onFailure(RetrofitErrorInterpreter.interpretException(error));
                    }
                });
    }

    private void checkDeviceStatus(Device device) throws UnauthorizedDeviceException, AuthPendingDeviceException {
        switch (device.getStatus()){
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
        String screenResolution = "" + dm.heightPixels + "x" + dm.widthPixels;
        double x = Math.pow((double) dm.widthPixels / (double) dm.densityDpi, 2);
        double y = Math.pow((double) dm.heightPixels / (double) dm.densityDpi, 2);
        double screenInches = Math.sqrt(x + y);

        return new Device(thisDevice.getName(), getImei(context), Build.SERIAL, wifiInfo.getMacAddress(),
                thisDevice.getAddress(), Build.VERSION.RELEASE,
                Build.getRadioVersion(), Build.BRAND, Build.MODEL, screenInches, screenResolution,
                getBackCameraResolutionInMp(), getSDMemoryCardSize(), getPrimaryGmail(context));
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
     * @param context
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
     * @return tamaño memory card en GigaBytes
     */
    private double getSDMemoryCardSize() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesTotal = (long) stat.getBlockSize() * (long) stat.getBlockCount();
        return bytesTotal / 1073741824d;
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
                ;
                Camera.Parameters cameraParams = camera.getParameters();
                for (int j = 0; j < cameraParams.getSupportedPictureSizes().size(); j++) {
                    long pixelCountTemp = cameraParams.getSupportedPictureSizes().get(i).width * cameraParams.getSupportedPictureSizes().get(i).height;
                    if (pixelCountTemp > pixelCount) {
                        pixelCount = pixelCountTemp;
                        maxResolution = ((float) pixelCountTemp) / (1024000.0f);
                    }
                }

                camera.release();
            }
        }

        return maxResolution;
    }
}
