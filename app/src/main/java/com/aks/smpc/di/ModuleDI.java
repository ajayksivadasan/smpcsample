package com.aks.smpc.di;

import android.content.Context;

import com.aks.smpc.utils.RoomDBase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class ModuleDI {
    @Provides
    @Singleton
    public RoomDBase providesBaseRoomDataBase(@ApplicationContext Context context) {
        return RoomDBase.provideRoomDb(context);
    }
@Provides
    public TableDAO providesTableDAO(RoomDBase roomDBase){
        return roomDBase.providesTableDAO();
}
}
