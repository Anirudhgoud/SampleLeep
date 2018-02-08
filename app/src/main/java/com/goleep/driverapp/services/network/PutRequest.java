package com.goleep.driverapp.services.network;



import com.goleep.driverapp.interfaces.NetworkRequest;

import java.util.Map;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by kunalsingh on 13/04/17.
 */

public class PutRequest implements NetworkRequest {

    PutRequest(){}

    @Override
    public Request buildRequest(String url, RequestBody body, Map<String, String> header) {
        okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
        for (Map.Entry<String, String> entry : header.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }
        if(body != null){
            builder.put(body);
        }
        return builder.url(url).build();
    }
}
