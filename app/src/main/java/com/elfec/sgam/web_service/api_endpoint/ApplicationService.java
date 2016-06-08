package com.elfec.sgam.web_service.api_endpoint;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import rx.Observable;

/**
 * Created by drodriguez on 07/06/2016.
 * application services endpoint
 */
public interface ApplicationService {
    //http://192.168.50.56:3000/api/applications/com.elfec.lecturas/1.16.01.13?d
    @Streaming
    @GET("applications/{packageName}/{version}?d")
    Observable<ResponseBody> downloadApk(@Path("packageName") String packageName,
                                         @Path("version") String version);
}
