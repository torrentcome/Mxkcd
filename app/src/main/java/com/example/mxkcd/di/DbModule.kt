package com.example.mxkcd.di

import android.content.Context
import androidx.room.Room
import com.example.mxkcd.db.DbXkcd
import com.example.mxkcd.db.XkcdDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DbModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): DbXkcd {
        return Room.databaseBuilder(appContext, DbXkcd::class.java, "db_xkcd")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    fun provideChannelDao(usersDatabase: DbXkcd): XkcdDao {
        return usersDatabase.xkcdDao
    }
}
