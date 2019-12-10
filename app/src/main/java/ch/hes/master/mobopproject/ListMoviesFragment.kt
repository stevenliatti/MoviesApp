package ch.hes.master.mobopproject

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.hes.master.mobopproject.data.Constants
import ch.hes.master.mobopproject.data.Movie

class ListMoviesFragment: Fragment() {

    private var listener: OnListFragmentInteractionListener? = null

    private val apiKey = Constants.tmdbApiKey
    private val requestController = VolleyRequestController()

    private val popularMoviesUrl = "https://api.themoviedb.org/3/discover/movie?api_key=$apiKey&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1"
    private val searchUrl = "https://api.themoviedb.org/3/search/movie?api_key=$apiKey&language=en-US&page=1&include_adult=false&query="

    private val args: ListMoviesFragmentArgs by navArgs()

    class MyViewHolder : RecyclerView.ViewHolder, GenericAdapter.Binder<Movie> {

        var title: TextView
        var overviewView: TextView
        var img: ImageView
        var view: View
        constructor( itemView: View):super(itemView){
            title = itemView.findViewById(R.id.original_title)
            overviewView = itemView.findViewById(R.id.overview)
            img = itemView.findViewById(R.id.img)
            view = itemView
        }
        override fun bind(m: Movie, clickListener: View.OnClickListener) {
            title.text = m.title
            overviewView.text = m.overview
            img.setImageBitmap(m.img)

            view.tag = m
            view.setOnClickListener(clickListener)
        }
    }

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

                        val myAdapter = object : GenericAdapter<Movie>(movies, listener) {
                            override fun getLayoutId(position: Int, obj: Movie): Int {
                                return R.layout.fragment_movie
                            }

                            override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
                                return MyViewHolder(view)
                            }
                        }
                        view.layoutManager= LinearLayoutManager(view.context)
                        view.setHasFixedSize(true)
                        view.adapter=myAdapter

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
        fun onListFragmentInteraction(item: Any, view: View) {
            if (item is Movie) {
                val action =
                    ListMoviesFragmentDirections
                        .actionListMoviesFragmentToMovieDetailsFragment(item.id, item.urlImg)
                view.findNavController().navigate(action)
            }
        }
    }

}
