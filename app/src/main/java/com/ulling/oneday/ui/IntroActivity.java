package com.ulling.oneday.ui;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.ulling.oneday.databinding.ActivityIntroBinding;

import android.os.Bundle;

import com.ulling.lib.core.ui.QcBaseLifeActivity;
import com.ulling.oneday.OnedayApplication;
import com.ulling.oneday.R;
import com.ulling.oneday.common.Define;
import com.ulling.oneday.entities.NotiOfUlling;
import com.ulling.oneday.util.FireBaseAuthUtils;
import com.ulling.oneday.util.MoveActivityUtils;

public class IntroActivity extends QcBaseLifeActivity {

    private ActivityIntroBinding viewBinding;
    private OnedayApplication qApp;

    private Handler mHandler;
    private NotiOfUlling mNotiOfUlling;

    @Override
    public void onDestroy() {
        super.onDestroy();
//
    }


    @Override
    protected int needGetLayoutId() {
        return R.layout.activity_intro;
    }

    @Override
    protected void needInitToOnCreate() {
        qApp = OnedayApplication.getInstance();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        // 익명 가입
        FireBaseAuthUtils.getInstance().getCurrentUser();

        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper(), mCallback);

        mHandler.sendEmptyMessage(Define.INIT_APP_VERSION_CHECK);
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
        viewBinding = (ActivityIntroBinding) getViewDataBinding();
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


    private Handler.Callback mCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Define.INIT_APP_VERSION_CHECK:
                    checkAppVersionCheck();
                    break;

                case Define.INIT_APP_NOTI_CHECK:
                    checkNotiUllingCheck();
                    break;

                case Define.INIT_SPLASH:
                    splash();
                    break;

                case Define.MOVE_MAIN_ACTIVITY:
                    startMain();
                    break;

            }
            return false;
        }
    };

    private void checkAppVersionCheck() {
        mHandler.sendEmptyMessage(Define.INIT_APP_NOTI_CHECK);
    }

    private void checkNotiUllingCheck() {
        mHandler.sendEmptyMessage(Define.INIT_SPLASH);
    }

    private void splash() {
        mHandler.sendEmptyMessage(Define.MOVE_MAIN_ACTIVITY);
    }

    private void startMain() {
//        FirebaseUser user = FireBaseAuthUtils.getInstance().getCurrentUser();
//        if (user != null && user.getDisplayName() != null
//                && !"".equals(user.getDisplayName().toString().trim())) {
//            MoveActivityUtils.moveMainActivity(IntroActivity.this, mNotiOfUlling);
//        } else {
//            MoveActivityUtils.moveFirstAppActivity(IntroActivity.this, mNotiOfUlling);
//        }
        MoveActivityUtils.moveMainActivity(IntroActivity.this, mNotiOfUlling);
    }
}
