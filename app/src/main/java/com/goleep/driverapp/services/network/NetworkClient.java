package com.goleep.driverapp.services.network;

import android.content.Context;

import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.NetworkStringConstants;
import com.goleep.driverapp.constants.RequestConstants;
import com.goleep.driverapp.constants.SharedPreferenceKeys;
import com.goleep.driverapp.interfaces.NetworkAPICallback;
import com.goleep.driverapp.services.storage.LocalStorageService;
import com.goleep.driverapp.utils.LogUtils;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by kunalsingh on 10/04/17.
 */

public class NetworkClient {
    private ResponseValidator responseValidator;
    private NetworkChecker networkChecker;

    NetworkClient(){
        responseValidator = new ResponseValidator();
        networkChecker = new NetworkChecker();
    }

    public void makePutRequest(Context context, String requestUrl, boolean isAuthRequired, Map<String, Object> bodyParams,
                               final NetworkAPICallback networkAPICallback){
        requestHandler(context, requestUrl, isAuthRequired, bodyParams, RequestConstants.CONTENT_TYPE_JSON,
                NetworkConstants.PUT_REQUEST, networkAPICallback, null);
    }

    public void makeJsonPostRequest(Context context, String requestUrl, boolean isAuthRequired,
                                    Map<String, Object> bodyParams,
                                    final NetworkAPICallback networkAPICallback){
        requestHandler(context, requestUrl, isAuthRequired, bodyParams, RequestConstants.CONTENT_TYPE_JSON,
                NetworkConstants.POST_REQUEST, networkAPICallback, null);
    }

    public void makeJsonPutRequest(Context context, String requestUrl, boolean isAuthRequired,
                                   Map<String, Object> bodyParams,
                                   final NetworkAPICallback networkAPICallback) {
        requestHandler(context, requestUrl, isAuthRequired, bodyParams, RequestConstants.CONTENT_TYPE_JSON,
                NetworkConstants.PUT_REQUEST, networkAPICallback, null);
    }

    public void makeDeleteRequest(Context context, String requestUrl, boolean isAuthRequired,
                                  Map<String, String> headerParams, final NetworkAPICallback networkAPICallback){
        requestHandler(context, requestUrl, isAuthRequired, null,
                RequestConstants.CONTENT_TYPE_JSON, NetworkConstants.DELETE_REQUEST, networkAPICallback, headerParams);

    }

    public void makeGetRequest(Context context, String requestUrl, boolean isAuthRequired,
                               final NetworkAPICallback networkAPICallback){
        requestHandler(context, requestUrl, isAuthRequired, null, null,
                NetworkConstants.GET_REQUEST, networkAPICallback, null);
    }

    public void makeGetRequest(Context context, String requestUrl, Map<String, String> headerParams, boolean isAuthRequired,
                               final NetworkAPICallback networkAPICallback){
        requestHandler(context, requestUrl, isAuthRequired, null, null,
                NetworkConstants.GET_REQUEST, networkAPICallback, headerParams);
    }

    private void requestHandler(final Context context, String requestUrl, final boolean isAuthRequired,
                                Map<String, Object> bodyParams, String contentType,
                                String requestType, final NetworkAPICallback networkAPICallback,
                                Map<String, String> headerParams){
        if(!networkChecker.isNetworkAvailable(context)){
            if(networkAPICallback != null){
                networkAPICallback.onNetworkResponse(NetworkConstants.NETWORK_ERROR, null,
                        networkChecker.getNetworkErrorMessage(context));
            }
            return;
        }
        OkHttpClient client = new OkHttpClient();
        Request request = new RequestFactory().createRequest(context, requestUrl, isAuthRequired,
                requestType, contentType, bodyParams, headerParams);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(networkAPICallback != null){
                    networkAPICallback.onNetworkResponse(NetworkConstants.FAILURE, null,
                            NetworkStringConstants.REQUEST_FAILURE);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtils.error("response", response.toString());
                if(networkAPICallback != null){
                    if(response.header(RequestConstants.AUTHORIZATION) != null) {
                        LocalStorageService.sharedInstance().getLocalFileStore().store(context,
                                SharedPreferenceKeys.AUTH_TOKEN,
                                response.header(RequestConstants.AUTHORIZATION));

                    }
                    Object[] objects = responseValidator.validateResponse(response);
                    networkAPICallback.onNetworkResponse(getNetworkState(objects), getResponse(objects),
                            getErrorMessage(objects));
                }
            }
        });
    }

    public void uploadImage(String authToken, String url, File file, String fileName) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileName, RequestBody.create(MediaType.parse("image/jpeg"), file));
        RequestBody formBody = builder.build();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer " + authToken)
                .url(url)
                .put(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {}

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtils.error("response", response.toString());
            }
        });
    }


    private int getNetworkState(Object[] objects){
        Object state = objects[0];
        return state != null ? (int) state: 0;
    }

    private JSONArray getResponse(Object[] objects){
        Object response = objects[1];
        return response != null ? (JSONArray) response: null;
    }

    private String getErrorMessage(Object[] objects){
        Object error = objects[2];
        return error != null ? (String) error: null;
    }
}
