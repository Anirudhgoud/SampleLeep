package com.goleep.driverapp.services.network;



import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.NetworkStringConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.Response;

/**
 * Created by kunalsingh on 10/04/17.
 */

public class ResponseValidator {
    private ObjectMapper objectMapper;


    ResponseValidator(){
        objectMapper = new ObjectMapper();
    }

    public Object[] validateResponse(Response response){
        Object[] responseObjects = new Object[3];
        switch (response.code()){
            case 200:
            case 204:
            case 304:
                responseObjects[0] = NetworkConstants.SUCCESS;
                responseObjects[1] = objectMapper.getResponse(response);
                responseObjects[2] = null;
                break;
            case 404:
            case 405:
            case 422:
            case 500:
                responseObjects[0] = NetworkConstants.FAILURE;
                responseObjects[1] = null;
                responseObjects[2] = parseErrorMessage(response);
                break;
            case 401:
                responseObjects[0] = NetworkConstants.UNAUTHORIZED;
                responseObjects[1] = null;
                responseObjects[2] = NetworkStringConstants.UNAUTHORIZED;
                break;
            default:
                responseObjects[0] = NetworkConstants.FAILURE;
                responseObjects[1] = null;
                responseObjects[2] = NetworkStringConstants.REQUEST_FAILURE;

        }
        return responseObjects;
    }


    private String parseErrorMessage(Response response) {
        String errorMessage = NetworkStringConstants.REQUEST_FAILURE;
        if (response == null) return errorMessage;

        JSONArray jsonArray = objectMapper.getResponse(response);
        if (jsonArray == null) return errorMessage;
        else {
            JSONObject errorObject = jsonArray.optJSONObject(0);
            errorMessage = errorObject.optString("message", NetworkStringConstants.REQUEST_FAILURE);
        }
        return errorMessage;
    }

}
