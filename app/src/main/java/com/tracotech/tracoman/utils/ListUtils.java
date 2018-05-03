package com.tracotech.tracoman.utils;

import com.tracotech.tracoman.helpers.uimodels.Product;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {

    //Returns the list with combined selected and returned products
    public List<Product> combinedList(List<Product> selectedProducts, List<Product> returnedProducts){
        List<Product> finalList = new ArrayList<>();

        if (selectedProducts == null && returnedProducts == null) return finalList;
        else if (selectedProducts != null && returnedProducts == null) return selectedProducts;
        else if (selectedProducts == null) return returnedProducts;
        else {
            finalList.addAll(selectedProducts);
            for (Product returnedProduct : returnedProducts) {
                Product product = productWithId(returnedProduct.getId(), finalList);
                if (product == null){
                    finalList.add(returnedProduct);
                }else {
                    product.setReturnQuantity(returnedProduct.getReturnQuantity());
                    product.setReturnReason(returnedProduct.getReturnReason());
                }
            }
        }
        return finalList;
    }

    //Check if Product with given id is present in productList
    private Product productWithId(int id, List<Product> productList){
        for (Product product : productList) {
            if (product.getId() == id) return product;
        }
        return null;
    }
}
