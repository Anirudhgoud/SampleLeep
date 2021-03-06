package com.tracotech.tracoman.leep.dropoff.cashsales;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tracotech.tracoman.R;
import com.tracotech.tracoman.constants.AppConstants;
import com.tracotech.tracoman.constants.IntentConstants;
import com.tracotech.tracoman.constants.Permissions;
import com.tracotech.tracoman.helpers.uihelpers.CountryCodeHelper;
import com.tracotech.tracoman.helpers.uihelpers.LocationHelper;
import com.tracotech.tracoman.helpers.uihelpers.PermissionHelper;
import com.tracotech.tracoman.helpers.uimodels.Business;
import com.tracotech.tracoman.helpers.uimodels.Country;
import com.tracotech.tracoman.helpers.uimodels.Customer;
import com.tracotech.tracoman.helpers.uimodels.CustomerInfo;
import com.tracotech.tracoman.helpers.uimodels.Address;
import com.tracotech.tracoman.interfaces.LocationChangeListener;
import com.tracotech.tracoman.interfaces.UILevelNetworkCallback;
import com.tracotech.tracoman.leep.main.ParentAppCompatActivity;
import com.tracotech.tracoman.utils.LogUtils;
import com.tracotech.tracoman.viewmodels.dropoff.cashsales.NewCustomerViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tracotech.tracoman.constants.AppConstants.LOCATION_PERMISSION_REQUEST_CODE;

public class NewCustomerActivity extends ParentAppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, LocationChangeListener, GoogleMap.OnMarkerDragListener {

    @BindView(R.id.tv_complete_address)
    TextView tvAddress;
    @BindView(R.id.et_location)
    EditText etLocation;
    @BindView(R.id.et_postalcode)
    EditText etPostalCode;
    @BindView(R.id.et_addressline1)
    EditText etAddressLine1;
    @BindView(R.id.et_addressline2)
    EditText etAddressLine2;
    @BindView(R.id.et_city)
    EditText etCity;
    @BindView(R.id.et_state)
    EditText etState;
    @BindView(R.id.et_country)
    EditText etCountry;
    @BindView(R.id.tv_confirm)
    Button tvConfirm;

