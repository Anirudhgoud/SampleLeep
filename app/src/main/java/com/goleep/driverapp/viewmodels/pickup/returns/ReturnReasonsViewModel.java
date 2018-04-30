package com.goleep.driverapp.viewmodels.pickup.returns;

import android.arch.lifecycle.ViewModel;

import com.goleep.driverapp.constants.AppConstants;
import com.goleep.driverapp.constants.ReasonCategory;
import com.goleep.driverapp.helpers.uimodels.Product;
import com.goleep.driverapp.helpers.uimodels.Reason;
import com.goleep.driverapp.helpers.uimodels.ReturnReason;

import java.util.ArrayList;
import java.util.Collections;
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

    public List<Reason> generateReturnReasonsList(){
        if (returnReasons == null) return Collections.emptyList();
        List<Reason> reasonList = new ArrayList<>();

        List<ReturnReason> resellableItems = new ArrayList<>();
        List<ReturnReason> nonResellableItems = new ArrayList<>();
        for (ReturnReason returnReason : returnReasons) {
            String category = returnReason.getReasonCategory();
            returnReason.setItemType(AppConstants.TYPE_REASON);
            if (category == null) continue;
            switch (category){
                case ReasonCategory.RESELLABLE: resellableItems.add(returnReason); break;
                case ReasonCategory.NON_RESELLABLE: nonResellableItems.add(returnReason); break;
            }
        }

        if (resellableItems.size() > 0){
            reasonList.add(headerItem(ReasonCategory.RESELLABLE));
            reasonList.addAll(resellableItems);
        }

        if (nonResellableItems.size() > 0){
            reasonList.add(headerItem(ReasonCategory.NON_RESELLABLE));
            reasonList.addAll(nonResellableItems);
        }
        return reasonList;
    }

    private Reason headerItem(String title){
        Reason reason = new Reason();
        reason.setItemType(AppConstants.TYPE_ORDERS_HEADER);
        reason.setTitle(title);
        return reason;
    }
}
