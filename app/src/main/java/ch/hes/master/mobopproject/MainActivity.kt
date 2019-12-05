package ch.hes.master.mobopproject

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import ch.hes.master.mobopproject.data.Constants
import ch.hes.master.mobopproject.data.Movie


class MainActivity : AppCompatActivity(), ListMoviesFragment.OnListFragmentInteractionListener {

    private val apiKey = Constants.tmdbApiKey
    private val requestController = VolleyRequestController()

    private val popularMoviesUrl = "https://api.themoviedb.org/3/discover/movie?api_key=$apiKey&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1"
    private val searchUrl = "https://api.themoviedb.org/3/search/movie?api_key=$apiKey&language=en-US&page=1&include_adult=false&query="


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        // Verify the action and get the query
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                getActualMovies(searchUrl + query)
            }
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
            R.id.home_button -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getActualMovies(url: String) {
        requestController.getMovies(url, this, object : ServerCallback<ArrayList<Movie>> {
            override fun onSuccess(movies: ArrayList<Movie>) {
               // val fragMovie = ListMoviesFragment(movies)
                val fragTab = UserListFragment()
            }
        })
    }
}
