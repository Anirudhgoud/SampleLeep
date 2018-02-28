package com.goleep.driverapp.leep;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.viewmodels.DropOffDeliveryOrderDetailsViewModel;

public class DropOffDeliveryOrderDetailsActivity extends AppCompatActivity {

    private DropOffDeliveryOrderDetailsViewModel viewModel;
    private RecyclerView deliveryItemsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_droff_off_delivery_order_details);
        initialise();
    }

    private void initialise(){
        viewModel = ViewModelProviders.of(this).get(DropOffDeliveryOrderDetailsViewModel.class);
        initialiseRecyclerView();
    }

    private void initialiseRecyclerView(){
        deliveryItemsRecyclerView = findViewById(R.id.delivery_order_recyclerview);

    }
}
