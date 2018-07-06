package com.ulling.lib.core.db;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.ulling.lib.core.base.AppExecutors;
import com.ulling.lib.core.entities.QcBaseItem;

/**
 * 룸 데이터 베이스 생성
 * ㄴ 로컬 DB - 룸데이터베이스 사용
 * create dao
 */
public abstract class QcBaseAppDatabase extends RoomDatabase {

    private static QcBaseAppDatabase sInstance;

    public static QcBaseAppDatabase getInstance(final Context context, final AppExecutors executors) {
        if (sInstance == null) {
            synchronized (QcBaseAppDatabase.class) {
                if (sInstance == null) {
//                    sInstance = buildDatabase(context.getApplicationContext(), executors);
//                    sInstance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }
}
