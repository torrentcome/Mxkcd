package com.example.mxkcd.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mxkcd.base.DataState
import com.example.mxkcd.dto.XkcdItem
import com.example.mxkcd.repo.ItemRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: ItemRepo) : ViewModel() {
    val all: MutableState<DataState<List<XkcdItem>>?> = mutableStateOf(null)

    suspend fun getAll() = viewModelScope.launch(Dispatchers.IO) {
        val all1 = repo.getAll()
        all1.onEach {
            all.value = it
        }.launchIn(viewModelScope)
    }
}
