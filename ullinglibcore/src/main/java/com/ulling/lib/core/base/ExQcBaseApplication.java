package com.ulling.lib.core.base;

import com.ulling.lib.core.db.ExQcBaseAppDatabase;
import com.ulling.lib.core.model.ExDataModel;

public class ExQcBaseApplication extends QcBaseApplication {

    private static ExQcBaseApplication SINGLE_U;

    public static synchronized ExQcBaseApplication getInstance() {
        return SINGLE_U;
    }

    public ExQcBaseAppDatabase getDatabase() {
        return ExQcBaseAppDatabase.getInstance(this, mAppExecutors);
    }

    public ExDataModel getExDataModel() {
        return ExDataModel.getInstance(getDatabase());
    }



}
