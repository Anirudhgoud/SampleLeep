package com.goleep.driverapp.services.network;



import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.NetworkStringConstants;

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
            case 401:
            case 500:
                responseObjects[0] = NetworkConstants.FAILURE;
                responseObjects[1] = null;
                responseObjects[2] = NetworkStringConstants.BAD_REQUEST;
                break;
        }
        return responseObjects;
    }


}
