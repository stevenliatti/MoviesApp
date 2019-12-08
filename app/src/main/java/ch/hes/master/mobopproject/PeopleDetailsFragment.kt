package ch.hes.master.mobopproject

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.gridlayout.widget.GridLayout
import androidx.navigation.fragment.navArgs
import ch.hes.master.mobopproject.data.Cast
import ch.hes.master.mobopproject.data.Constants
import ch.hes.master.mobopproject.data.PeopleDetails
import org.json.JSONArray
import org.json.JSONObject


class PeopleDetailsFragment : Fragment() {

    private val apiKey = Constants.tmdbApiKey
    private val requestController = VolleyRequestController()

    private lateinit var dummyContext: Context

    private var id : Int? = null
    private var urlImg : String? = null

    private val args: PeopleDetailsFragmentArgs by navArgs()

    private lateinit var name: TextView
    private lateinit var biography: TextView
    private lateinit var knownFor: TextView
    private lateinit var birthday: TextView
    private lateinit var placeOfBirth: TextView
    private lateinit var deathday: TextView
    private lateinit var imageView: ImageView
    private lateinit var gender: TextView
    private lateinit var popularity: TextView
    private lateinit var homepage: TextView
    private lateinit var similarMoviesGridView: GridLayout

    private fun getDetails(context: Context) {
        val url = "https://api.themoviedb.org/3/person/${this.id}?api_key=$apiKey"

        requestController.getPeopleDetails(url, context, object : ServerCallback<PeopleDetails> {
            override fun onSuccess(peopleDetails: PeopleDetails) {
                name.text = peopleDetails.name
                biography.text = peopleDetails.biography
                knownFor.text = peopleDetails.knownFor
                birthday.text = peopleDetails.birthday
                deathday.text = peopleDetails.deathday
                placeOfBirth.text = peopleDetails.placeOfBirth
                gender.text = peopleDetails.gender
                popularity.text = peopleDetails.popularity.toString()
                homepage.text = peopleDetails.homepage
            }
        })
    }

    private fun getCredits(castNb: Int, keysCrew: List<String>, context: Context) {
        val url = "https://api.themoviedb.org/3/person/${this.id}/credits?api_key=$apiKey"

        requestController.volleyRequest(url, context, object : ServerCallback<JSONObject> {
            override fun onSuccess(res: JSONObject) {
                val cast = creditsFromJsonArray(res.getJSONArray("cast"), "character")
                val crew = creditsFromJsonArray(res.getJSONArray("crew"), "job")
                var castString = ""
                for (i in 0..castNb) {
                    if (i < cast.size) castString += cast[i].toString() + ", "
                }
                castString =
                    if (castString.isNotEmpty()) castString.subSequence(0, castString.length - 2).toString()
                    else ""
                placeOfBirth.text = castString

                var crewString = ""
                for (c in crew) {
                    if (keysCrew.contains(c.function)) crewString += c.toString() + ", "
                }
                crewString =
                    if (crewString.isNotEmpty()) crewString.subSequence(0, crewString.length - 2).toString()
                    else ""
                deathday.text = crewString
            }
        })
    }

    private fun creditsFromJsonArray(jsonArray: JSONArray, keyFunction: String): ArrayList<Cast> {
        val credits = ArrayList<Cast>()
        for (i in 0 until jsonArray.length()) {
            val res = jsonArray.getJSONObject(i)
            credits.add(Cast(res.getString("name"), res.getString(keyFunction)))
        }
        return credits
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_people_details, container, false)

        id = args.id
        urlImg = args.urlImg

        name = view.findViewById(R.id.name_details) as TextView
        biography = view.findViewById(R.id.biography) as TextView
        knownFor = view.findViewById(R.id.known_for_details) as TextView
        birthday = view.findViewById(R.id.birthday) as TextView
        placeOfBirth = view.findViewById(R.id.place_of_birth) as TextView
        deathday = view.findViewById(R.id.deathday) as TextView
        imageView = view.findViewById(R.id.img_people_details) as ImageView
        gender = view.findViewById(R.id.gender) as TextView
        popularity = view.findViewById(R.id.popularity_people) as TextView
        homepage = view.findViewById(R.id.homepage) as TextView
        similarMoviesGridView = view.findViewById(R.id.in_movies_grid) as GridLayout

        requestController.setImageView(urlImg, imageView, 500, view.context)
        getDetails(view.context)
        //getCredits(5, crew, view.context)

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
