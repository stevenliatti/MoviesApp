package ch.hes.master.mobopproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ch.hes.master.mobopproject.dummy.DummyContent

class MainActivity : AppCompatActivity(), MovieFragment.OnListFragmentInteractionListener {
    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {
        Log.println(Log.DEBUG,"test", "test" + item)

        val movieDetailsFragment = MovieDetails.newInstance()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, movieDetailsFragment, "moviedetailsfragment")
            .addToBackStack("moviedetailsfragment")
            .commit()
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var movieFragment = MovieFragment.newInstance(1)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, movieFragment, "moviefragment")
            .addToBackStack("moviefragment")
            .commit()


    }
}
