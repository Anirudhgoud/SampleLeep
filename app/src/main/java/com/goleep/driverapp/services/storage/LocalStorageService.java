package com.goleep.driverapp.services.storage;

/**
 * Created by kunalsingh on 10/04/17.
 */

public class LocalStorageService {
    private static LocalStorageService localStorageService;
    private LocalFileStore localFileStore;

    private LocalStorageService(){
        localFileStore = new LocalFileStore();
    }

    public static LocalStorageService sharedInstance(){
        if(localStorageService == null){
            localStorageService = new LocalStorageService();
        }
        return localStorageService;
    }

    public LocalFileStore getLocalFileStore(){
        return localFileStore != null? localFileStore: new LocalFileStore();
    }
}
