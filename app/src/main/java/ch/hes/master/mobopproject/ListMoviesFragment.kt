package ch.hes.master.mobopproject

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import ch.hes.master.mobopproject.data.Constants
import ch.hes.master.mobopproject.data.Movie

class ListMoviesFragment: Fragment() {

    private val apiKey = Constants.tmdbApiKey

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

            Common.getMovies(url, "results", "poster_path", view.context, object : ServerCallback<ArrayList<Movie>> {
                override fun onSuccess(result: ArrayList<Movie>) {
                    val lmv = ListMoviesRecyclerView()
                    lmv.setView(result, view, listener)
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
            throw RuntimeException("$context must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
