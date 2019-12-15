package ch.hes.master.mobopproject

import android.content.Context
import android.graphics.Bitmap
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
import ch.hes.master.mobopproject.data.Movie
import ch.hes.master.mobopproject.data.People
import org.json.JSONException
import org.json.JSONObject

class ListPeoplesFragment: Fragment() {

    private var listener: OnListFragmentInteractionListener? = null

    private val apiKey = Constants.tmdbApiKey
    private val requestController = VolleyRequestController()

    private val popularPeopleUrl = "https://api.themoviedb.org/3/person/popular?api_key=$apiKey&language=en-US&page=1"
    private val searchUrl = "https://api.themoviedb.org/3/search/person?api_key=$apiKey&language=en-US&page=1&include_adult=false&query="

    private val args: ListPeoplesFragmentArgs by navArgs()

    class ListPeopleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), GenericAdapter.Binder<People> {

        var nameView: TextView = itemView.findViewById(R.id.name)
        var knownForView: TextView = itemView.findViewById(R.id.known_for)
        var inMoviesView: TextView = itemView.findViewById(R.id.in_movies)
        var imageView: ImageView = itemView.findViewById(R.id.img_people)
        var view: View = itemView

        override fun bind(data: People, clickListener: View.OnClickListener) {
            nameView.text = data.nameTitle
            knownForView.text = data.knowFor
            inMoviesView.text = "In movies: " + data.inMovies!!.map { m -> m.nameTitle }
            imageView.setImageBitmap(data.img)

            view.tag = data
            view.setOnClickListener(clickListener)
        }
    }

    private fun getPeoples(url: String, context: Context, callback: ServerCallback<ArrayList<People>>) {
        val peoples: ArrayList<People> = ArrayList()
        requestController.httpGet(url, context, object : ServerCallback<JSONObject> {
            override fun onSuccess(result: JSONObject) {
                val jsArray = result.getJSONArray("results")
                for (i in 0 until jsArray.length()) {
                    val jsObj = jsArray.getJSONObject(i)
                    requestController.getPosterImage(jsObj.getString("profile_path"), context, object : ServerCallback<Bitmap> {
                        override fun onSuccess(img: Bitmap) {
                            val knownFor: ArrayList<Movie> = ArrayList()

                            try {
                                val knownForJson = jsObj.getJSONArray("known_for")
                                for (j in 0 until knownForJson.length()) {
                                    val propsMovie = knownForJson.getJSONObject(j)
                                    if (propsMovie.getString("media_type") == "movie") {
                                        knownFor.add(
                                            Movie(
                                                propsMovie.getInt("id"),
                                                propsMovie.getString("title"),
                                                null,
                                                propsMovie.getString("poster_path"),
                                                propsMovie.getString("overview"))
                                        )
                                    }
                                }
                            }
                            catch (e: JSONException) {
                                println("$e, $url")
                            }

                            peoples.add(People(
                                jsObj.getInt("id"),
                                jsObj.getString("name"),
                                img,
                                jsObj.getString("profile_path"),
                                jsObj.getString("known_for_department"),
                                knownFor))

                            if(i == jsArray.length()-1) {
                                callback.onSuccess(peoples)
                            }
                        }
                    })
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_peoples_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            val url = if (args.query != null) searchUrl + args.query else popularPeopleUrl

            getPeoples(url, view.context, object : ServerCallback<ArrayList<People>> {
                override fun onSuccess(result: ArrayList<People>) {

                    val myAdapter = object : GenericAdapter<People>(result, listener) {
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
            throw RuntimeException("$context must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
