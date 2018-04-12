package com.goleep.driverapp.leep;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.adapters.ReturnReasonListAdapter;
import com.goleep.driverapp.helpers.uimodels.ReturnReason;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.viewmodels.ReturnReasonsViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReturnsSelectReasonActivity extends ParentAppCompatActivity {

    @BindView(R.id.reasons_rv)
    RecyclerView stocksRecyclerView;

    private ReturnReasonsViewModel returnReasonsViewModel;

    private ReturnReasonListAdapter adapter;

    private UILevelNetworkCallback returnReasonsCallback = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown,
                                       String errorMessage, boolean toLogout) {
            if(uiModels != null && uiModels.size() > 0){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.updateList((List<ReturnReason>) uiModels);
                    }
                });
            }
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_returns_select_reason);
    }

    @Override
    public void doInitialSetup() {
        ButterKnife.bind(this);
        initView();
        initRecyclerView();
        fetchReturnReasons();
    }

    private void initView() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.returns), R.drawable.ic_returns_title_icon);
        returnReasonsViewModel = ViewModelProviders.of(this).get(ReturnReasonsViewModel.class);
    }

    private void fetchReturnReasons() {
        returnReasonsViewModel.fetchReturnReasons(returnReasonsCallback);
    }

    private void initRecyclerView(){
        stocksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stocksRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        adapter = new ReturnReasonListAdapter(new ArrayList<>());
        stocksRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId){
            case R.id.left_toolbar_button:
                finish();
                break;
        }
    }
}
