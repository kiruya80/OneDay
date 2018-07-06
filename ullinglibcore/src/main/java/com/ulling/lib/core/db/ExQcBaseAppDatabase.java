package com.ulling.lib.core.db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.ulling.lib.core.base.AppExecutors;
import com.ulling.lib.core.db.dao.ExUserDao;
import com.ulling.lib.core.entities.QcBaseItem;

import java.util.List;

/**
 * 룸 데이터 베이스 생성
 * ㄴ 로컬 DB - 룸데이터베이스 사용
 */
public abstract class ExQcBaseAppDatabase extends RoomDatabase {

    private static ExQcBaseAppDatabase sInstance;
    public static final String DATABASE_NAME = "basic-sample-db";

    public static ExQcBaseAppDatabase getInstance(final Context context, final AppExecutors executors) {
        if (sInstance == null) {
            synchronized (ExQcBaseAppDatabase.class) {
                if (sInstance == null) {
//                    sInstance = buildDatabase(context.getApplicationContext(), executors);
//                    sInstance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    public abstract ExUserDao exUserDao();

//    public MutableLiveData<QcBaseItem> user;
//
//    public LiveData<QcBaseItem> getUser() {
//        return user;
//    }
//    public MutableLiveData<List<QcBaseItem>> users;
//
//    public LiveData<List<QcBaseItem>> getUsers() {
//        return users;
//    }
//
//    public LiveData<List<QcBaseItem>> getUsers(int index) {
//        return users;
//    }


//    private static DataRepository sInstance;
//
//    private final AppDatabase mDatabase;
//    private MediatorLiveData<List<ProductEntity>> mObservableProducts;
//
//    private DataRepository(final AppDatabase database) {
//        mDatabase = database;
//        mObservableProducts = new MediatorLiveData<>();
//
//        mObservableProducts.addSource(mDatabase.productDao().loadAllProducts(),
//                productEntities -> {
//                    if (mDatabase.getDatabaseCreated().getValue() != null) {
//                        mObservableProducts.postValue(productEntities);
//                    }
//                });
//    }
//
//    public static DataRepository getInstance(final AppDatabase database) {
//        if (sInstance == null) {
//            synchronized (DataRepository.class) {
//                if (sInstance == null) {
//                    sInstance = new DataRepository(database);
//                }
//            }
//        }
//        return sInstance;
//    }
//
//    /**
//     * Get the list of products from the database and get notified when the data changes.
//     */
//    public LiveData<List<ProductEntity>> getProducts() {
//        return mObservableProducts;
//    }

}
