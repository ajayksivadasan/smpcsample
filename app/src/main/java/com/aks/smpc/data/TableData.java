package com.aks.smpc.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "table_data")
public class TableData {
    @PrimaryKey(autoGenerate = true)
    int id;
    String string1;
    String string2;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getString1() {
        return string1;
    }

    public void setString1(String string1) {
        this.string1 = string1;
    }

    public String getString2() {
        return string2;
    }

    public void setString2(String string2) {
        this.string2 = string2;
    }
}
