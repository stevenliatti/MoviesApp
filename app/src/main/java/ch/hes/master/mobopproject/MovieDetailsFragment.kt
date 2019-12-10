package ch.hes.master.mobopproject

import android.content.ActivityNotFoundException
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.gridlayout.widget.GridLayout
import androidx.navigation.fragment.navArgs
import ch.hes.master.mobopproject.data.*
import com.google.android.youtube.player.YouTubeStandalonePlayer
import org.json.JSONArray
import org.json.JSONObject


class MovieDetailsFragment : Fragment() {

    private val apiKey = Constants.tmdbApiKey
    private val requestController = VolleyRequestController()

    private lateinit var dummyContext: Context

    private var movieId : Int? = null
    private var urlImg : String? = null
    private lateinit var details : MovieDetails

    private val args: MovieDetailsFragmentArgs by navArgs()

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
    private lateinit var likeBox: CheckBox
    private lateinit var dislikeBox: CheckBox

    private fun getMoreDetails(context: Context) {
        val url = "https://api.themoviedb.org/3/movie/${this.movieId}?api_key=$apiKey"

        requestController.getMovieDetails(url, context, object : ServerCallback<MovieDetails> {
            override fun onSuccess(res: MovieDetails) {

                details = res

                titleView.setText(details.title)
                descriptionView.setText(details?.overview)

                for (genre in details.genresNames) {
                    val genreView = TextView(view?.context)
                    genreView.text = genre
                    genreNamesView.addView(genreView)
                }

                popularityView.setText("Popularity : " + details.popularity)

                for (prodCount in details.productionCountries) {
                    val prodCountView = TextView(view?.context)
                    prodCountView.text = prodCount
                    prodCountriesView.addView(prodCountView)
                }

                releaseDateView.setText(details.releaseDate)
                subtitleView.setText(details.subtitle)
                voteCountView.setText("Vote count : " + details.voteCount)

            }
        })
    }

    private fun getCredits(castNb: Int, keysCrew: List<String>, context: Context) {
        val url = "https://api.themoviedb.org/3/movie/${this.movieId}/credits?api_key=$apiKey"

        requestController.volleyRequest(url, context, object : ServerCallback<JSONObject> {
            override fun onSuccess(res: JSONObject) {
                val cast = creditsFromJsonArray(res.getJSONArray("cast"), "character")
                val crew = creditsFromJsonArray(res.getJSONArray("crew"), "job")
                var castString = ""
                for (i in 0..castNb) {
                    if (i < cast.size) castString += cast[i].toString() + ", "
                }
                castString =
                    if (castString.isNotEmpty()) castString.subSequence(0, castString.length - 2).toString()
                    else ""
                castView.text = castString

                var crewString = ""
                for (c in crew) {
                    if (keysCrew.contains(c.function)) crewString += c.toString() + ", "
                }
                crewString =
                    if (crewString.isNotEmpty()) crewString.subSequence(0, crewString.length - 2).toString()
                    else ""
                crewView.text = crewString
            }
        })
    }

