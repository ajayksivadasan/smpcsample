package com.aks.smpc.utils;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.aks.smpc.data.TableData;
import com.aks.smpc.data.TableNew;
import com.aks.smpc.di.Table2DAO;
import com.aks.smpc.di.TableDAO;

@Database(entities = {TableData.class, TableNew.class

}, version = 2)
public abstract class RoomDBase extends RoomDatabase {
    public static RoomDBase provideRoomDb(Context context) {
        return Room
                .databaseBuilder(context.getApplicationContext(), RoomDBase.class, "users_database")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }
    public abstract TableDAO providesTableDAO();
    public abstract Table2DAO providesTable2DAO();
}
