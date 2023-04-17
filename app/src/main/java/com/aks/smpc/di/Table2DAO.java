package com.aks.smpc.di;

import androidx.room.Dao;
import androidx.room.Insert;

import com.aks.smpc.data.TableNew;

@Dao
public interface Table2DAO {
    @Insert
    long insert(TableNew tableNew);
}
