package com.example.mxkcd.base

/**
 * Data state for processing api response Loading, Success and Error
 */
sealed class Command<out R> {
    data class Success<out T>(val data: T) : Command<T>()
    data class Error(val exception: Exception) : Command<Nothing>()
    object Loading : Command<Nothing>()
}
