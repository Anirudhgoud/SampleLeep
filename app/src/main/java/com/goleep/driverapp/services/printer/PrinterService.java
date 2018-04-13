package com.goleep.driverapp.services.printer;


import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.ngx.BluetoothPrinter;

/**
 * Created by vishalm on 12/04/18.
 */
public class PrinterService {
    private static PrinterService printerService;
    private BluetoothPrinter printer;

    private PrinterService(){
        printerService = new PrinterService();
    }

    public static PrinterService sharedInstance(){
        if(printerService == null){
            printerService = new PrinterService();
        }
        return printerService;
    }

    public BluetoothPrinter getPrinter(){
        return printer != null? printer: BluetoothPrinter.INSTANCE;
    }
}
