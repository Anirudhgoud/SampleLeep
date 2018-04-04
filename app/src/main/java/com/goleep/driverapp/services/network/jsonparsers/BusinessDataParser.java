package com.goleep.driverapp.services.network.jsonparsers;

import com.goleep.driverapp.helpers.uimodels.BusinessAttribute;
import com.goleep.driverapp.helpers.uimodels.ReportAttrribute;

import org.json.JSONObject;

/**
 * Created by shubham on 04/04/2018.
 */

public class BusinessDataParser {
    public BusinessAttribute reportsDataByParsingJsonResponse(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        int id;
        String name;
        String imageUrl;
        String category;
        String email;
        String contactName1;
        String contactEmail1;
        String designation1;
        String contactNumber1;
        String contactName2;
        String contactEmail2;
        String designation2;
        String contactNumber2;
        String addressLine1;
        String addressLine2;
        String city;
        String state;
        String country;
        String postalCode;
        int businessCategoryId;
        String description;
        boolean active;
        String countryCode1;
        String countryCode2;
        int countryId;
        String locations;
        BusinessAttribute businessAttribute = new BusinessAttribute();
        businessAttribute.setId(jsonObject.optInt("id"));
        businessAttribute.setName(jsonObject.optString("name"));
        businessAttribute.setImageUrl(jsonObject.optString("image_url"));
        businessAttribute.setCategory(jsonObject.optString("category"));
        businessAttribute.setEmail(jsonObject.optString("email"));
        businessAttribute.setContactName1(jsonObject.optString("contact_name_1"));
        businessAttribute.setContactEmail1(jsonObject.optString("contact_email_1"));
        businessAttribute.setDesignation1(jsonObject.optString("designation_1"));
        businessAttribute.setContactNumber1(jsonObject.optString("contact_number_1"));
        businessAttribute.setContactName2(jsonObject.optString("contact_name_2"));
        businessAttribute.setContactEmail2(jsonObject.optString("contact_email_2"));
        businessAttribute.setDesignation2(jsonObject.optString("designation_2"));
        businessAttribute.setContactName2(jsonObject.optString("contact_number_2"));
        businessAttribute.setAddressLine1(jsonObject.optString("address_line1"));
        businessAttribute.setAddressLine2(jsonObject.optString("address_line2"));
        businessAttribute.setCity(jsonObject.optString("city"));
        businessAttribute.setState(jsonObject.optString("state"));
        businessAttribute.setCountry(jsonObject.optString("country"));
        businessAttribute.setPostalCode(jsonObject.optString("postal_code"));
        businessAttribute.setBusinessCategoryId(jsonObject.optInt("business_category_id"));
        businessAttribute.setDescription(jsonObject.optString("description"));
        businessAttribute.setActive(jsonObject.optBoolean("active"));
        businessAttribute.setCountryCode1(jsonObject.optString("country_code_1"));
        businessAttribute.setCountryCode2(jsonObject.optString("country_code_2"));
        businessAttribute.setCountryId(jsonObject.optInt("country_id"));
        businessAttribute.setLocations(jsonObject.optString("locations"));
        return businessAttribute;
    }
}
