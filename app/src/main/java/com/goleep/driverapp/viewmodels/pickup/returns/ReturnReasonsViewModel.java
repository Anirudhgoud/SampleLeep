package com.goleep.driverapp.viewmodels.pickup.returns;

import android.arch.lifecycle.ViewModel;

import com.goleep.driverapp.helpers.uimodels.Product;
import com.goleep.driverapp.helpers.uimodels.ReturnReason;

import java.util.List;

/**
 * Created by vishalm on 05/04/18.
 */

public class ReturnReasonsViewModel extends ViewModel {

    private Product product;
    private List<ReturnReason> returnReasons;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<ReturnReason> getReturnReasons() {
        return returnReasons;
    }

    public void setReturnReasons(List<ReturnReason> returnReasons) {
        this.returnReasons = returnReasons;
    }
}
