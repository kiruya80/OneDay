package com.ulling.lib.core.entities;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.ulling.lib.core.model.ExUser;

@Entity(tableName = "exuser")
public class ExUserEntity implements ExUser {
    @PrimaryKey
    private int index;

    @PrimaryKey
    private int id;
    private String name;

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public ExUserEntity(int index, int id, String name) {
        this.index = index;
        this.id = id;
        this.name = name;
    }

    public ExUserEntity(ExUserEntity exUserEntity) {
        this.index = exUserEntity.getIndex();
        this.id = exUserEntity.getId();
        this.name = exUserEntity.getName();
    }
}
