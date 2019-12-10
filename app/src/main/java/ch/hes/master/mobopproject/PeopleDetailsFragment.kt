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
import androidx.gridlayout.widget.GridLayout
import androidx.navigation.fragment.navArgs
import ch.hes.master.mobopproject.data.Constants
import ch.hes.master.mobopproject.data.Movie
import ch.hes.master.mobopproject.data.People
import ch.hes.master.mobopproject.data.PeopleDetails


class PeopleDetailsFragment : Fragment() {

    private val apiKey = Constants.tmdbApiKey
    private val requestController = VolleyRequestController()

    private lateinit var dummyContext: Context

    private var id : Int? = null
    private var urlImg : String? = null
    private var knownFor: String? = null

    private lateinit var details: PeopleDetails

    private val args: PeopleDetailsFragmentArgs by navArgs()

    private lateinit var name: TextView
    private lateinit var biography: TextView
    private lateinit var knownForTextView: TextView
    private lateinit var birthday: TextView
    private lateinit var placeOfBirth: TextView
    private lateinit var deathday: TextView
    private lateinit var imageView: ImageView
    private lateinit var gender: TextView
    private lateinit var popularity: TextView
    private lateinit var homepage: TextView
    private lateinit var inMoviesGridLayout: GridLayout

    private fun getDetails(context: Context) {
        val url = "https://api.themoviedb.org/3/person/${this.id}?api_key=$apiKey"

        requestController.getPeopleDetails(url, context, object : ServerCallback<PeopleDetails> {
            override fun onSuccess(ds: PeopleDetails) {
                details = ds
                name.text = ds.name
                biography.text = ds.biography
                knownForTextView.text = ds.knownFor
                birthday.text = ds.birthday
                deathday.text = ds.deathday
                placeOfBirth.text = ds.placeOfBirth
                gender.text = ds.gender
                popularity.text = ds.popularity.toString()
                homepage.text = ds.homepage
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_people_details, container, false)

        id = args.id
        urlImg = args.urlImg
        knownFor = args.knownFor

        name = view.findViewById(R.id.name_details) as TextView
        biography = view.findViewById(R.id.biography) as TextView
        knownForTextView = view.findViewById(R.id.known_for_details) as TextView
        birthday = view.findViewById(R.id.birthday) as TextView
        placeOfBirth = view.findViewById(R.id.place_of_birth) as TextView
        deathday = view.findViewById(R.id.deathday) as TextView
        imageView = view.findViewById(R.id.img_people_details) as ImageView
        gender = view.findViewById(R.id.gender) as TextView
        popularity = view.findViewById(R.id.popularity_people) as TextView
        homepage = view.findViewById(R.id.homepage) as TextView
        inMoviesGridLayout = view.findViewById(R.id.in_movies_grid) as GridLayout

        requestController.setImageView(urlImg, imageView, 500, view.context)
        getDetails(view.context)
        println(knownFor)
        Common.getGridMovies(
            view,
            "https://api.themoviedb.org/3/person/${this.id}/movie_credits?api_key=$apiKey",
            People(42, "", Bitmap.createBitmap(42,42, Bitmap.Config.ALPHA_8), "", knownFor!!, listOf()),
            Movie(42, "bob", Bitmap.createBitmap(42,42, Bitmap.Config.ALPHA_8), "", ""),
            inMoviesGridLayout)

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dummyContext = context
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString()
                    + " must implement ToolbarListener")
        }
    }

}
