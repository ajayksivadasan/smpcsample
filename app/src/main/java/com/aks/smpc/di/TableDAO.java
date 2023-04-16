package com.aks.smpc.di;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.aks.smpc.data.TableData;

import java.util.List;

@Dao
public interface TableDAO {
    @Query("select * from table_data")
    List<TableData> getAllTAbleDAta();
    @Insert
    void insertData(TableData tableData);
}
