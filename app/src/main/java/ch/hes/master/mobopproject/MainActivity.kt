package ch.hes.master.mobopproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), OnListFragmentInteractionListener {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        fab.setOnClickListener { view ->
            navController.navigate(R.id.searchFragment)
        }

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.loginFragment,
                R.id.registerFragment,
                R.id.listLikesMoviesFragment,
                R.id.userListFragment,
                R.id.listMoviesFragment,
                R.id.listPeoplesFragment,
                R.id.searchFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Set up the tabs bottom navigation
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.setupWithNavController(Navigation.findNavController(this, R.id.nav_host_fragment))
        setNavHeader(navView)

        val item: MenuItem = navView.menu.findItem(R.id.logoutAction)

        val pref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)


        item.setOnMenuItemClickListener {
            with (pref!!.edit()) {
                clear()
                commit()
            }
            setNavHeader(navView)
            drawerLayout.closeDrawer(navView)
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home_button -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean { // Inflate the top_menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.top_menu, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun setNavHeader(navView: NavigationView) {
        val headerView = navView.getHeaderView(0)
        val tvPseudo = headerView.findViewById<TextView>(R.id.tvPseudo)
        val tvMail = headerView.findViewById<TextView>(R.id.tvMail)


        val auth = Common.getAuth(getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE), this)
        if(auth != null) {
            tvPseudo.text = auth.pseudo
            tvMail.text = auth.email
        } else {
            tvPseudo.text = "Sign in or register for see your personal information"
            tvMail.text = ""
        }
    }

}
