package com.ulling.lib.core.entities;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Ignore;

import com.ulling.lib.core.viewutil.adapter.QcRecyclerBaseAdapter;

/**
 * Created by P100651 on 2017-07-27.
 */
public class QcBaseLiveItem extends LiveData<QcBaseItem> {
    @Ignore
    public int type = QcRecyclerBaseAdapter.TYPE_DEFAULT;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public QcBaseLiveItem() {

    }
}