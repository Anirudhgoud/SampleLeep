package com.tracotech.tracoman.services.network;

/**
 * Created by kunalsingh on 09/04/17.
 */

public class NetworkService {
    private static NetworkService networkService;
    private NetworkClient networkClient;

    private NetworkService(){
        networkClient = new NetworkClient();
    }

    public static NetworkService sharedInstance(){
        if(networkService == null){
            networkService = new NetworkService();
        }
        return networkService;
    }

    public NetworkClient getNetworkClient(){
        return networkClient != null ? networkClient : new NetworkClient();
    }
}
