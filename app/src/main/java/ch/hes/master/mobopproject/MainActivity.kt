package ch.hes.master.mobopproject

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import ch.hes.master.mobopproject.data.Constants
import ch.hes.master.mobopproject.data.Movie
import org.json.JSONObject

class MainActivity : AppCompatActivity(), MovieFragment.OnListFragmentInteractionListener {

    private val apiKey = Constants.tmdbApiKey
    val requestController = VolleyRequestController()

    override fun onListFragmentInteraction(item: Movie?) {
        Log.println(Log.DEBUG,"test", "test$item")

        val id = item?.id
        val urlImg = item?.urlImg


        val movieDetailsFragment = MovieDetails.newInstance(id, urlImg)

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
        getActualMovies()
    }

    private fun getActualMovies() {
        val movies: ArrayList<Movie> = ArrayList()
        val url = "https://api.themoviedb.org/3/discover/movie?api_key=$apiKey&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1"
        requestController.volleyRequest(url, this, object : ServerCallback<JSONObject> {
            override fun onSuccess(response: JSONObject) {
                val fragMovie = MovieFragment.newInstance(1, movies)
                val results = response.getJSONArray("results")
                for (i in 0 until results.length()) {
                    val res = results.getJSONObject(i)
                    val movie = Movie(res.getInt("id"), res.getString("title"), res.getString("overview"), res.getString("poster_path"),null)

                    requestController.getPosterImage(movie.urlImg, applicationContext, object : ServerCallback<Bitmap> {
                        override fun onSuccess(result: Bitmap) {
                            movie.img = result
                            fragMovie.updateCell(i)
                        }
                    })
                    movies.add(movie)
                }
                setActiveFragment(fragMovie, "moviefragment")
            }
        })
    }
}
