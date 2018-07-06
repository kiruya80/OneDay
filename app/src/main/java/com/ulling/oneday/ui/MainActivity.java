package com.ulling.oneday.ui;

import android.content.Intent;
import android.os.Bundle;

import com.ulling.lib.core.ui.QcBaseLifeActivity;
import com.ulling.oneday.OnedayApplication;
import com.ulling.oneday.R;
import com.ulling.oneday.databinding.ActivityMainBinding;

public class MainActivity extends QcBaseLifeActivity {

    private ActivityMainBinding viewBinding;
    private OnedayApplication qApp;


    @Override
    protected int needGetLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void needInitToOnCreate() {
        qApp = OnedayApplication.getInstance();

    }

    @Override
    protected void optGetSavedInstanceState(Bundle savedInstanceState) {

    }

    @Override
    protected void optGetIntent(Intent intent) {

    }

    @Override
    protected void needResetData() {

    }

    @Override
    protected void needUIBinding() {
        viewBinding = (ActivityMainBinding) getViewDataBinding();

    }

    @Override
    protected void needUIEventListener() {

    }

    @Override
    protected void needSubscribeUiFromViewModel() {

    }

    @Override
    protected void needSubscribeUiClear() {

    }

    @Override
    protected void needOnShowToUser() {

    }

}
