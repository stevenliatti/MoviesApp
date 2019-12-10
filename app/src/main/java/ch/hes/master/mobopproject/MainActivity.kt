package ch.hes.master.mobopproject

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity(), OnListFragmentInteractionListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.setupWithNavController(Navigation.findNavController(this, R.id.nav_host_fragment))


        // Verify the action and get the query
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                val action = ListMoviesFragmentDirections.actionListMoviesFragmentSelf(query)
                this.findNavController(R.id.nav_host_fragment).navigate(action)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean { // Inflate the top_menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.top_menu, menu)
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

   /* private fun SetupBottomNavigationView(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.setupWithNavController(Navigation.findNavController(this, R.id.nav_host_fragment))
        bottomNavigationView.setOnNavigationItemSelectedListener { item -> changeTab(item.itemId) }
    }

    fun changeTab(integer: Int): Boolean {
        when(integer) {
           // R.id.home_bottom_button -> println("BTN HOME")
            R.id.network_button -> println("BTN NETWORK")
            R.id.my_movies -> println("BTN MOVIES")
        }
        return true
    }*/

}
