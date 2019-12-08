package ch.hes.master.mobopproject

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.hes.master.mobopproject.data.Constants
import ch.hes.master.mobopproject.data.People

class ListPeoplesFragment: Fragment() {

    private var listener: OnListFragmentInteractionListener? = null

    private val apiKey = Constants.tmdbApiKey
    private val requestController = VolleyRequestController()

    private val popularPeopleUrl = "https://api.themoviedb.org/3/person/popular?api_key=$apiKey&language=en-US&page=1"
    private val searchUrl = "https://api.themoviedb.org/3/search/person?api_key=$apiKey&language=en-US&page=1&include_adult=false&query="

    //private val args: ListPeoplesFragmentArgs by navArgs()

    class ListPeopleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), GenericAdapter.Binder<People> {

        var nameView: TextView = itemView.findViewById(R.id.name)
        var knownForView: TextView = itemView.findViewById(R.id.known_for)
        var inMoviesView: TextView = itemView.findViewById(R.id.in_movies)
        var imageView: ImageView = itemView.findViewById(R.id.img_people)
        var view: View = itemView

        override fun bind(p: People, clickListener: View.OnClickListener) {
            nameView.text = p.name
            knownForView.text = p.knowFor
            inMoviesView.text = "In movies: " + p.inMovies.map { m -> m.title }
            imageView.setImageBitmap(p.img)

            view.tag = p
            view.setOnClickListener(clickListener)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_peoples_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            //val url = if (args.query != null) searchUrl + args.query else popularPeopleUrl
            val url = ""
            requestController.getPeoples(url, view.context, object : ServerCallback<ArrayList<People>> {
                override fun onSuccess(peoples: ArrayList<People>) {

                    val myAdapter = object : GenericAdapter<People>(peoples, listener) {
                        override fun getLayoutId(position: Int, obj: People): Int {
                            return R.layout.fragment_people
                        }

                        override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
                            return ListPeopleViewHolder(view)
                        }
                    }
                    view.layoutManager= LinearLayoutManager(view.context)
                    view.setHasFixedSize(true)
                    view.adapter=myAdapter
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
