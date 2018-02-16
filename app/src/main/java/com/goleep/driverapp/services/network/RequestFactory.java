package com.goleep.driverapp.services.network;

import android.content.Context;


import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.RequestConstants;
import com.goleep.driverapp.constants.SharedPreferenceKeys;
import com.goleep.driverapp.interfaces.NetworkRequest;
import com.goleep.driverapp.services.storage.LocalFileStore;
import com.goleep.driverapp.services.storage.LocalStorageService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by kunalsingh on 12/04/17.
 */

public class RequestFactory {

    RequestFactory(){}

    public Request createRequest(Context context, String url, boolean isAuthRequired, String requestType,
                                 String contentType, Map<String, Object> bodyParams, Map<String, String> localHeaderParams){
        NetworkRequest networkRequest;
        if(requestType.equals(NetworkConstants.PUT_REQUEST)){
            networkRequest = new PutRequest();
        }else if(requestType.equals(NetworkConstants.POST_REQUEST)){
            networkRequest = new PostRequest();
        } else if(requestType.equals(NetworkConstants.DELETE_REQUEST)){
            networkRequest = new DeleteRequest();
        }else{
            networkRequest = new GetRequest();
        }
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put(RequestConstants.KEY_USER_AGENT, RequestConstants.USER_AGENT);
        RequestBody requestBody = null;
        if(isAuthRequired){
            LocalFileStore localFileStore = LocalStorageService.sharedInstance().getLocalFileStore();
            String authToken = localFileStore.getString(context, SharedPreferenceKeys.AUTH_TOKEN);
            headerParams.put(RequestConstants.AUTHORIZATION, authToken);
        }
        if(localHeaderParams != null){
            for (Map.Entry<String, String> entry : localHeaderParams.entrySet()) {
                headerParams.put(entry.getKey(), entry.getValue());
            }
        }
        if(contentType != null && contentType.equals("form")){
            FormBody.Builder bodyBuilder = new FormBody.Builder();
            for (Map.Entry<String, Object> entry : bodyParams.entrySet()) {
                String value;
                try{
                    value = (String) entry.getValue();
                }catch (ClassCastException exception){
                    exception.printStackTrace();
                    value = entry.getValue().toString();
                }
                bodyBuilder.add(entry.getKey(), value);
            }
            requestBody = bodyBuilder.build();
            headerParams.put("Content-Type", "application/x-form-urlencoded");
        }else if (contentType != null && contentType.equals(RequestConstants.CONTENT_TYPE_JSON)){
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            headerParams.put("Content-Type", "application/json");
            JSONObject json = new JSONObject();
            if(bodyParams != null) {
                for (Map.Entry<String, Object> entry : bodyParams.entrySet()) {
                    Object value;
                    try {
                        value = entry.getValue();
                    } catch (ClassCastException exception) {
                        exception.printStackTrace();
                        value = entry.getValue().toString();
                    }
                    try {
                        json.put(entry.getKey(), value);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                requestBody = RequestBody.create(JSON, String.valueOf(json));
            }
        }
        return networkRequest.buildRequest(url, requestBody, headerParams);
    }
}
