package com.goleep.driverapp.helpers.uihelpers;

import android.content.Context;

import com.goleep.driverapp.helpers.uimodels.Country;
import com.goleep.driverapp.services.network.jsonparsers.CountryDataParser;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by shubham on 10/04/2018.
 */

public class CountryCodeHelper {
    private  Context  context;

    public CountryCodeHelper(Context context) {
        this.context = context;
    }

    public List<Country> getCountries() {
        String json = null;
        try (InputStream inputStream = context.getAssets().open("country_data.json");
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader);) {
            StringBuilder stringBuilder = new StringBuilder();
            String content;
            while ((content = bufferedReader.readLine()) != null) {
                stringBuilder.append(content);
            }
            json = stringBuilder.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        JSONArray jsonObject = null;
        try {
            jsonObject = new JSONArray(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return new CountryDataParser().reportsDataByParsingJsonResponse(jsonObject);
    }
}
