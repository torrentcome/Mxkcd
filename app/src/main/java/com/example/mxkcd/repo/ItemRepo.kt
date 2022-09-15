package com.example.mxkcd.repo

import android.util.Log
import com.example.mxkcd.base.DataState
import com.example.mxkcd.db.DbXkcd
import com.example.mxkcd.db.asDto
import com.example.mxkcd.dto.XkcdItem
import com.example.mxkcd.dto.asDbModel
import com.example.mxkcd.net.XkcdApi
import com.example.mxkcd.net.asDtoModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ItemRepo @Inject constructor(private val api: XkcdApi, private val db: DbXkcd) {
    suspend fun detail(id: Int): Flow<DataState<XkcdItem>> = flow {
        emit(DataState.Loading)
        try {
            val searchResult = api.getXkcdItem(id)
            emit(DataState.Success(searchResult.asDtoModel()))
        } catch (e: Exception) {
            Log.e("e", "detail error ->", e)
            emit(DataState.Error(e))
        }
    }

    suspend fun saveItem(xkcdItem: XkcdItem) {
        try {
            db.xkcdDao.put(xkcdItem.asDbModel())
        } catch (e: Exception) {
            Log.e("e", "saveItem error ->", e)
        }
    }

    suspend fun getAll(): Flow<DataState<List<XkcdItem>>> = flow {
        emit(DataState.Loading)
        try {
            val all = db.xkcdDao.getAll()
            emit(DataState.Success(all.map { it.asDto() }))
        } catch (e: Exception) {
            Log.e("e", "getAll error ->", e)
            emit(DataState.Error(e))
        }
    }
}
