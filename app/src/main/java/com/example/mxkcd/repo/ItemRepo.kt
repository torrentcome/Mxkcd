package com.example.mxkcd.repo

import com.example.mxkcd.base.DataState
import com.example.mxkcd.db.DbXkcd
import com.example.mxkcd.dto.XkcdItem
import com.example.mxkcd.net.XkcdApi
import com.example.mxkcd.net.asDbModel
import com.example.mxkcd.net.asDtoModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ItemRepo @Inject constructor(private val api: XkcdApi, private val db: DbXkcd) {
    suspend fun detail(id: Int): Flow<DataState<XkcdItem>> = flow {
        emit(DataState.Loading)
        try {
            val searchResult = api.getXkcdItem(id)
            db.xkcdDao.put(searchResult.asDbModel())
            emit(DataState.Success(searchResult.asDtoModel()))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }
}