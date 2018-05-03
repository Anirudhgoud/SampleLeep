package com.tracotech.tracoman.services.network;



import com.tracotech.tracoman.interfaces.NetworkRequest;

import java.util.Map;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by kunalsingh on 13/04/17.
 */

public class GetRequest implements NetworkRequest {

    GetRequest(){}

    @Override
    public Request buildRequest(String url, RequestBody body, Map<String, String> header) {
        okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
        for (Map.Entry<String, String> entry : header.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }
        return builder.url(url).build();
    }
}
