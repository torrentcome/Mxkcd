package com.example.mxkcd.ui.detail

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mxkcd.base.Command
import com.example.mxkcd.dto.XkcdItem
import com.example.mxkcd.repo.ItemRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemDetailViewModel @Inject constructor(private val repo: ItemRepo) : ViewModel() {
    val item: MutableState<Command<XkcdItem>?> = mutableStateOf(null)

    fun itemDetail(id : Int) = viewModelScope.launch(Dispatchers.IO) {
        repo.detail(id).onEach {
            Log.d("debug", "item=$item")
            item.value = it
        }.launchIn(viewModelScope)
    }

    fun saveItemDetail(xkcdItem : XkcdItem) = viewModelScope.launch(Dispatchers.IO) {
        repo.saveItem(xkcdItem)
    }
}
