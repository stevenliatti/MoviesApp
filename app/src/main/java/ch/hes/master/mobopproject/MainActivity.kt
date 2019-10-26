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
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity(), MovieFragment.OnListFragmentInteractionListener {

    private fun makeRequest(): JsonObjectRequest {
        val apiKey = Constants.apiKey
        val url = "https://api.themoviedb.org/3/discover/movie?api_key=$apiKey&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1"
        val movies: ArrayList<Movie> = ArrayList()

        return JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                val results = response.getJSONArray("results")
                Log.println(Log.DEBUG, this.javaClass.name, "makeRequest : $results")
                for (i in 0 until results.length()) {
                    val res = results.getJSONObject(i)
                    val newMovie = Movie(res.getInt("id"), res.getString("original_title"), res.getString("overview"))
                    Log.println(Log.DEBUG, this.javaClass.name, "newMovie : $newMovie")
                    movies.add(newMovie)
                }

                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, MovieFragment.newInstance(1, movies), "moviefragment")
                    .addToBackStack("moviefragment")
                    .commit()
            },
            Response.ErrorListener { error ->
                Log.println(Log.DEBUG, this.javaClass.name, "error in makeRequest : $error")
            }
        )
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

        val httpQueue = Volley.newRequestQueue(this)
        httpQueue.add(makeRequest())
    }
}
