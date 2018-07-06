package com.ulling.lib.core.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;

import com.ulling.lib.core.entities.QcBaseItem;

public abstract class NetworkBoundResource {

    private MediatorLiveData<QcBaseItem> result = new MediatorLiveData<>();

    protected abstract void createCall();

    protected abstract void saveCallResult();

    protected abstract void shouldFetch();

    protected abstract LiveData<QcBaseItem> loadFromDb();

    protected abstract void onFetchFailed();
}