    private GoogleMap googleMap;
    private Marker marker;
    private NewCustomerViewModel viewModel;
    private PermissionHelper permissionHelper;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_new_customer);
    }

    @Override
    public void doInitialSetup() {
        viewModel = ViewModelProviders.of(NewCustomerActivity.this).get(NewCustomerViewModel.class);
        ButterKnife.bind(NewCustomerActivity.this);
        initialiseToolbar();
        initialiseMapView();
        getIntentData();
        tvConfirm.setOnClickListener(this);
    }

    private void initialiseToolbar() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.new_customer), R.drawable.ic_new_customer);
    }

    private void initialiseMapView() {
        viewModel.setCountryAttributeList(new CountryCodeHelper().getCountries(this));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(NewCustomerActivity.this);
    }


    private void getIntentData() {
        Intent intent = getIntent();
        if (intent == null) return;
        viewModel.setCustomerInfo(intent.getParcelableExtra(IntentConstants.CUSTOMER_INFO));
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId) {
            case R.id.tv_confirm:
                onConfirmButtonClick();
                break;
            case R.id.left_toolbar_button:
                finish();
                break;
        }
    }

    private void onConfirmButtonClick() {
        if (checkInputFiledVaidation()) {
            CustomerInfo customerInfo = viewModel.getCustomerInfo();
            if (customerInfo == null) return;
            if (customerInfo.getBusinessId() == 0)
                createNewCustomerCall();
            else
                createNewLocationCall(customerInfo.getBusinessId());
        }
    }

    private boolean checkInputFiledVaidation() {
        viewModel.setPostalCode(etPostalCode.getText().toString());
        String areaName = etLocation.getText().toString();
        String strPostalCode = etPostalCode.getText().toString();
        String strAddressLine1 = etAddressLine1.getText().toString();
        String strAddressLine2 = etAddressLine2.getText().toString();
        String strCity = etCity.getText().toString();
        String strState = etState.getText().toString();
        String strCountry = etCountry.getText().toString();
        viewModel.setCountryId(0);
        boolean returnValue = true;
        if (areaName.isEmpty()) {
            etLocation.setError(getResources().getString(R.string.location_field_empty));
            returnValue = false;
        }
        if (strPostalCode.isEmpty() || strPostalCode.length() < 6) {
            etPostalCode.setError(getResources().getString(R.string.postal_code_error));
            returnValue = false;
        }
        if (strAddressLine1.isEmpty()) {
            etAddressLine1.setError(getResources().getString(R.string.address_error));
            returnValue = false;
        }
        if (strAddressLine2.isEmpty()) {
            etAddressLine1.setError(getResources().getString(R.string.address_error));
            returnValue = false;
        }
        if (strCity.isEmpty()) {
            etCity.setError(getResources().getString(R.string.city_field_empty));
            returnValue = false;
        }
        if (strState.isEmpty()) {
            etState.setError(getResources().getString(R.string.state_field_empty));

        }
        if (strCountry.isEmpty()) {
            etCountry.setError(getResources().getString(R.string.country_field_empty));
            returnValue = false;
        }
        if (returnValue) {
            getCountryCode(strCountry);
            if (viewModel.getCountryId() == 0) {
                Toast.makeText(this, "please select your address on the map", Toast.LENGTH_LONG).show();
                returnValue = false;
            }
        }
        return returnValue;
    }

    private void getCountryCode(String strCountry) {
        for (Country country : viewModel.getCountryAttributeList()) {
            strCountry = strCountry.toLowerCase().trim();
            if (country.getName().toLowerCase().trim().contains(strCountry)) {
                viewModel.setCountryId(country.getId());
                break;
            }
        }
    }

    private void checkForLocationPermission() {
        permissionHelper = new PermissionHelper(this, new String[]{Permissions.FINE_LOCATION, Permissions.COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        permissionHelper.request(new PermissionHelper.PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                fetchUserLocation();
            }

            @Override
            public void onPermissionDenied() {
                Toast.makeText(NewCustomerActivity.this, getString(R.string.location_permission_denied), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void fetchUserLocation() {
        LocationHelper locationHelper = new LocationHelper(NewCustomerActivity.this);
        locationHelper.setLocationChangeListener(NewCustomerActivity.this);
        locationHelper.getLastKnownLocation(NewCustomerActivity.this);
        googleMap.setMyLocationEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionHelper != null) {
            permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        NewCustomerActivity.this.googleMap = googleMap;
        checkForLocationPermission();
        googleMap.setOnMarkerDragListener(this);
    }

    @Override
    public void onLastKnownLocationReceived(Location location) {
        if (location == null) {
            Toast.makeText(NewCustomerActivity.this, NewCustomerActivity.this.getString(R.string.location_fetch_error), Toast.LENGTH_SHORT).show();
        } else {
            runOnUiThread(() -> {
                        showProgressDialog();
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        viewModel.getAddressFromLatitudeLongitude(mapCallBack, location.getLatitude(), location.getLongitude());
                        viewModel.setLastLatLng(latLng);
                        LogUtils.debug("newCusto", latLng + "");
                        marker = googleMap.addMarker(new
                                MarkerOptions()
                                .position(latLng)
                                .title("your location").draggable(true));
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_selected));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    }
            );
        }
    }

    @Override
    public void onLastKnownLocationError(String errorMessage) {
        Toast.makeText(NewCustomerActivity.this, errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }


    @Override
    public void onMarkerDragStart(Marker marker) {
    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        showProgressDialog();
        LatLng position = marker.getPosition();
        if (position == null) return;
        viewModel.getAddressFromLatitudeLongitude(mapCallBack, position.latitude, position.longitude);
    }

    private void setUiElements(Address address) {
        marker.setTitle(address.getFormattedAddress());
        tvAddress.setText(address.getFormattedAddress());
        etPostalCode.setText(address.getPostalCode());
        etAddressLine1.setText(address.getAddressLine1());
        etAddressLine2.setText(address.getAddressLine2());
        etCity.setText(address.getCity());
        etState.setText(address.getState());
        etCountry.setText(address.getCountry());
    }

    private void createNewCustomerCall() {
        CustomerInfo customerInfo = viewModel.getCustomerInfo();
        if (customerInfo == null) return;
        showProgressDialog();
        viewModel.createNewCustomer(newCustomerCallBack, viewModel.getCountryId(),
                customerInfo.getBusinessName(),
                customerInfo.getEmail(), customerInfo.getContactName(),
                customerInfo.getContactNumber(), customerInfo.getDesignation(),
                viewModel.getPostalCode(),
                customerInfo.getBusinessTypeId(), customerInfo.getCountryDialCode(),
                etAddressLine1.getText().toString(), etAddressLine2.getText().toString(), etCity.getText().toString(), etState.getText().toString());
    }

    private void createNewLocationCall(int businessId) {
        CustomerInfo customerInfo = viewModel.getCustomerInfo();
        if (customerInfo == null) return;
        showProgressDialog();
        viewModel.createNewLocation(newLocationCallBack, etLocation.getText().toString()
                , etAddressLine1.getText().toString(), etAddressLine2.getText().toString(),
                etCity.getText().toString(), etState.getText().toString(),
                viewModel.getCountryId(), viewModel.getPostalCode(),
                etAddressLine1.getText().toString(), viewModel.getLastLatLng().latitude,
                viewModel.getLastLatLng().longitude, customerInfo.getContactName(),
                customerInfo.getDesignation(), customerInfo.getEmail(),
                customerInfo.getContactNumber(), businessId);
    }


    /*network callbacks*/
    private UILevelNetworkCallback mapCallBack = (uiModels, isDialogToBeShown, errorMessage, toLogout) -> runOnUiThread(() -> handleReportsResponse(uiModels, isDialogToBeShown, errorMessage, toLogout));

    private void handleReportsResponse(List<?> uiModels, boolean isDialogToBeShown,
                                       String errorMessage, boolean toLogout) {
        dismissProgressDialog();
        if (uiModels == null) {
            if (toLogout) {
                logoutUser();
            } else if (isDialogToBeShown) {
                showNetworkRelatedDialogs(errorMessage);
            }
        } else if (uiModels.size() > 0) {
            Address address = (Address) uiModels.get(0);
            setUiElements(address);
        }
    }

    private UILevelNetworkCallback newCustomerCallBack = (uiModels, isDialogToBeShown, errorMessage, toLogout) -> runOnUiThread(() -> handleResponseForNewCustomer(uiModels, isDialogToBeShown, errorMessage, toLogout));

    private void handleResponseForNewCustomer(List<?> uiModels, boolean isDialogToBeShown,
                                              String errorMessage, boolean toLogout) {
        dismissProgressDialog();
        if (uiModels == null) {
            if (toLogout) {
                logoutUser();
            } else if (isDialogToBeShown) {
                showNetworkRelatedDialogs(errorMessage);
            }
        } else if (uiModels.size() > 0) {
            Business business = (Business) uiModels.get(0);
            viewModel.getCustomerInfo().setBusinessId(business.getId());
            createNewLocationCall(business.getId());
        }
    }

    private UILevelNetworkCallback newLocationCallBack = (uiModels, isDialogToBeShown, errorMessage, toLogout) -> runOnUiThread(() -> handleNewLocation(uiModels, isDialogToBeShown, errorMessage, toLogout));

    private void handleNewLocation(List<?> uiModels, boolean isDialogToBeShown,
                                   String errorMessage, boolean toLogout) {
        dismissProgressDialog();
        if (uiModels == null) {
            if (toLogout) {
                logoutUser();
            } else if (isDialogToBeShown) {
                showNetworkRelatedDialogs(errorMessage);
            }
        } else if (uiModels.size() > 0) {
            List<com.tracotech.tracoman.helpers.uimodels.Location> listLocation = (List<com.tracotech.tracoman.helpers.uimodels.Location>) uiModels;
            com.tracotech.tracoman.helpers.uimodels.Location location = listLocation.get(0);
            gotoNewActivity(location);
        }
    }

    private void  gotoNewActivity(com.tracotech.tracoman.helpers.uimodels.Location location){
        Customer customer = getCustomer(location);
        Intent intent = new Intent(NewCustomerActivity.this,CashSalesSelectProductsActivity.class);
        intent.putExtra(IntentConstants.CONSUMER_LOCATION,customer);
        startActivity(intent);
        setResult(AppConstants.ACTIVITY_CLEAR_FORM);
        finish();
    }

    private Customer getCustomer(com.tracotech.tracoman.helpers.uimodels.Location location) {
        Customer customer = new Customer();
        customer.setId(location.getId());
        customer.setName(viewModel.getCustomerInfo().getBusinessName());
        customer.setBusinessId(viewModel.getCustomerInfo().getBusinessId());
        customer.setArea(etAddressLine1.getText().toString());
        return customer;
    }
}
