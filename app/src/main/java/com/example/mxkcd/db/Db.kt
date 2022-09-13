package com.example.mxkcd.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface XkcdDao {
    @Query("select * from EntityItem ORDER BY num ASC")
    fun getAll(): List<EntityItem>

    @Query("select * from EntityItem WHERE num LIKE :id")
    fun get(id: Int): Flow<EntityItem>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun put(xkcdItem: EntityItem)
}

@Database(entities = [EntityItem::class], version = 1)
abstract class DbXkcd : RoomDatabase() {
    abstract val xkcdDao: XkcdDao
}