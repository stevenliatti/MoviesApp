package ch.hes.master.mobopproject

import android.content.ActivityNotFoundException
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.gridlayout.widget.GridLayout
import androidx.lifecycle.ViewModelProviders
import ch.hes.master.mobopproject.data.Constants
import ch.hes.master.mobopproject.data.Cast
import ch.hes.master.mobopproject.data.MovieYoutubeVideo
import ch.hes.master.mobopproject.data.MvDetails
import com.google.android.youtube.player.YouTubeStandalonePlayer
import org.json.JSONArray
import org.json.JSONObject


class MovieDetails : Fragment() {

    private val apiKey = Constants.tmdbApiKey
    private val requestController = VolleyRequestController()

    private lateinit var dummyContext: Context

    private var movieId : Int? = null
    private var urlImg : String? = null
    private var movieDetails : MvDetails? = null

    private lateinit var titleView: TextView
    private lateinit var descriptionView: TextView
    private lateinit var castView: TextView
    private lateinit var crewView: TextView
    private lateinit var imageView: ImageView
    private lateinit var genreNamesView: LinearLayout
    private lateinit var popularityView: TextView
    private lateinit var prodCountriesView: LinearLayout
    private lateinit var releaseDateView: TextView
    private lateinit var subtitleView: TextView
    private lateinit var voteCountView: TextView

    private lateinit var similarMoviesGridView: GridLayout
    private lateinit var videosView: LinearLayout

    companion object {
        @JvmStatic
        fun newInstance(id: Int?, urlImg: String?): MovieDetails {
            return MovieDetails().apply {
                this.movieId = id
                this.urlImg = urlImg
            }
        }
    }

    private fun getMoreDetails(context: Context) {
        val url = "https://api.themoviedb.org/3/movie/${this.movieId}?api_key=$apiKey"

        requestController.VolleyRequest(url, context, object : ServerCallback<JSONObject> {
            override fun onSuccess(res: JSONObject) {

                val genresNames = buildStringListFromJsonSArray(res.getJSONArray("genres"), "name")
                val popularity = res.getDouble("popularity")
                val productionCountries = buildStringListFromJsonSArray(res.getJSONArray("production_countries"), "name")
                val releaseDate = res.getString("release_date")
                val subtitle = res.getString("tagline")
                val voteCount = res.getInt("vote_count")

                movieDetails = MvDetails(
                    res.getInt("id"),
                    res.getString("title"),
                    res.getString("overview"),
                    genresNames,
                    popularity,
                    productionCountries,
                    releaseDate,
                    subtitle,
                    voteCount
                )

                // TODO: use ViewModel instead

                titleView.setText(movieDetails?.title)
                descriptionView.setText(movieDetails?.overview)

                for (genre in genresNames) {
                    val genreView = TextView(view?.context)
                    genreView.text = genre
                    genreNamesView.addView(genreView)
                }

                popularityView.setText("Popularity : " + popularity.toString())

                for (prodCount in productionCountries) {
                    val prodCountView = TextView(view?.context)
                    prodCountView.text = prodCount
                    prodCountriesView.addView(prodCountView)
                }

                releaseDateView.setText(releaseDate)
                subtitleView.setText(subtitle)
                voteCountView.setText("Vote count : " + voteCount.toString())

            }
        })
    }

    private fun getCredits(castNb: Int, keysCrew: List<String>, context: Context) {
        val url = "https://api.themoviedb.org/3/movie/${this.movieId}/credits?api_key=$apiKey"

        requestController.VolleyRequest(url, context, object : ServerCallback<JSONObject> {
            override fun onSuccess(res: JSONObject) {
                val cast = creditsFromJsonArray(res.getJSONArray("cast"), "character")
                val crew = creditsFromJsonArray(res.getJSONArray("crew"), "job")
                var castString = ""
                for (i in 0..castNb) {
                    if (i < cast.size) castString += cast[i].toString() + ", "
                }
                castString = castString.subSequence(0, castString.length - 2).toString()
                castView.text = castString

                var crewString = ""
                for (c in crew) {
                    if (keysCrew.contains(c.function)) crewString += c.toString() + ", "
                }
                crewString = crewString.subSequence(0, crewString.length - 2).toString()
                crewView.text = crewString
            }
        })
    }

