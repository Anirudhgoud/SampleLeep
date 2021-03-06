package com.tracotech.tracoman.helpers.uihelpers;

import android.content.Context;

import com.tracotech.tracoman.helpers.uimodels.Country;
import com.tracotech.tracoman.services.network.jsonparsers.CountryDataParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by shubham on 10/04/2018.
 */

public class CountryCodeHelper {

    public CountryCodeHelper() {

    }

    public List<Country> getCountries(Context context) {
        String json;
        try (InputStream inputStream = context.getAssets().open("country_data.json");
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
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
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return new CountryDataParser().reportsDataByParsingJsonResponse(jsonArray);
    }
}
