package ch.hes.master.mobopproject.data

import android.graphics.Bitmap

data class Movie(val id: Int,
                 val originalTitle: String,
                 val overview: String,
                 val urlImg: String,
                 var img: Bitmap?)

data class MvDetails(val id: Int,
                 val originalTitle: String,
                 val overview: String,
                 var genresNames: ArrayList<String>?,
                 var popularity: Double?,
                 var productionCountries: ArrayList<String>?,
                 var releaseDate: String?,
                 var subtitle: String?,
                 var voteCount: Int?)