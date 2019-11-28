package ch.hes.master.mobopproject.data

import android.graphics.Bitmap

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val urlImg: String,
    var img: Bitmap?
)

data class MvDetails(
    val id: Int,
    val title: String,
    val overview: String,
    var genresNames: ArrayList<String>,
    var popularity: Double,
    var productionCountries: ArrayList<String>,
    var releaseDate: String,
    var subtitle: String,
    var voteCount: Int
)

data class MovieYoutubeVideo(val key: String, val name: String, val type: String)

data class Cast(val name: String, val function: String) {
    override fun toString(): String {
        return name + " (" + function + ")"
    }
}
