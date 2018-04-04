package com.goleep.driverapp.services.network.jsonparsers;

import com.goleep.driverapp.helpers.uimodels.BusinessCategoryAttribute;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubham on 03/04/2018.
 */

public class BusinessCategoryParser {
    public List<BusinessCategoryAttribute> reportsDataByParsingJsonResponse(JSONObject jsonObject){
        if(jsonObject == null){
            return null;
        }else {
                List<BusinessCategoryAttribute> listBusinessCategoryAttribute = new ArrayList<>();
            JSONArray jsonArrayData = jsonObject.optJSONArray("data");
            for(int index = 0; index<jsonArrayData.length();index++){
                JSONObject jsonObject1 = jsonArrayData.optJSONObject(index);
                BusinessCategoryAttribute businessCategoryAttribute =new BusinessCategoryAttribute();
                businessCategoryAttribute.setId(jsonObject1.optInt("id"));
                businessCategoryAttribute.setName(jsonObject1.optString("name"));
                businessCategoryAttribute.setBusinessesCount(jsonObject1.optInt("businesses_count"));
                listBusinessCategoryAttribute.add(businessCategoryAttribute);
            }
            return  listBusinessCategoryAttribute;
        }
    }

}
