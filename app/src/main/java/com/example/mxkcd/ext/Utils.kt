package com.example.mxkcd.ext

fun nonNegatif(e: Int): Int {
    return if (e <= 0) 0 else e
}

fun noMas(e: Int, max: Int): Int {
    return if (e >= max) nonNegatif(max) else nonNegatif(e)
}