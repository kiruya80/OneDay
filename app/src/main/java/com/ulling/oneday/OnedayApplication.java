package com.ulling.oneday;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ulling.lib.core.base.QcBaseApplication;
import com.ulling.oneday.common.Define;
import com.ulling.oneday.util.FireBaseAuthUtils;


import io.realm.Realm;

/**
 * 두둘 어플리케이션
 */
public class OnedayApplication extends QcBaseApplication {
    private static OnedayApplication SINGLE_U;
    public static Context qCon;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseFirestore firebaseFirestore;


    public static synchronized OnedayApplication getInstance() {
        return SINGLE_U;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private synchronized void init() {
        SINGLE_U = this;
        qCon = getApplicationContext();
        APP_NAME = qCon.getResources().getString(R.string.app_name);

        FireBaseAuthUtils.getInstance().init();
        FirebaseInstanceId.getInstance().getToken();
        Realm.init(this);

//        initDatabase();
    }

    private void initDatabase() {
        if (firebaseDatabase == null)
            firebaseDatabase = FirebaseDatabase.getInstance();
        if (databaseReference == null)
            databaseReference = firebaseDatabase.getReference(Define.FIREBASE_DATA_ONEDAY_ROOT);

        getFirebaseFirestore();
    }

    public FirebaseFirestore getFirebaseFirestore() {
        if (firebaseFirestore == null)
            firebaseFirestore = FirebaseFirestore.getInstance();
        return firebaseFirestore;
    }
}
