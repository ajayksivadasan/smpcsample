package com.aks.smpc.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TableNew {
    @PrimaryKey(autoGenerate = true)
    int id;
    String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
