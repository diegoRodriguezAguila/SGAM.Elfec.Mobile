package com.elfec.sgam.web_services.api_endpoints;

import com.elfec.sgam.model.Device;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Endpoint de los webservices de dispositivos <i>/devices</i> en la API
 */
public interface IDevicesEndpoint {

    @GET("/devices/{imei}")
    Device getDevice(@Path("imei") String imei);

    @POST("/devices")
    Device registerDevice(@Body Device device);
}
