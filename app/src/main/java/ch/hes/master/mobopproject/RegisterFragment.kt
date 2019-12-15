package ch.hes.master.mobopproject

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONObject


class RegisterFragment : Fragment() {

   // private var listener: OnFragmentInteractionListener? = null
    val sharedPref = activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
    private val requestController = VolleyRequestController()
    private val urlLogin = "https://mobop.liatti.ch/user/register"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        val pseudo = view.findViewById(R.id.inputPseudo) as TextInputLayout
        val email = view.findViewById(R.id.inputEmail) as TextInputLayout
        val password = view.findViewById(R.id.inputMdp) as TextInputLayout
        val passwordRepeat = view.findViewById(R.id.inputMdpRepeat) as TextInputLayout
        val button = view.findViewById(R.id.btnRegister) as Button

        button.setOnClickListener {
            if(password.editText!!.text.toString() == passwordRepeat.editText!!.text.toString() && pseudo.editText!!.text.toString() != "") {
                val data = JSONObject()
                data.put("pseudo", pseudo.editText!!.text.toString())
                data.put("email", email.editText!!.text.toString())
                data.put("password", password.editText!!.text.toString())
                requestController.httpPost(urlLogin, data, view.context, object : ServerCallback<JSONObject> {
                    override fun onSuccess(res: JSONObject) {
                        if(res.getString("token") == "TOKKKEN") {
                            val pseudo = res.getString("pseudo")
                            val token = res.getString("token")
                            with (sharedPref!!.edit()) {
                                putString(getString(R.string.pseudo), pseudo)
                                putString(getString(R.string.token), token)
                                commit()
                            }
                            view.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToListMoviesFragment())
                        }
                    }
                })
            }
        }

        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        // listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
        // listener = null
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }
}
