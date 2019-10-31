package ch.hes.master.mobopproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import ch.hes.master.mobopproject.data.Constants
import ch.hes.master.mobopproject.data.Movie
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import android.graphics.Bitmap
import com.android.volley.toolbox.ImageRequest


class MainActivity : AppCompatActivity(), MovieFragment.OnListFragmentInteractionListener {

    val apiKey = Constants.apiKey

    private fun makeRequest(): JsonObjectRequest {

        var movies: ArrayList<Movie> = ArrayList()
        val url = "https://api.themoviedb.org/3/discover/movie?api_key=$apiKey&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1"

        return JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                val results = response.getJSONArray("results")
                Log.println(Log.DEBUG, this.javaClass.name, "makeRequest : $results")
                for (i in 0 until results.length()) {
                    val res = results.getJSONObject(i)
                    val movie = Movie(res.getInt("id"), res.getString("original_title"), res.getString("overview"), null)
                    HttpQueue.getInstance(this).addToRequestQueue(getImage(res, movie))
                    movies.add(movie)
                }

                val fragMovie = MovieFragment.newInstance(1, movies)

                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, fragMovie, "moviefragment")
                    .addToBackStack("moviefragment")
                    .commit()
            },
            Response.ErrorListener { error ->
                Log.println(Log.DEBUG, this.javaClass.name, "error in makeRequest : $error")
            }
        )
    }

    private fun getImage(res: JSONObject, movie: Movie): ImageRequest {
        val url = "https://image.tmdb.org/t/p/w300" + res.getString("poster_path")

        return ImageRequest(url,
            Response.Listener { response ->
                val img = Bitmap.createBitmap(response)
                movie.img = img
            }, 0, 0, null, null)

    }

    override fun onListFragmentInteraction(item: Movie?) {
        Log.println(Log.DEBUG,"test", "test$item")


        val movieDetailsFragment = MovieDetails.newInstance(item)

        setActiveFragment(movieDetailsFragment, "moviedetailsfragment")
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun setActiveFragment(frag: Fragment, tag: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, frag, tag)
            .addToBackStack(tag)
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        HttpQueue.getInstance(this).addToRequestQueue(makeRequest())

    }
}
