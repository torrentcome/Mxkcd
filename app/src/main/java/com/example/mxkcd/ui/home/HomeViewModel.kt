package com.example.mxkcd.ui.home

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
class HomeViewModel @Inject constructor(private val repo: ItemRepo) : ViewModel() {
    private val _allState = MutableStateFlow<Command<List<DtoItem>>>(Command.Loading())
    val all: StateFlow<Command<List<DtoItem>>> = _allState

    suspend fun getAll() = viewModelScope.launch(Dispatchers.IO) {
        repo.getAll().onEach {
            _allState.value = it
        }.launchIn(viewModelScope)
    }
}
