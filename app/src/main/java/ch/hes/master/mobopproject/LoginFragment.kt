package ch.hes.master.mobopproject

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONObject
import org.w3c.dom.Text


class LoginFragment : Fragment() {


    // private var listener: OnFragmentInteractionListener? = null
    private val requestController = VolleyRequestController()
    private val urlLogin = "https://mobop.liatti.ch/user/login"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val pseudo = view.findViewById(R.id.inputPseudo) as TextInputLayout
        val password = view.findViewById(R.id.inputMdp) as TextInputLayout
        val button: Button = view.findViewById(R.id.btnLogin)
        val register = view.findViewById(R.id.tvRegister) as TextView
       //  password.setPasswordVisibilityToggleDrawable(R.drawable.ic_person_greem_100dp)
      //  if(pseudo.editText!!.text.toString() != "" && password.editText!!.text.toString() != "") {
            button.setOnClickListener {
                val data = JSONObject()
                data.put("pseudo", pseudo.editText!!.text.toString())
                data.put("password", password.editText!!.text.toString())
                requestController.httpPost(urlLogin, data, view.context, object : ServerCallback<JSONObject> {
                    override fun onSuccess(res: JSONObject) {
                        if(res.getString("token") == "TOKKKEN") {
                            val trucPersistant = res.getString("pseudo")
                            println(trucPersistant)
                            // navigate to home
                        }
                    }
                })
            }
        //}
        register.setOnClickListener {
            view.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }


        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
       // listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        /*if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }*/
    }

    override fun onDetach() {
        super.onDetach()
        //listener = null
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

}
