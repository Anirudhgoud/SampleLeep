package com.tracotech.tracoman.services.network;

import com.tracotech.tracoman.interfaces.NetworkRequest;

import java.util.Map;

import okhttp3.RequestBody;

/**
 * Created by kunalsingh on 12/04/17.
 */

public class PostRequest implements NetworkRequest {

    PostRequest(){}

    @Override
    public okhttp3.Request buildRequest(String url, RequestBody body, Map<String, String> header) {
        okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
        for (Map.Entry<String, String> entry : header.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }
        if(body != null){
            builder.post(body);
        }
        return builder.url(url).build();
    }
}
