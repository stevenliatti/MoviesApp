package ch.hes.master.mobopproject

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import ch.hes.master.mobopproject.data.Constants
import ch.hes.master.mobopproject.data.Movie
import java.lang.Exception
import kotlin.reflect.typeOf

class ListMoviesFragment: Fragment() {

    private lateinit var my: ListMoviesView

    private var listener: OnListFragmentInteractionListener? = null

    private val apiKey = Constants.tmdbApiKey
    private val requestController = VolleyRequestController()

    private val popularMoviesUrl = "https://api.themoviedb.org/3/discover/movie?api_key=$apiKey&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1"
    private val searchUrl = "https://api.themoviedb.org/3/search/movie?api_key=$apiKey&language=en-US&page=1&include_adult=false&query="

    private val args: ListMoviesFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                val url = if (args.query != null) searchUrl + args.query else popularMoviesUrl

                requestController.getMovies(url, view.context, object : ServerCallback<ArrayList<Movie>> {
                    override fun onSuccess(movies: ArrayList<Movie>) {
                        my = ListMoviesView(movies, listener)
                        adapter = my
                    }
                })
            }
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


    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(movie: Movie, view: View) {
            val action =
                ListMoviesFragmentDirections
                    .actionListMoviesFragmentToMovieDetailsFragment(movie.id, movie.urlImg)
            view.findNavController().navigate(action)
        }
    }

}
