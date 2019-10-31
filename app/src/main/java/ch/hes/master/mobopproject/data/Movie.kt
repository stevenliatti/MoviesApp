package ch.hes.master.mobopproject.data

import android.graphics.Bitmap

data class Movie(val id: Int, val originalTitle: String, val overview: String, var img: Bitmap?)
