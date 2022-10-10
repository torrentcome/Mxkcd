package com.example.mxkcd.repo

import android.util.Log
import com.example.mxkcd.base.Command
import com.example.mxkcd.db.DbXkcd
import com.example.mxkcd.db.asDto
import com.example.mxkcd.dto.DtoItem
import com.example.mxkcd.dto.asEntityItem
import com.example.mxkcd.net.XkcdApi
import com.example.mxkcd.net.asDtoModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ItemRepo @Inject constructor(private val api: XkcdApi, private val db: DbXkcd) {
    suspend fun detail(id: Int): Flow<Command<DtoItem>> = flow {
        emit(Command.Loading("try to fetch from api"))
        try {
            val fetchResult = api.fetch(id)
            emit(Command.Success(fetchResult.asDtoModel()))
        } catch (e: Exception) {
            Log.e("e", "detail error ->", e)
            emit(Command.Error(e))
        }
    }

    fun save(dtoItem: DtoItem): Flow<Command<DtoItem>> = flow {
        emit(Command.Loading("try to save in Db"))
        try {
            db.xkcdDao.put(dtoItem.asEntityItem())
            emit(Command.Success(dtoItem))
        } catch (e: Exception) {
            Log.e("e", "saveItem error ->", e)
            emit(Command.Error(e))
        }
    }

    suspend fun getAll(): Flow<Command<List<DtoItem>>> = flow {
        emit(Command.Loading("try to select * the Db"))
        try {
            val all = db.xkcdDao.getAll()
            emit(Command.Success(all.map { it.asDto() }))
        } catch (e: Exception) {
            Log.e("e", "getAll error ->", e)
            emit(Command.Error(e))
        }
    }
}
