package com.goleep.driverapp.leep;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.widget.Toast;

import com.goleep.driverapp.R;
import com.goleep.driverapp.constants.IntentConstants;
import com.goleep.driverapp.constants.Permissions;
import com.goleep.driverapp.helpers.customfont.CustomEditText;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.helpers.uihelpers.LocationHelper;
import com.goleep.driverapp.helpers.uihelpers.PermissionHelper;
import com.goleep.driverapp.helpers.uimodels.Business;
import com.goleep.driverapp.helpers.uimodels.Country;
import com.goleep.driverapp.helpers.uimodels.CustomerInfo;
import com.goleep.driverapp.helpers.uimodels.MapData;
import com.goleep.driverapp.interfaces.LocationChangeListener;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.jsonparsers.CountryDataParser;
import com.goleep.driverapp.utils.LogUtils;
import com.goleep.driverapp.viewmodels.NewCustomerViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.goleep.driverapp.constants.AppConstants.LOCATION_PERMISSION_REQUEST_CODE;

public class NewCustomerActivity extends ParentAppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, LocationChangeListener, GoogleMap.OnMarkerDragListener {
    @BindView(R.id.tv_complete_address)
    CustomTextView tvAddress;
    @BindView(R.id.et_location)
    CustomEditText etLocation;
    @BindView(R.id.et_postalcode)
    CustomEditText etPostalCode;
    @BindView(R.id.et_addressline1)
    CustomEditText etAddressLine1;
    @BindView(R.id.et_addressline2)
    CustomEditText etAddressLine2;
    @BindView(R.id.et_city)
    CustomEditText etCity;
    @BindView(R.id.et_state)
    CustomEditText etState;
    @BindView(R.id.et_country)
    CustomEditText etCountry;
    @BindView(R.id.tv_confirm)
    CustomTextView tvConfirm;

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
        getCustomerInfo();
        tvConfirm.setOnClickListener(this);
    }

    private void initialiseToolbar() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.new_customer), R.drawable.ic_new_customer);
    }

    private void initialiseMapView() {
        viewModel.setCountryAttributeList(getCountries());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(NewCustomerActivity.this);
    }


    private void getCustomerInfo() {
        CustomerInfo customerInfo = null;
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        customerInfo = bundle.getParcelable(IntentConstants.CUSTOMER_INFO);
        viewModel.setCustomerInfo(customerInfo);
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId) {
            case R.id.tv_confirm:
                actionOnConfirmButtonClick();
                break;
            case R.id.left_toolbar_button:
                finish();
                break;
        }
    }

    private void actionOnConfirmButtonClick() {
        if (checkInputFiledVaidation())
            if (viewModel.getCustomerInfo().getBusinessId() == 0)
                createNewCustomerCall();
            else
                createNewLocationCall(viewModel.getCustomerInfo().getBusinessId());
    }

    private boolean checkInputFiledVaidation() {
        viewModel.setPostalCode(etPostalCode.getText().toString());
        String areaName = etLocation.getText().toString();
        if (areaName.isEmpty()) {
            etLocation.setError("location name could not be empty");
            return false;
        } else if (viewModel.getPostalCode().length() == 0) {
            etPostalCode.setError("postalcode could not be empty");
            return false;
        }
        return true;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkForLocationPermission();
        else
            fetchUserLocation();
        googleMap.setOnMarkerDragListener(this);
    }

    @Override
    public void onLastKnownLocationReceived(Location location) {
        if (location == null) {
            Toast.makeText(NewCustomerActivity.this, NewCustomerActivity.this.getString(R.string.location_fetch_error), Toast.LENGTH_SHORT).show();
        } else {
            runOnUiThread(() -> {

                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        getAddressFromLatLng(location.getLatitude(), location.getLongitude());
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
        LogUtils.debug("new Cust", marker.getPosition().latitude + " " + marker.getPosition().longitude);
        showProgressDialog();
        getAddressFromLatLng(marker.getPosition().latitude, marker.getPosition().longitude);
    }

    private void getAddressFromLatLng(double latitude, double longitude) {
        viewModel.getAddressFromLatitudeLongitude(mapCallBack, String.valueOf(latitude), String.valueOf(longitude));
    }

    private void setUiElements(MapData mapData) {
        marker.setTitle(mapData.getTotalAddress());
        tvAddress.setText(mapData.getTotalAddress());
        etPostalCode.setText(mapData.getPostalCode());
        etAddressLine1.setText(mapData.getAddressLine1());
        etAddressLine2.setText(mapData.getAddressLine2());
        etCity.setText(mapData.getCity());
        etState.setText(mapData.getState());
        etCountry.setText(mapData.getCountry());
    }

    private void createNewCustomerCall() {
        showProgressDialog();
        viewModel.createNewCustomer(newCustomerCallBack, viewModel.getCountry_id(),
                viewModel.getCustomerInfo().getBusinessName(),
                viewModel.getCustomerInfo().getEmail(), viewModel.getCustomerInfo().getContactName(),
                viewModel.getCustomerInfo().getContactNumber(), viewModel.getCustomerInfo().getDesignation(),
                viewModel.getPostalCode(),
                viewModel.getCustomerInfo().getBusinessTypeId());
    }

    private void createNewLocationCall(int businessId) {
        showProgressDialog();
        viewModel.createNewLocation(newLocationCallBack, etLocation.getText().toString()
                , etAddressLine1.getText().toString(), etAddressLine2.getText().toString(),
                etCity.getText().toString(), etState.getText().toString(),
                viewModel.getCountry_id(), viewModel.getPostalCode(),
                etAddressLine1.getText().toString(), viewModel.getLastLatLng().latitude,
                viewModel.getLastLatLng().longitude, viewModel.getCustomerInfo().getContactName(),
                viewModel.getCustomerInfo().getDesignation(), viewModel.getCustomerInfo().getEmail(),
                viewModel.getCustomerInfo().getContactNumber(), businessId);
    }

    private List<Country> getCountries() {
        String json = null;
        try (InputStream inputStream = getAssets().open("country.json");
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader);) {
            StringBuilder stringBuilder = new StringBuilder();
            String content;
            while ((content = bufferedReader.readLine()) != null) {
                stringBuilder.append(content);
            }
            json = stringBuilder.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return new CountryDataParser().reportsDataByParsingJsonResponse(jsonObject);
    }

    /*network callbacks*/
    private UILevelNetworkCallback mapCallBack = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown,
                                       String errorMessage, boolean toLogout) {
            runOnUiThread(() -> handleReportsResponse(uiModels, isDialogToBeShown, errorMessage, toLogout));
        }
    };

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
            MapData mapData = (MapData) uiModels.get(0);
            setUiElements(mapData);
            for (Country country : viewModel.getCountryAttributeList()) {
                String countryName = mapData.getCountry().toLowerCase().trim();
                if (country.getName().toLowerCase().trim().contains(countryName)) {
                    Toast.makeText(this, country.getName() + " " + country.getId(), Toast.LENGTH_LONG).show();
                    viewModel.setCountry_id(country.getId());
                    break;
                }
            }
        }
    }

    private UILevelNetworkCallback newCustomerCallBack = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown,
                                       String errorMessage, boolean toLogout) {
            runOnUiThread(() -> handleResponseForNewCustomer(uiModels, isDialogToBeShown, errorMessage, toLogout));
        }
    };

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
            Toast.makeText(this, "Customer Ctreated" + business.getId() + "", Toast.LENGTH_LONG).show();
            viewModel.getCustomerInfo().setBusinessId(business.getId());
            createNewLocationCall(business.getId());
        }
    }

    private UILevelNetworkCallback newLocationCallBack = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown,
                                       String errorMessage, boolean toLogout) {
            runOnUiThread(() -> handleNewLocation(uiModels, isDialogToBeShown, errorMessage, toLogout));
        }
    };

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
            @SuppressWarnings("unchecked")
            List<com.goleep.driverapp.helpers.uimodels.Location> listLocation = (List<com.goleep.driverapp.helpers.uimodels.Location>) uiModels;
            com.goleep.driverapp.helpers.uimodels.Location location = listLocation.get(0);
            Toast.makeText(this, "Location created " + location.getId(), Toast.LENGTH_LONG).show();
        }
    }
}