    private fun getVideos(context: Context, videosNb: Int) {
        val url = "https://api.themoviedb.org/3/movie/${this.movieId}/videos?api_key=$apiKey"

        requestController.volleyRequest(url, context, object : ServerCallback<JSONObject> {
            override fun onSuccess(response: JSONObject) {
                val results = response.getJSONArray("results")
                val videos: ArrayList<MovieYoutubeVideo> = ArrayList()
                val numberVideos = if(results.length() < videosNb) results.length() else videosNb
                for (i in 0 until numberVideos) {
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
                    val thumbnail = ImageView(view?.context)
                    requestController.setYoutubeImageView(video.key, thumbnail, context)

                    thumbnail.setOnClickListener {
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
                    thumbnail.minimumWidth = 250
                    thumbnail.minimumHeight = 250
                    val lp = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    lp.setMargins(0, 0, 10, 0)
                    thumbnail.layoutParams = lp

                    val playButton = ImageView(context)

                    playButton.setImageResource(R.drawable.ic_play_circle_outline_black_50dp)
                    playButton.minimumWidth = 25
                    playButton.minimumHeight = 25
                    val lp2 = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    lp2.setMargins(55, 55, 0, 0)
                    playButton.layoutParams = lp2

                    val frame = FrameLayout(context)
                    frame.addView(thumbnail)
                    frame.addView(playButton)

                    videoView.addView(frame)

                    val nameView = TextView(view?.context)
                    val lp3 = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    lp3.setMargins(0, 20, 0, 0)
                    nameView.layoutParams = lp3
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

    private fun creditsFromJsonArray(jsonArray: JSONArray, keyFunction: String): ArrayList<Cast> {
        val credits = ArrayList<Cast>()
        for (i in 0 until jsonArray.length()) {
            val res = jsonArray.getJSONObject(i)
            credits.add(Cast(res.getString("name"), res.getString(keyFunction)))
        }
        return credits
    }

    private fun initAppreciationButtons(context: Context) {
        // TODO: get actual user instead
        val user = "fred"
        val url = "https://mobop.liatti.ch/user/movieAppreciation?pseudo=$user&idMovie=$movieId"
        requestController.volleyRequest(url, context, object : ServerCallback<JSONObject> {
            override fun onSuccess(response: JSONObject) {
                when (response.getString("appreciation")) {
                    "LIKE" -> {
                        likeBox.isChecked = true
                        dislikeBox.isClickable = false
                        dislikeBox.setTextColor(Color.GRAY)
                    }
                    "DISLIKE" -> {
                        dislikeBox.isChecked = true
                        likeBox.isClickable = false
                        likeBox.setTextColor(Color.GRAY)
                    }
                }
            }
        })
    }

    enum class AppreciationEndpoint {
        LIKE, UNDO_LIKE, DISLIKE, UNDO_DISLIKE
    }

    private fun setAppreciation(context: Context, appreciationEndpoint: AppreciationEndpoint) {
        // TODO: get actual user instead
        val user = "fred"
        val endpoint = when (appreciationEndpoint) {
            AppreciationEndpoint.LIKE -> "likeMovie"
            AppreciationEndpoint.UNDO_LIKE -> "undoLikeMovie"
            AppreciationEndpoint.DISLIKE -> "dislikeMovie"
            AppreciationEndpoint.UNDO_DISLIKE -> "undoDislikeMovie"
        }

        val url = "https://mobop.liatti.ch/user/$endpoint?pseudo=$user&idMovie=$movieId"
        requestController.volleyRequest(url, context, object : ServerCallback<JSONObject> {
            override fun onSuccess(response: JSONObject) {

            }
        })
    }

    private fun onAppreciationBoxesClicked(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                R.id.likeBox -> {
                    when (checked) {
                        true -> {
                            dislikeBox.isClickable = false
                            dislikeBox.setTextColor(Color.GRAY)
                            setAppreciation(view.context, AppreciationEndpoint.LIKE)
                        }
                        false -> {
                            dislikeBox.isClickable = true
                            dislikeBox.setTextColor(Color.BLACK)
                            setAppreciation(view.context, AppreciationEndpoint.UNDO_LIKE)
                        }
                    }
                }
                R.id.dislikeBox -> {
                    when (checked) {
                        true -> {
                            likeBox.isClickable = false
                            likeBox.setTextColor(Color.GRAY)
                            setAppreciation(view.context, AppreciationEndpoint.DISLIKE)
                        }
                        false -> {
                            likeBox.isClickable = true
                            likeBox.setTextColor(Color.BLACK)
                            setAppreciation(view.context, AppreciationEndpoint.UNDO_DISLIKE)
                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_movie_details, container, false)

        movieId = args.id
        urlImg = args.urlImg

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
        likeBox = view.findViewById(R.id.likeBox) as CheckBox
        dislikeBox = view.findViewById(R.id.dislikeBox) as CheckBox

        // Recuperation of movie details of TMDB
        requestController.setImageView(urlImg, imageView, 500, view.context)
        getMoreDetails(view.context)
        val crew = listOf("Producer", "Casting", "Music", "Writer", "Director")
        getCredits(5, crew, view.context)
        val movie = Movie(42, "bob", Bitmap.createBitmap(42, 42, Bitmap.Config.ALPHA_8), "", "")
        Common.getGridMovies(
            view,
            "https://api.themoviedb.org/3/movie/${this.movieId}/similar?api_key=$apiKey",
            movie,
            movie,
            similarMoviesGridView)
        getVideos(view.context, 3)
        initAppreciationButtons(view.context)

        likeBox.setOnClickListener { listenerView ->
            onAppreciationBoxesClicked(listenerView)
        }
        dislikeBox.setOnClickListener { listenerView ->
            onAppreciationBoxesClicked(listenerView)
        }

        return view
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
