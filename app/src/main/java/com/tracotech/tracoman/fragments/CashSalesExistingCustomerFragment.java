package com.tracotech.tracoman.fragments;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.tracotech.tracoman.R;
import com.tracotech.tracoman.adapters.CustomerListAdapter;
import com.tracotech.tracoman.adapters.CustomerSearchArrayAdapter;
import com.tracotech.tracoman.constants.AppConstants;
import com.tracotech.tracoman.constants.IntentConstants;
import com.tracotech.tracoman.constants.Permissions;
import com.tracotech.tracoman.helpers.customviews.CustomAppCompatAutoCompleteTextView;
import com.tracotech.tracoman.helpers.uihelpers.EditTextHelper;
import com.tracotech.tracoman.helpers.uihelpers.LocationHelper;
import com.tracotech.tracoman.helpers.uihelpers.PermissionHelper;
import com.tracotech.tracoman.helpers.uimodels.Customer;
import com.tracotech.tracoman.interfaces.CustomerClickEventListener;
import com.tracotech.tracoman.interfaces.LocationChangeListener;
import com.tracotech.tracoman.interfaces.UILevelNetworkCallback;
import com.tracotech.tracoman.leep.dropoff.cashsales.CashSalesActivity;
import com.tracotech.tracoman.leep.dropoff.cashsales.CashSalesSelectProductsActivity;
import com.tracotech.tracoman.leep.main.ParentAppCompatActivity;
import com.tracotech.tracoman.leep.pickup.returns.ReturnsCustomerSelectActivity;
import com.tracotech.tracoman.leep.pickup.returns.ReturnsSelectProductActivity;
import com.tracotech.tracoman.viewmodels.dropoff.cashsales.CashSalesExistingCustomerViewModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tracotech.tracoman.constants.AppConstants.LOCATION_PERMISSION_REQUEST_CODE;

/**
 * Created by anurag on 19/03/18.
 */

public class CashSalesExistingCustomerFragment extends Fragment implements LocationChangeListener, AdapterView.OnItemClickListener {

    @BindView(R.id.customer_recycler_view)
    RecyclerView customersRecyclerView;
    @BindView(R.id.atv_search)
    CustomAppCompatAutoCompleteTextView atvSearch;

    private CashSalesExistingCustomerViewModel viewModel;
    private CustomerListAdapter customerListAdapter;
    private CustomerSearchArrayAdapter customerSearchListAdapter;
    private PermissionHelper permissionHelper;

    private CustomerClickEventListener customerClickEventListener = this::gotoNextScreen;

