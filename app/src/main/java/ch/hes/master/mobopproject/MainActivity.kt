package ch.hes.master.mobopproject

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ch.hes.master.mobopproject.data.Constants
import ch.hes.master.mobopproject.data.Movie


class MainActivity : AppCompatActivity(), MovieFragment.OnListFragmentInteractionListener {

    private val apiKey = Constants.tmdbApiKey
    private val requestController = VolleyRequestController()

    private val popularMoviesUrl = "https://api.themoviedb.org/3/discover/movie?api_key=$apiKey&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1"
    private val searchUrl = "https://api.themoviedb.org/3/search/movie?api_key=$apiKey&language=en-US&page=1&include_adult=false&query="


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

        // Verify the action and get the query
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                getActualMovies(searchUrl + query)
            }
        }
        else {
            getActualMovies(popularMoviesUrl)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean { // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // Handle item selection
        return when (item.itemId) {
            R.id.search_button -> {
                onSearchRequested()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getActualMovies(url: String) {
        requestController.getMovies(url, this, object : ServerCallback<ArrayList<Movie>> {
            override fun onSuccess(movies: ArrayList<Movie>) {
                val fragMovie = MovieFragment.newInstance(1, movies)
                setActiveFragment(fragMovie, "moviefragment")
            }
        })
    }
}
