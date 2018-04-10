package com.goleep.driverapp.helpers.uihelpers;

import android.util.SparseArray;

import com.goleep.driverapp.interfaces.BarcodeScanListener;
import com.google.android.gms.samples.vision.barcodereader.BarcodeGraphic;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

import xyz.belvi.mobilevisionbarcodescanner.BarcodeRetriever;

public class BarcodeScanHelper implements BarcodeRetriever {

    private BarcodeScanListener barcodeScanListener;
    private final String CAMERA_PERMISSION_DENIED = "Camera Permission denied.";

    public void setBarcodeScanListener(BarcodeScanListener barcodeScanListener) {
        this.barcodeScanListener = barcodeScanListener;
    }


    @Override
    public void onRetrieved(Barcode barcode) {
        if (barcodeScanListener != null) barcodeScanListener.onBarcodeScan(barcode);
    }

    @Override
    public void onRetrievedMultiple(Barcode closetToClick, List<BarcodeGraphic> barcode) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onRetrievedFailed(String reason) {
        if (barcodeScanListener != null) barcodeScanListener.onBarcodeScanFailure(reason);
    }

    @Override
    public void onPermissionRequestDenied() {
        if (barcodeScanListener != null) barcodeScanListener.onBarcodeScanFailure(CAMERA_PERMISSION_DENIED);
    }
}
