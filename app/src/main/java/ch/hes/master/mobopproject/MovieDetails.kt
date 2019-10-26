package ch.hes.master.mobopproject

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ch.hes.master.mobopproject.data.Movie

class MovieDetails : Fragment() {

    private var name : String = ""
    private var movie : Movie? = null

    companion object {
        @JvmStatic
        fun newInstance(movie: Movie?): MovieDetails {
            return MovieDetails().apply {
                this.movie = movie
            }
        }
    }

    private lateinit var viewModel: MovieDetailsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.movie_details_fragment, container, false)

        val title = view.findViewById(R.id.title) as TextView
        val descr = view.findViewById(R.id.description) as TextView

        title.setText(this.movie?.originalTitle)
        descr.setText(this.movie?.overview)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
