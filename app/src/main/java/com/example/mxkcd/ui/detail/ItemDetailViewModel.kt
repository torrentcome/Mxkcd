package com.example.mxkcd.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mxkcd.base.Command
import com.example.mxkcd.dto.DtoItem
import com.example.mxkcd.repo.ItemRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemDetailViewModel @Inject constructor(private val repo: ItemRepo) : ViewModel() {
    private val _item = MutableStateFlow<Command<DtoItem>>(Command.Loading())
    val item: StateFlow<Command<DtoItem>> = _item

    fun itemDetail(id : Int) = viewModelScope.launch(Dispatchers.IO) {
        repo.detail(id).onEach {
            Log.d("debug", "item=$item")
            _item.value = it
        }.launchIn(viewModelScope)
    }

    fun saveItemDetail(xkcdItem : DtoItem) = viewModelScope.launch(Dispatchers.IO) {
        repo.save(xkcdItem)
    }
}
