package com.ulling.lib.core.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.ulling.lib.core.entities.QcBaseItem;

import java.util.List;

@Dao
public interface ExUserDao {
    @Query("SELECT * FROM exuser")
    LiveData<QcBaseItem> loadUser();

    @Query("SELECT * FROM exuser")
    LiveData<List<QcBaseItem>> loadUsers();

    @Query("SELECT * FROM exuser where `index` = :index")
    LiveData<List<QcBaseItem>> loadUsers(int index);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<QcBaseItem> users);
}
