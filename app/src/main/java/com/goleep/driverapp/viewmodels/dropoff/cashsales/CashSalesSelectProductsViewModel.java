package com.goleep.driverapp.viewmodels.dropoff.cashsales;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.goleep.driverapp.constants.AppConstants;
import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.helpers.uimodels.Customer;
import com.goleep.driverapp.helpers.uimodels.Product;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.network.jsonparsers.OrderItemParser;
import com.goleep.driverapp.services.room.AppDatabase;
import com.goleep.driverapp.services.room.RoomDBService;
import com.goleep.driverapp.services.room.entities.StockProductEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by anurag on 30/03/18.
 */

public class CashSalesSelectProductsViewModel extends AndroidViewModel {

    protected AppDatabase leepDatabase;
    private Customer consumerLocation;
    private Product selectedProduct;
    private ArrayList<Product> scannedProducts = new ArrayList<>();

    public CashSalesSelectProductsViewModel(@NonNull Application application) {
        super(application);
        leepDatabase = RoomDBService.sharedInstance().getDatabase(application);
    }

    public StockProductEntity sellebleProductHavingBarcode(String barcode) {
        return leepDatabase.stockProductDao().sellableProductHavingBarcode(barcode);
    }

    public List<StockProductEntity> sellebleProductsWithName(String searchText) {
        return leepDatabase.stockProductDao().sellebleProductsWithName(searchText);
    }

    public void getProductPricing(int destinationLocationId, int productId, UILevelNetworkCallback networkCallback) {
        String url = UrlConstants.PRODUCT_PRICING_URL + "?destination_location_id=" + destinationLocationId + "&product_id=" + productId;

        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication(), url, true, (type, response, errorMessage) -> {
            switch (type) {
                case NetworkConstants.SUCCESS:
                    double productPrice = new OrderItemParser().getProductPriceByParsingJsonArray(response);
                    networkCallback.onResponseReceived(new ArrayList<>(Collections.singletonList(productPrice)), false,
                            null, false);
                    break;

                case NetworkConstants.FAILURE:
                case NetworkConstants.NETWORK_ERROR:
                    networkCallback.onResponseReceived(null, true, errorMessage, false);
                    break;

                default:
                    networkCallback.onResponseReceived(null, false, null, false);
                    break;
            }
        });
    }

    public Product getProductFromStockProduct(StockProductEntity stockProduct) {
        if (stockProduct == null) return null;
        Product product = new Product();
        product.setId(stockProduct.getId());
        product.setProductName(stockProduct.getProductName());
        product.setPrice(stockProduct.getDefaultPrice());
        product.setQuantity(stockProduct.getQuantity(AppConstants.TYPE_SELLABLE));
        product.setMaxQuantity(product.getQuantity());
        product.setWeight(stockProduct.getWeight());
        product.setWeightUnit(stockProduct.getWeightUnit());
        return product;
    }

    public void addToScannedProduct(Product scannedProduct) {
        scannedProducts.add(scannedProduct);
    }

    public Product getProductFromScannedProducts(int id) {
        for (Product product : scannedProducts) {
            if (product.getId() == id) return product;
        }
        return null;
    }

    public boolean isProductInScannedList(int id) {
        for (Product product : scannedProducts) {
            if (product.getId() == id) return true;
        }
        return false;
    }

    // getters and setters
    public ArrayList<Product> getScannedProducts() {
        return scannedProducts;
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public Customer getConsumerLocation() {
        return consumerLocation;
    }

    public void setConsumerLocation(Customer consumerLocation) {
        this.consumerLocation = consumerLocation;
    }

    public List<StockProductEntity> allProductsWithName(String searchText) {
        return leepDatabase.stockProductDao().allProductsWithName(searchText);
    }

    public StockProductEntity productsWithBarcode(String barcode) {
        return leepDatabase.stockProductDao().productHavingBarcode(barcode);
    }
}
