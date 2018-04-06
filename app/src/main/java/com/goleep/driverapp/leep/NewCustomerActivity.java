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
import com.goleep.driverapp.helpers.uimodels.MapAttribute;
import com.goleep.driverapp.interfaces.LocationChangeListener;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
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
    private NewCustomerViewModel newCustomerViewModel;
    private PermissionHelper permissionHelper;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_new_customer);
    }

    @Override
    public void doInitialSetup() {
        ButterKnife.bind(NewCustomerActivity.this);
        newCustomerViewModel = ViewModelProviders.of(NewCustomerActivity.this).get(NewCustomerViewModel.class);
        initialiseToolbar();
        initialiseMapView();
        CustomerInfo customerInfo = getIntent().getExtras().getParcelable(IntentConstants.CUSTOMER_INFO);
        newCustomerViewModel.setCustomerInfo(customerInfo);
        tvConfirm.setOnClickListener(this);
    }

    private void initialiseMapView() {
        showProgressDialog();
        newCustomerViewModel.getCountries(getCountriesCallBack);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(NewCustomerActivity.this);
    }

    private void initialiseToolbar() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.new_customer), R.drawable.ic_new_customer);
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
            if (newCustomerViewModel.getCustomerInfo().getBusinessId() == 0)
                createNewCustomerCall();
            else
                createNewLocationCall(newCustomerViewModel.getCustomerInfo().getBusinessId());

    }

    private boolean checkInputFiledVaidation() {
        newCustomerViewModel.setPostalCode(etPostalCode.getText().toString());
        String areaName = etLocation.getText().toString();
        if (areaName.length() == 0) {
            etLocation.setError("location name could not be empty");
            return false;
        } else if (newCustomerViewModel.getPostalCode().length() == 0) {
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
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    @Override
    public void onLastKnownLocationReceived(Location location) {
        googleMap.setOnMarkerDragListener(this);
        if (location == null) {
            Toast.makeText(NewCustomerActivity.this, NewCustomerActivity.this.getString(R.string.location_fetch_error), Toast.LENGTH_SHORT).show();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    getAddressFromLatLng(location.getLatitude(), location.getLongitude());
                    newCustomerViewModel.setLatitude(location.getLatitude());
                    newCustomerViewModel.setLongitude(location.getLongitude());
                    LogUtils.debug("newCusto", latLng + "");
                    marker = googleMap.addMarker(new
                            MarkerOptions()
                            .position(latLng)
                            .title("your location").draggable(true));
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_selected));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                }
            });
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

    private void getAddressFromLatLng(double latitude, double longitude) {
        newCustomerViewModel.getAddressFromLatitudeLongitude(mapCallBack, String.valueOf(latitude), String.valueOf(longitude));
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
        getAddressFromLatLng(marker.getPosition().latitude, marker.getPosition().longitude);
    }

    private void setUiElements(MapAttribute mapAttribute) {
        marker.setTitle(mapAttribute.getTotalAddress());
        tvAddress.setText(mapAttribute.getTotalAddress());
        etPostalCode.setText(mapAttribute.getPostalCode());
        etAddressLine1.setText(mapAttribute.getAddressLine1());
        etAddressLine2.setText(mapAttribute.getAddressLine2());
        etCity.setText(mapAttribute.getCity());
        etState.setText(mapAttribute.getState());
        etCountry.setText(mapAttribute.getCountry());
    }

    private void createNewCustomerCall() {
        showProgressDialog();
        newCustomerViewModel.createNewCustomer(newCustomerCallBack, newCustomerViewModel.getCountry_id(),
                newCustomerViewModel.getCustomerInfo().getBusinessName(),
                newCustomerViewModel.getCustomerInfo().getEmail(), newCustomerViewModel.getCustomerInfo().getContactName(),
                newCustomerViewModel.getCustomerInfo().getContactNumber(), newCustomerViewModel.getCustomerInfo().getDesignation(),
                newCustomerViewModel.getPostalCode(),
                newCustomerViewModel.getCustomerInfo().getBusinessTypeId());
    }

    private void createNewLocationCall(int businessId) {
        showProgressDialog();
        newCustomerViewModel.createNewLocation(newLocationCallBack, etLocation.getText().toString()
                , etAddressLine1.getText().toString(), etAddressLine2.getText().toString(),
                etCity.getText().toString(), etState.getText().toString(),
                newCustomerViewModel.getCountry_id(), newCustomerViewModel.getPostalCode(),
                etAddressLine1.getText().toString(), newCustomerViewModel.getLatitude(),
                newCustomerViewModel.getLongitude(), newCustomerViewModel.getCustomerInfo().getContactName(),
                newCustomerViewModel.getCustomerInfo().getDesignation(), newCustomerViewModel.getCustomerInfo().getEmail(),
                newCustomerViewModel.getCustomerInfo().getContactNumber(), businessId);
    }


    /*network callbacks*/
    private UILevelNetworkCallback mapCallBack = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown,
                                       String errorMessage, boolean toLogout) {
            dismissProgressDialog();
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
            runOnUiThread(() -> {
                MapAttribute mapAttribute = (MapAttribute) uiModels.get(0);
                setUiElements(mapAttribute);
                for (Country country : newCustomerViewModel.getCountryAttributeList()) {
                    String countryName = mapAttribute.getCountry().toLowerCase().trim();
                    if (country.getName().toLowerCase().trim().contains(countryName)) {
                        Toast.makeText(this, country.getName() + " " + country.getId(), Toast.LENGTH_LONG).show();
                        newCustomerViewModel.setCountry_id(country.getId());
                        break;
                    }
                }
            });
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
                newCustomerViewModel.getCustomerInfo().setBusinessId(business.getId());
                createNewLocationCall(business.getId());
        }
    }

    private UILevelNetworkCallback getCountriesCallBack = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown,
                                       String errorMessage, boolean toLogout) {
            runOnUiThread(() -> handleCountries(uiModels, isDialogToBeShown, errorMessage, toLogout));
        }
    };

    private void handleCountries(List<?> uiModels, boolean isDialogToBeShown,
                                 String errorMessage, boolean toLogout) {
        dismissProgressDialog();
        if (uiModels == null) {
            if (toLogout) {
                logoutUser();
            } else if (isDialogToBeShown) {
                showNetworkRelatedDialogs(errorMessage);
            }
        } else if (uiModels.size() > 0) {
                List<Country> listCountryAttributeList = (List<Country>) uiModels;
                newCustomerViewModel.setCountryAttributeList(listCountryAttributeList);
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
                List<com.goleep.driverapp.helpers.uimodels.Location> listLocation = (List<com.goleep.driverapp.helpers.uimodels.Location>) uiModels;
                com.goleep.driverapp.helpers.uimodels.Location location = listLocation.get(0);
                Toast.makeText(this, "Location created " + location.getId(), Toast.LENGTH_LONG).show();
        }
    }

}
