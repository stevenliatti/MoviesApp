package ch.hes.master.mobopproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ch.hes.master.mobopproject.dummy.DummyContent

class MainActivity : AppCompatActivity(), MovieFragment.OnListFragmentInteractionListener {
    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {
        Log.println(Log.DEBUG,"test", "test" + item)
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var myFirstFragment = MovieFragment.newInstance(1)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, myFirstFragment, "mymoviefragment")
            .addToBackStack("mymoviefragment")
            .commit()


    }
}
