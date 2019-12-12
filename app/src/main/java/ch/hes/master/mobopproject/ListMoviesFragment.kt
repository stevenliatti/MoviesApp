package ch.hes.master.mobopproject

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import ch.hes.master.mobopproject.data.Constants
import ch.hes.master.mobopproject.data.Item
import ch.hes.master.mobopproject.data.Movie

class ListMoviesFragment: Fragment() {

    private val apiKey = Constants.tmdbApiKey
    private val requestController = VolleyRequestController()

    private val popularMoviesUrl = "https://api.themoviedb.org/3/discover/movie?api_key=$apiKey&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1"
    private val searchUrl = "https://api.themoviedb.org/3/search/movie?api_key=$apiKey&language=en-US&page=1&include_adult=false&query="

    private val args: ListMoviesFragmentArgs by navArgs()

    private var listener: OnListFragmentInteractionListener? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.generic_list_items, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            val url = if (args.query != null) searchUrl + args.query else popularMoviesUrl

            val movie = Movie(42, "bob", Bitmap.createBitmap(42,42, Bitmap.Config.ALPHA_8), "", "")

            requestController.getItems(url, movie, movie, view.context, object : ServerCallback<ArrayList<Item>> {
                override fun onSuccess(movies: ArrayList<Item>) {
                    val lmv = ListMoviesRecyclerView()
                    lmv.setView(movies, view, listener)
                }
            })
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
