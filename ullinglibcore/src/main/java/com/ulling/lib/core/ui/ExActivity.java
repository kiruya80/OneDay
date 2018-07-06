package com.ulling.lib.core.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ulling.lib.core.base.ExQcBaseApplication;
import com.ulling.lib.core.base.QcBaseApplication;
import com.ulling.lib.core.entities.QcBaseItem;
import com.ulling.lib.core.viewmodel.ExViewModel;

import java.util.List;

/**
 * View
 * ㄴ 뷰모델 생성
 * ㄴ 뷰모델에 있는 데이터를 옵져버로 연결하여 UI를 업데이트한다
 */
public class ExActivity extends QcBaseLifeActivity {

    private ExViewModel mExViewModel;
    private ExQcBaseApplication qApp;

    @Override
    protected int needGetLayoutId() {
        return 0;
    }

    @Override
    protected void needInitToOnCreate() {
        qApp = ExQcBaseApplication.getInstance();

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
//        viewBinding = (ActivityMainBinding) getViewDataBinding();
    }

    @Override
    protected void needUIEventListener() {
    }

    @Override
    protected void needSubscribeUiFromViewModel() {
//        ExViewModel.Factory factory = new ExViewModel.Factory(
//                 getApplication(), getArguments().getInt(KEY_PRODUCT_ID));
//
//          ExViewModel model = ViewModelProviders.of(this, factory)
//                .get(ExViewModel.class);


        mExViewModel = ViewModelProviders.of(this).get(ExViewModel.class);
        mExViewModel.setExViewModel(qApp.getExDataModel(), 1);

        if (mExViewModel != null) {
            mExViewModel.setProductId(0);
            // 데이터 연결 및 데이터에 옵져버 달기
            if (!mExViewModel.getUser().hasActiveObservers())
                mExViewModel.getUser().observe(this, new Observer<QcBaseItem>() {
                    @Override
                    public void onChanged(@Nullable QcBaseItem qcBaseItem) {
                    }
                });
            if (!mExViewModel.getUsers().hasActiveObservers())
                mExViewModel.getUsers().observe(this, new Observer<List<QcBaseItem>>() {
                            @Override
                            public void onChanged(@Nullable List<QcBaseItem> qcBaseItems) {
                            }
                        }
                );
        }
    }

    @Override
    protected void needSubscribeUiClear() {
        // 옵져버 제거
        if (mExViewModel != null && mExViewModel.getUser().hasActiveObservers())
            mExViewModel.getUser().removeObservers(this);
        if (mExViewModel != null && mExViewModel.getUsers().hasActiveObservers())
            mExViewModel.getUsers().removeObservers(this);
    }

    @Override
    protected void needOnShowToUser() {

    }

    // 아답터 클릭 리스너
//    private final ProductClickCallback mProductClickCallback = new ProductClickCallback() {
//        @Override
//        public void onClick(Product product) {
//
//            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
//                ((MainActivity) getActivity()).show(product);
//            }
//        }
//    };
}
