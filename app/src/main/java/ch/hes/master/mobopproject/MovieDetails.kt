package ch.hes.master.mobopproject

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore.Video.Thumbnails.VIDEO_ID
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.gridlayout.widget.GridLayout
import androidx.lifecycle.ViewModelProviders
import ch.hes.master.mobopproject.data.Constants
import ch.hes.master.mobopproject.data.MvDetails
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.youtube.player.YouTubeStandalonePlayer
import kotlinx.android.synthetic.main.fragment_movie.view.*
import org.json.JSONArray


class MovieDetails : Fragment() {

    private val apiKey = Constants.tmdbApiKey
    val requestControler = VolleyRequestController()

    private lateinit var dummyContext: Context

    private var movieId : Int? = null
    private var urlImg : String? = null
    private var movieDetails : MvDetails? = null

    private lateinit var titleView: TextView
    private lateinit var descriptionView: TextView
    private lateinit var imageView: ImageView
    private lateinit var genreNamesView: LinearLayout
    private lateinit var popularityView: TextView
    private lateinit var prodCountriesView: LinearLayout
    private lateinit var releaseDateView: TextView
    private lateinit var subtitleView: TextView
    private lateinit var voteCountView: TextView

    private lateinit var similarMoviesGridView: GridLayout

    companion object {
        @JvmStatic
        fun newInstance(id: Int?, urlImg: String?): MovieDetails {
            return MovieDetails().apply {
                this.movieId = id
                this.urlImg = urlImg
            }
        }
    }

    private fun makeRequestForDetails(): JsonObjectRequest {
        val url = "https://api.themoviedb.org/3/movie/${this.movieId}?api_key=$apiKey"

        return JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { res ->
                val genresNames = buildStringListFromJsonSArray(res.getJSONArray("genres"), "name")
                val popularity = res.getDouble("popularity")
                val productionCountries = buildStringListFromJsonSArray(res.getJSONArray("production_countries"), "name")
                val releaseDate = res.getString("release_date")
                val subtitle = res.getString("tagline")
                val voteCount = res.getInt("vote_count")

                this.movieDetails = MvDetails(
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

                titleView.setText(this.movieDetails?.title)
                descriptionView.setText(this.movieDetails?.overview)

                for (genre in genresNames) {
                    val genreView = TextView(view?.context)
                    genreView.text = genre
                    this.genreNamesView.addView(genreView)
                }

                popularityView.setText("Popularity : " + popularity.toString())

                for (prodCount in productionCountries) {
                    val prodCountView = TextView(view?.context)
                    prodCountView.text = prodCount
                    this.prodCountriesView.addView(prodCountView)
                }

                releaseDateView.setText(releaseDate)
                subtitleView.setText(subtitle)
                voteCountView.setText("Vote count : " + voteCount.toString())

            },
            Response.ErrorListener { error ->
                Log.println(Log.DEBUG, this.javaClass.name, "error in makeRequestForDetails : $error")
            }
        )
    }

    private fun makeRequestForSimilarMovies(): JsonObjectRequest {
        val url = "https://api.themoviedb.org/3/movie/${this.movieId}/similar?api_key=$apiKey"

        return JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { res ->
                val similarMovies = buildStringListFromJsonSArray(res.getJSONArray("results"), "title")
                val total = similarMovies.size
                val columnsNumber = 3
                var row = 0
                var col = 0

               this.similarMoviesGridView.setColumnCount(columnsNumber)
               this.similarMoviesGridView.setRowCount(total / columnsNumber)
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

                    val gridParam: GridLayout.LayoutParams = GridLayout.LayoutParams(rowSpan, colspan)

                    val movieView = TextView(view?.context)
                    movieView.text = movie

                    /******* inflate *************/
                    var inflater: LayoutInflater = LayoutInflater.from(context)

                    //to get the MainLayout
                    val view: View = inflater.inflate(R.layout.movie_details_fragment, null)

                    val inflatedLayout: View = inflater.inflate(R.layout.fragment_movie, view as ViewGroup, false)

                    inflatedLayout.setLayoutParams(LinearLayout.LayoutParams
                        (
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                    )

                    var tit = inflatedLayout.findViewById(R.id.original_title) as TextView
                    var mv = inflatedLayout.findViewById(R.id.img) as ImageView

                    //tit.setText(movie)
                    displayImgs(res.getJSONArray("results"), "poster_path", mv, view.context, idx)

                    /******* inflate *************/

                    this.similarMoviesGridView.addView(inflatedLayout, gridParam)



                    // this.similarMoviesGridView.addView(movieView, gridParam)
                    col++
                    idx++
                }
            },
            Response.ErrorListener { error ->
                Log.println(Log.DEBUG, this.javaClass.name, "error in makeRequestForSimilarMovies : $error")
            }
        )
    }

    private fun makeRequestForVideos(): JsonObjectRequest {
        val url = "https://api.themoviedb.org/3/movie/${this.movieId}?api_key=$apiKey"

        return JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { res ->

            },
            Response.ErrorListener { error ->
                Log.println(Log.DEBUG, this.javaClass.name, "error in makeRequestForVideos : $error")
            }
        )
    }

    private fun buildStringListFromJsonSArray(jsonArray: JSONArray, key: String): ArrayList<String> {
        val strings: ArrayList<String> = ArrayList()
        for (i in 0 until jsonArray.length()) {
            val res = jsonArray.getJSONObject(i)
            strings.add(res.getString(key))
        }
        return strings
    }

    private fun displayImgs(jsonArray: JSONArray, key: String, mv: ImageView, context: Context, idx: Int) {
            val res = jsonArray.getJSONObject(idx)
            val path = res.getString(key)
            requestControler.getPosterImage(path, context, object : ServerCallback<Bitmap> {
                override fun onSuccess(result: Bitmap) {
                    mv.setImageBitmap(result)
                }
            })
    }

    private lateinit var viewModel: MovieDetailsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.movie_details_fragment, container, false)

        titleView = view.findViewById(R.id.original_title) as TextView
        descriptionView = view.findViewById(R.id.overview) as TextView
        imageView = view.findViewById(R.id.imgDetails) as ImageView
        genreNamesView = view.findViewById(R.id.genreNames) as LinearLayout
        popularityView = view.findViewById(R.id.popularity) as TextView
        prodCountriesView = view.findViewById(R.id.prodCountries) as LinearLayout
        releaseDateView = view.findViewById(R.id.release_date) as TextView
        subtitleView = view.findViewById(R.id.subtitle) as TextView
        voteCountView = view.findViewById(R.id.vote_count) as TextView
        similarMoviesGridView = view.findViewById(R.id.similarMoviesGridLayout) as GridLayout

        // Call http request for movie details
        HttpQueue.getInstance(view.context).addToRequestQueue(Common.setImage(urlImg, imageView, 500))
        HttpQueue.getInstance(view.context).addToRequestQueue(makeRequestForDetails())
        HttpQueue.getInstance(view.context).addToRequestQueue(makeRequestForSimilarMovies())
        //HttpQueue.getInstance(view.context).addToRequestQueue(makeRequestForVideos())

       /* val intent = YouTubeStandalonePlayer.createVideoIntent(
            activity,
            Constants.youtubeApiKey,
            "GKXS_YA9s7E"
        )
        startActivity(intent)*/

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
