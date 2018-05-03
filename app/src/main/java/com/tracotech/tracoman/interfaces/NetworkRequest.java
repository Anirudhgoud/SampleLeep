package com.tracotech.tracoman.interfaces;

import java.util.Map;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by kunalsingh on 12/04/17.
 */

public interface NetworkRequest {
    Request buildRequest(String url, RequestBody body, Map<String, String> header);
}
