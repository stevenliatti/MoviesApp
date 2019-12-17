package ch.hes.master.mobopproject.data

import android.graphics.Bitmap

open class Item(
    open val id: Int,
    open val nameTitle: String,
    open var img: Bitmap?,
    open val urlImg: String,
    open val knowFor: String
)

data class Movie(
    override val id: Int,
    override val nameTitle: String,
    override var img: Bitmap?,
    override val urlImg: String,
    val overview: String
) : Item(id, nameTitle, img, urlImg, "")

data class MovieDetails(
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

data class People(
    override val id: Int,
    override val nameTitle: String,
    override var img: Bitmap?,
    override val urlImg: String,
    override var knowFor: String,
    val inMovies: List<Movie>?
) : Item(id, nameTitle, img, urlImg, knowFor)

data class PeopleDetails(
    val id: Int,
    val name: String,
    val biography: String,
    val knownFor: String,
    val popularity: Double,
    val birthday: String,
    val placeOfBirth: String,
    val deathday: String,
    val gender: String,
    val homepage: String
)

data class User(val pseudo: String, val email: String, val password: String, var followed: Boolean = false)
data class Auth(val pseudo: String, val email: String, val token: String)
