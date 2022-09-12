package com.example.mxkcd.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mxkcd.dto.XkcdItem

@Dao
interface XkcdDao {
    @Query("select * from EntityItem")
    fun getList(): LiveData<List<XkcdItem>>

    @Query("select * from EntityItem WHERE num LIKE :id")
    fun get(id: Int): LiveData<EntityItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun put(xkcdItem: EntityItem)
}

@Database(entities = [EntityItem::class], version = 1)
abstract class DbXkcd : RoomDatabase() {
    abstract val xkcdDao: XkcdDao
}