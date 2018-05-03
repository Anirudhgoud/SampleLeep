package com.tracotech.tracoman.interfaces;

import com.google.android.gms.vision.barcode.Barcode;

public interface BarcodeScanListener {
    void onBarcodeScan(Barcode barcode);
    void onBarcodeScanFailure(String reason);
}
