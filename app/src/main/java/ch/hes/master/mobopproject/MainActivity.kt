package ch.hes.master.mobopproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import ch.hes.master.mobopproject.data.Constants
import ch.hes.master.mobopproject.data.Movie

class MainActivity : AppCompatActivity(), MovieFragment.OnListFragmentInteractionListener {

    val apiKey = Constants.tmdbApiKey
    val requestControler = VolleyRequestController()

    override fun onListFragmentInteraction(item: Movie?) {
        Log.println(Log.DEBUG,"test", "test$item")

        val id = item?.id
        val urlImg = item?.urlImg


        val movieDetailsFragment = MovieDetails.newInstance(id, urlImg)

        setActiveFragment(movieDetailsFragment, "moviedetailsfragment")
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun setActiveFragment(frag: Fragment, tag: String) {
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

    fun getActualMovies() {
        var movies: ArrayList<Movie> = ArrayList()
        val url = "https://api.themoviedb.org/3/discover/movie?api_key=$apiKey&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1"

        requestControler.getMovies(url, this, object : ServerCallback<ArrayList<Movie>> {
            override fun onSuccess(movies: ArrayList<Movie>) {
                val fragMovie = MovieFragment.newInstance(1, movies)
                setActiveFragment(fragMovie, "moviefragment")
            }
        })
    }
}
// Log.println(Log.DEBUG, this.javaClass.name, "makeRequest : $results")