    public CashSalesExistingCustomerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cash_sales_existing_customer, container, false);
        ButterKnife.bind(this, view);
        initialise();
        checkForLocationPermission();
        return view;
    }

    private void initialise() {
        FragmentActivity activity = getActivity();
        if (activity == null) return;
        viewModel = ViewModelProviders.of(activity).get(CashSalesExistingCustomerViewModel.class);
        initialiseRecyclerView();
        initialiseAutoCompleteTextView();
    }


    private void initialiseRecyclerView() {
        customersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        customersRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        customerListAdapter = new CustomerListAdapter(new ArrayList<>());
        customerListAdapter.setCustomerClickEventListener(customerClickEventListener);
        customersRecyclerView.setAdapter(customerListAdapter);
    }

    private void initialiseAutoCompleteTextView() {
        Drawable rightDrawable = AppCompatResources.getDrawable(getContext(), R.drawable.ic_search_inactive);
        atvSearch.setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrawable, null);
        customerSearchListAdapter = new CustomerSearchArrayAdapter(getContext(), new ArrayList());
        atvSearch.setAdapter(customerSearchListAdapter);
        atvSearch.setOnItemClickListener(this);
        editTextHelper.attachTextChangedListener(atvSearch);
    }

    private EditTextHelper editTextHelper = new EditTextHelper(editable -> {
        if (editable.length() > 2) fetchCustomerList(false, false, null, editable.toString(), CashSalesExistingCustomerFragment.this.customerSuggestionsNetworkCallback);
    });

    private void checkForLocationPermission() {
        permissionHelper = new PermissionHelper(this, new String[]{Permissions.FINE_LOCATION, Permissions.COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        permissionHelper.request(new PermissionHelper.PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                fetchUserLocation();
            }

            @Override
            public void onPermissionDenied() {
                Toast.makeText(getContext(), getContext().getString(R.string.location_permission_denied), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionHelper != null) {
            permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void fetchUserLocation() {
        Activity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            LocationHelper locationHelper = new LocationHelper(activity);
            locationHelper.setLocationChangeListener(this);
            locationHelper.getLastKnownLocation(activity);
        }
    }

    private UILevelNetworkCallback customerListNetworkCallback = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage, boolean toLogout) {
            Activity activity = getActivity();
            if (activity == null || activity.isFinishing()) return;

            ParentAppCompatActivity parentActivity = ((ParentAppCompatActivity) activity);
            activity.runOnUiThread(() -> {
                parentActivity.dismissProgressDialog();
                if (uiModels == null) {
                    if (toLogout) {
                        parentActivity.logoutUser();
                    } else if (isDialogToBeShown) {
                        parentActivity.showNetworkRelatedDialogs(errorMessage);
                    }
                } else if (uiModels.size() > 0) {
                    List<Customer> customerList = (List<Customer>) uiModels;
                    customerListAdapter.updateData(customerList);
                }
            });
        }
    };

    private UILevelNetworkCallback customerSuggestionsNetworkCallback = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage, boolean toLogout) {
            Activity activity = getActivity();
            if (activity == null || activity.isFinishing()) return;

            ParentAppCompatActivity parentActivity = ((ParentAppCompatActivity) activity);
            activity.runOnUiThread(() -> {
                parentActivity.dismissProgressDialog();
                if (uiModels == null) {
                    if (toLogout) {
                        parentActivity.logoutUser();
                    }
                } else if (uiModels.size() > 0) {
                    List<Customer> customerList = (List<Customer>) uiModels;
                    customerSearchListAdapter.updateData(customerList);
                }
            });
        }
    };

    private void fetchCustomerList(boolean showLoading, boolean lastDeliveryDateRequired, LatLng currentLocation, String searchText, UILevelNetworkCallback networkCallback) {
        if (showLoading) {
            Activity activity = getActivity();
            if (activity == null || activity.isFinishing()) return;
            ParentAppCompatActivity parentActivity = ((ParentAppCompatActivity) activity);
            parentActivity.showProgressDialog();
        }
        viewModel.fetchCustomerList(lastDeliveryDateRequired, currentLocation, searchText, networkCallback);
    }

    @Override
    public void onLastKnownLocationReceived(Location location) {
        if (location != null)
            fetchCustomerList(true, true, new LatLng(location.getLatitude(), location.getLongitude()), null, customerListNetworkCallback);
        else {
            Toast.makeText(getContext(), getContext().getString(R.string.location_fetch_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLastKnownLocationError(String errorMessage) {
        Toast.makeText(getContext(), getContext().getString(R.string.location_fetch_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        gotoNextScreen((Customer) parent.getAdapter().getItem(position));
    }

    private void gotoNextScreen(Customer customer) {
        FragmentActivity activity = getActivity();
        if(activity != null && !activity.isFinishing()){
            if(activity instanceof CashSalesActivity) {
                gotoSelectProductActivity(activity, customer, AppConstants.CASH_SALES_FLOW);
            } else if(activity instanceof ReturnsCustomerSelectActivity) {
                gotoSelectProductActivity(activity, customer, AppConstants.RETURNS_FLOW) ;
            }
        }
    }

    private void gotoSelectProductActivity(FragmentActivity activity, Customer customer, int flow) {
        if (customer == null) return;
        Intent intent = null;
        switch (flow){
            case AppConstants.CASH_SALES_FLOW:
                intent = new Intent(activity, CashSalesSelectProductsActivity.class);
                break;
            case AppConstants.RETURNS_FLOW:
                intent = new Intent(activity, ReturnsSelectProductActivity.class);
                break;
        }
        if (intent == null) return;
        intent.putExtra(IntentConstants.FLOW, flow);
        intent.putExtra(IntentConstants.CONSUMER_LOCATION, customer);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        permissionHelper = null;
        super.onDestroy();
    }
}