    private fun getSimilarMovies(context: Context) {
        val url = "https://api.themoviedb.org/3/movie/${this.movieId}/similar?api_key=$apiKey"

        requestController.VolleyRequest(url, context, object : ServerCallback<JSONObject> {
            override fun onSuccess(res: JSONObject) {

                val similarMovies = buildStringListFromJsonSArray(res.getJSONArray("results"), "title")
                val total = similarMovies.size
                val columnsNumber = 3
                var row = 0
                var col = 0

                similarMoviesGridView.setColumnCount(columnsNumber)
                similarMoviesGridView.setRowCount(total / columnsNumber)
                var idx = 0
                for (movie in similarMovies) {
                    if (col == columnsNumber) {
                        col = 0
                        row++
                    }

                    var rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 1)
                    var colspan = GridLayout.spec(GridLayout.UNDEFINED, 1)
                    if (row == 0 && col == 0) {
                        Log.e("", "spec")
                        colspan = GridLayout.spec(GridLayout.UNDEFINED, 2)
                        rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 2)
                    }

                    val gridParam: GridLayout.LayoutParams =
                        GridLayout.LayoutParams(rowSpan, colspan)

                    val movieView = TextView(view?.context)
                    movieView.text = movie

                    /******* inflate *************/
                    var inflater: LayoutInflater = LayoutInflater.from(context)

                    //to get the MainLayout
                    val view: View = inflater.inflate(R.layout.movie_details_fragment, null)

                    val inflatedLayout: View =
                        inflater.inflate(R.layout.fragment_movie, view as ViewGroup, false)

                    inflatedLayout.setLayoutParams(
                        LinearLayout.LayoutParams
                            (
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                    )

                    var tit = inflatedLayout.findViewById(R.id.original_title) as TextView
                    var mv = inflatedLayout.findViewById(R.id.img) as ImageView

                    //tit.setText(movie)

                    val res = res.getJSONArray("results").getJSONObject(idx)
                    val path = res.getString("poster_path")
                    requestController.setImageView(path, mv, 300, view.context)

                    /******* inflate *************/

                    similarMoviesGridView.addView(inflatedLayout, gridParam)

                    col++
                    idx++
                }
            }
        })
    }

    private fun getVideos(context: Context) {
        val url = "https://api.themoviedb.org/3/movie/${this.movieId}/videos?api_key=$apiKey"

        requestController.VolleyRequest(url, context, object : ServerCallback<JSONObject> {
            override fun onSuccess(response: JSONObject) {
                val results = response.getJSONArray("results")
                val videos: ArrayList<MovieYoutubeVideo> = ArrayList()
                for (i in 0 until results.length()) {
                    val video = results.getJSONObject(i)
                    if (video.getString("site").equals("YouTube", true)) {
                        val key = video.getString("key")
                        val name = video.getString("name")
                        val type = video.getString("type")
                        videos.add(MovieYoutubeVideo(key, name, type))
                    }
                }

                for (video in videos) {
                    val videoView = LinearLayout(view?.context)
                    val playButton = ImageButton(view?.context)
                    playButton.setOnClickListener {
                        try {
                            val intent = YouTubeStandalonePlayer.createVideoIntent(
                                activity,
                                Constants.youtubeApiKey,
                                video.key
                            )
                            startActivity(intent)
                        } catch (e: ActivityNotFoundException) {
                            val toast = Toast.makeText(
                                view?.context,
                                "You have to install YouTube app",
                                Toast.LENGTH_LONG
                            )
                            toast.show()
                        }

                    }
                    playButton.setImageResource(android.R.drawable.ic_media_play)
                    playButton.minimumWidth = 250
                    playButton.minimumHeight = 250
                    videoView.addView(playButton)

                    val nameView = TextView(view?.context)
                    val typeView = TextView(view?.context)
                    nameView.text = video.name
                    nameView.setTextColor(Color.BLACK)
                    typeView.text = video.type

                    val infoView = LinearLayout(view?.context)
                    infoView.orientation = LinearLayout.VERTICAL
                    infoView.addView(nameView)
                    infoView.addView(typeView)

                    videoView.addView(infoView)
                    videosView.addView(videoView)
                }

            }
        })
    }

    private fun buildStringListFromJsonSArray(jsonArray: JSONArray, key: String): ArrayList<String> {
        val strings: ArrayList<String> = ArrayList()
        for (i in 0 until jsonArray.length()) {
            val res = jsonArray.getJSONObject(i)
            strings.add(res.getString(key))
        }
        return strings
    }

    private fun creditsFromJsonArray(jsonArray: JSONArray, keyFunction: String): ArrayList<Cast> {
        val credits = ArrayList<Cast>()
        for (i in 0 until jsonArray.length()) {
            val res = jsonArray.getJSONObject(i)
            credits.add(Cast(res.getString("name"), res.getString(keyFunction)))
        }
        return credits
    }

    private lateinit var viewModel: MovieDetailsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.movie_details_fragment, container, false)

        titleView = view.findViewById(R.id.original_title) as TextView
        descriptionView = view.findViewById(R.id.overview) as TextView
        castView = view.findViewById(R.id.cast) as TextView
        crewView = view.findViewById(R.id.crew) as TextView
        imageView = view.findViewById(R.id.imgDetails) as ImageView
        genreNamesView = view.findViewById(R.id.genreNames) as LinearLayout
        popularityView = view.findViewById(R.id.popularity) as TextView
        prodCountriesView = view.findViewById(R.id.prodCountries) as LinearLayout
        releaseDateView = view.findViewById(R.id.release_date) as TextView
        subtitleView = view.findViewById(R.id.subtitle) as TextView
        voteCountView = view.findViewById(R.id.vote_count) as TextView
        similarMoviesGridView = view.findViewById(R.id.similarMoviesGridLayout) as GridLayout
        videosView = view.findViewById(R.id.videos) as LinearLayout

        // Recuperation of movie details of TMDB
        requestController.setImageView(urlImg, imageView, 500, view.context)
        getMoreDetails(view.context)
        val crew = listOf("Producer", "Casting", "Music", "Writer", "Director")
        getCredits(5, crew, view.context)
        getSimilarMovies(view .context)
        getVideos(view.context)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dummyContext = context
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString()
                    + " must implement ToolbarListener")
        }
    }

}
