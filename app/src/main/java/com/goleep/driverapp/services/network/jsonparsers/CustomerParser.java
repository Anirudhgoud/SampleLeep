package com.goleep.driverapp.services.network.jsonparsers;

import com.goleep.driverapp.helpers.uimodels.Customer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anurag on 29/03/18.
 */

public class CustomerParser {

    public List<Customer> customerListByParsingJsonResponse(JSONArray jsonArray) {
        List<Customer> customers = new ArrayList<>();
        JSONObject firstObj = (JSONObject) jsonArray.opt(0);
        if (firstObj == null) {
            return customers;
        }
        JSONArray jsonList = firstObj.optJSONArray("data");
        if (jsonList == null) {
            return customers;
        }
        for (int i = 0; i < jsonList.length(); i++) {
            JSONObject jsonObject = jsonList.optJSONObject(i);
            Customer customer = customerByParsingJsonResponse(jsonObject);
            if (customer != null) {
                customers.add(customer);
            }
        }
        return customers;
    }

    public Customer customerByParsingJsonResponse(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setId(jsonObject.optInt("id"));
        customer.setName(jsonObject.optString("name"));
        customer.setBusinessId(jsonObject.optInt("business_id"));
        customer.setArea(jsonObject.optString("area"));
        customer.setLastDeliveryDate(jsonObject.optString("last_delivery_date"));
        return customer;
    }
}

