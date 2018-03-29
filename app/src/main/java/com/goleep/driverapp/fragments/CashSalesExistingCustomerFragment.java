package com.goleep.driverapp.fragments;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.goleep.driverapp.R;
import com.goleep.driverapp.adapters.CustomerListAdapter;
import com.goleep.driverapp.helpers.uihelpers.LocationHelper;
import com.goleep.driverapp.helpers.uimodels.Customer;
import com.goleep.driverapp.interfaces.CustomerClickEventListener;
import com.goleep.driverapp.interfaces.LocationChangeListener;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.leep.ParentAppCompatActivity;
import com.goleep.driverapp.viewmodels.CashSalesExistingCustomerViewModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anurag on 19/03/18.
 */

public class CashSalesExistingCustomerFragment extends Fragment implements LocationChangeListener {

    private RecyclerView customersRecyclerView;

    private CashSalesExistingCustomerViewModel viewModel;
    private CustomerListAdapter customerListAdapter;
    private final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    private CustomerClickEventListener customerClickEventListener = customerId -> {

    };

    public CashSalesExistingCustomerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cash_sales_existing_customer, container, false);
        customersRecyclerView = view.findViewById(R.id.customer_recycler_view);
        initialise();
        checkForLocationPermission();
        return view;
    }

    private void initialise() {
        viewModel = ViewModelProviders.of(getActivity()).get(CashSalesExistingCustomerViewModel.class);
        initialiseRecyclerView();
    }


    private void initialiseRecyclerView() {
        customersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        customersRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        customerListAdapter = new CustomerListAdapter(new ArrayList<>());
        customerListAdapter.setCustomerClickEventListener(customerClickEventListener);
        customersRecyclerView.setAdapter(customerListAdapter);
    }

    private void checkForLocationPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        onPermissionGranted();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onPermissionGranted();
                } else {
                    Toast.makeText(getContext(), getContext().getString(R.string.location_permission_denied), Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void onPermissionGranted() {
        fetchUserLocation();
    }

    private void fetchUserLocation() {
        LocationHelper locationHelper = new LocationHelper(getActivity());
        locationHelper.setLocationChangeListener(this);
        locationHelper.getLastKnownLocation(getActivity());
    }

    private UILevelNetworkCallback customerListNetworkCallback = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage, boolean toLogout) {
            ParentAppCompatActivity activity = ((ParentAppCompatActivity) getActivity());
            if (activity == null) return;

            activity.runOnUiThread(() -> {
                ((ParentAppCompatActivity) getActivity()).dismissProgressDialog();
                if(uiModels == null){
                    getActivity().runOnUiThread(() -> activity.dismissProgressDialog());
                    if(toLogout){
                        activity.logoutUser();
                    }else if(isDialogToBeShown) {
                        activity.showNetworkRelatedDialogs(errorMessage);
                    }
                } else if (uiModels.size() > 0) {
                    List<Customer> customerList = (List<Customer>) uiModels;
                    customerListAdapter.updateData(customerList);
                }
            });
        }
    };

    private void fetchCustomerList(boolean lastDeliveryDateRequired, LatLng currentLocation, UILevelNetworkCallback networkCallback) {
        ((ParentAppCompatActivity) getActivity()).showProgressDialog();
        viewModel.fetchCustomerList(lastDeliveryDateRequired, currentLocation, networkCallback);
    }

    @Override
    public void onLastKnownLocationReceived(Location location) {
        if (location != null)
            fetchCustomerList(true, new LatLng(location.getLatitude(), location.getLongitude()), customerListNetworkCallback);
        else {
            Toast.makeText(getContext(), getContext().getString(R.string.location_fetch_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLastKnownLocationError(String errorMessage) {
        Toast.makeText(getContext(), getContext().getString(R.string.location_fetch_error), Toast.LENGTH_SHORT).show();
    }
}
