package ch.hes.master.mobopproject

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.gridlayout.widget.GridLayout
import androidx.navigation.fragment.navArgs
import ch.hes.master.mobopproject.data.Constants
import ch.hes.master.mobopproject.data.PeopleDetails
import org.json.JSONObject


class PeopleDetailsFragment : Fragment() {

    private val apiKey = Constants.tmdbApiKey
    private val requestController = VolleyRequestController()

    private lateinit var dummyContext: Context

    private var id : Int? = null
    private var urlImg : String? = null
    private var knownFor: String? = null

    private val args: PeopleDetailsFragmentArgs by navArgs()

    private lateinit var name: TextView
    private lateinit var biography: TextView
    private lateinit var knownForTextView: TextView
    private lateinit var birthday: TextView
    private lateinit var placeOfBirth: TextView
    private lateinit var deathDay: TextView
    private lateinit var imageView: ImageView
    private lateinit var gender: TextView
    private lateinit var popularity: TextView
    private lateinit var homepage: TextView
    private lateinit var inMoviesGridLayout: GridLayout

    private fun getPeopleDetails(context: Context) {
        val url = "https://api.themoviedb.org/3/person/${this.id}?api_key=$apiKey"

        requestController.httpGet(url, context, object : ServerCallback<JSONObject> {
            override fun onSuccess(result: JSONObject) {
                val peopleDetails = PeopleDetails(
                    result.getInt("id"),
                    result.getString("name"),
                    result.getString("biography"),
                    result.getString("known_for_department"),
                    result.getDouble("popularity"),
                    result.getString("birthday"),
                    result.getString("place_of_birth"),
                    result.getString("deathday"),
                    result.getString("gender"),
                    result.getString("homepage")
                )

                check_text_validity(peopleDetails.name, name)
                check_text_validity(peopleDetails.biography, biography)
                check_text_validity(peopleDetails.knownFor, knownForTextView)
                check_text_validity(peopleDetails.birthday, birthday)
                check_text_validity(peopleDetails.deathday, deathDay)
                check_text_validity(peopleDetails.placeOfBirth, placeOfBirth)
                val genderTxt = if (peopleDetails.gender == "1") "Sex: Woman" else "Sex: Man"
                check_text_validity(genderTxt, gender)
                check_text_validity(peopleDetails.popularity.toString(), popularity)
                check_text_validity(peopleDetails.homepage, homepage)
            }
        })
    }

    fun check_text_validity(txt: String, textView: TextView) {
        if (txt != "null") {
            textView.text = txt
        } else {
            textView.isVisible = false
        }
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
        deathDay = view.findViewById(R.id.deathday) as TextView
        imageView = view.findViewById(R.id.img_people_details) as ImageView
        gender = view.findViewById(R.id.gender) as TextView
        popularity = view.findViewById(R.id.popularity_people) as TextView
        homepage = view.findViewById(R.id.homepage) as TextView
        inMoviesGridLayout = view.findViewById(R.id.in_movies_grid) as GridLayout

        requestController.setImageView(urlImg, imageView, 500, view.context)
        getPeopleDetails(view.context)
        Common.getMoviesGrid(
            view,
            "https://api.themoviedb.org/3/person/${this.id}/movie_credits?api_key=$apiKey",
            if (knownFor == "Directing") "crew" else "cast",
            "poster_path",
            inMoviesGridLayout,
            From.PEOPLE,
            Common::makeGridMovieCell)
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
