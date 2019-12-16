package ch.hes.master.mobopproject

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController


class SearchFragment : Fragment() {

    private lateinit var myContext: Context

    private lateinit var inputSearch: EditText
    private lateinit var searchGroup: RadioGroup
    private lateinit var radioMovies: RadioButton
    private lateinit var radioPeoples: RadioButton
    private lateinit var radioUsers: RadioButton
    private lateinit var submitButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        inputSearch = view.findViewById(R.id.input_search) as EditText
        searchGroup = view.findViewById(R.id.group_search) as RadioGroup
        radioMovies = view.findViewById(R.id.radio_movies) as RadioButton
        radioPeoples = view.findViewById(R.id.radio_peoples) as RadioButton
        radioUsers = view.findViewById(R.id.radio_users) as RadioButton
        submitButton = view.findViewById(R.id.search_button) as Button

        inputSearch.onSubmit { submit() }
        submitButton.setOnClickListener { submit() }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            myContext = context
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString()
                    + " must implement ToolbarListener")
        }
    }

    private fun EditText.onSubmit(func: () -> Unit) {
        setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) func()
            true
        }
    }

    private fun submit() {
        val query = inputSearch.text.toString()
        val action = when (searchGroup.checkedRadioButtonId) {
            radioMovies.id -> SearchFragmentDirections.actionSearchFragmentToListMoviesFragment(query)
            radioPeoples.id -> SearchFragmentDirections.actionSearchFragmentToListPeoplesFragment(query)
            radioUsers.id -> {
                val url = "https://mobop.liatti.ch/user/search?pseudo=$query"
                SearchFragmentDirections.actionSearchFragmentToUserListFragment(url)
            }
            else -> SearchFragmentDirections.actionSearchFragmentToListMoviesFragment(query)
        }

        val imm: InputMethodManager = activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
        view!!.findNavController().navigate(action)
    }
}
