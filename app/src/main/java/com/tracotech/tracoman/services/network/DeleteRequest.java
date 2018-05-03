package com.tracotech.tracoman.services.network;

import com.tracotech.tracoman.interfaces.NetworkRequest;

import java.util.Map;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by vishalm on 14/02/18.
 */

public class DeleteRequest implements NetworkRequest {
    @Override
    public Request buildRequest(String url, RequestBody body, Map<String, String> header) {
        okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
        for (Map.Entry<String, String> entry : header.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }
        return builder.url(url).delete().build();
    }
}
