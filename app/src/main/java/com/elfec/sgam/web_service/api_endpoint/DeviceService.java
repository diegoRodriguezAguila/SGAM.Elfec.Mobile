package com.elfec.sgam.web_service.api_endpoint;

import com.elfec.sgam.model.Device;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Endpoint de los webservices de dispositivos <i>/devices</i> en la API
 */
public interface DeviceService {

    @GET("devices/{imei}")
    Observable<Device> getDevice(@Path("imei") String imei);

    @POST("devices")
    Observable<Device> registerDevice(@Body Device device);
}
