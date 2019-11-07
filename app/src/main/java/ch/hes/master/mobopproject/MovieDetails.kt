package ch.hes.master.mobopproject

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ch.hes.master.mobopproject.data.Constants
import ch.hes.master.mobopproject.data.Movie
import ch.hes.master.mobopproject.data.MvDetails
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONArray

class MovieDetails : Fragment() {

    val apiKey = Constants.apiKey

    private var movieId : Int? = null
    private var urlImg : String? = null
    private var movieDetails : MvDetails? = null

    private lateinit var title: TextView
    private lateinit var descr: TextView

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
                val production_countries = buildStringListFromJsonSArray(res.getJSONArray("production_countries"), "name")
                val release_date = res.getString("release_date")
                val subtitle = res.getString("tagline")
                val vote_count = res.getInt("vote_count")

                this.movieDetails = MvDetails(
                    res.getInt("id"),
                    res.getString("original_title"),
                    res.getString("overview"),
                    genresNames,
                    popularity,
                    production_countries,
                    release_date,
                    subtitle,
                    vote_count
                )

                title.setText(this.movieDetails?.originalTitle)
                descr.setText(this.movieDetails?.overview)

            },
            Response.ErrorListener { error ->
                Log.println(Log.DEBUG, this.javaClass.name, "error in makeRequest : $error")
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

    private lateinit var viewModel: MovieDetailsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.movie_details_fragment, container, false)

        title = view.findViewById(R.id.original_title) as TextView
        descr = view.findViewById(R.id.overview) as TextView

        // Call http request for movie details
        HttpQueue.getInstance(view.context).addToRequestQueue(makeRequestForDetails())

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
