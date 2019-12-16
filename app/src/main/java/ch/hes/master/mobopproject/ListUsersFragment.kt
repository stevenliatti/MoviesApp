package ch.hes.master.mobopproject

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import ch.hes.master.mobopproject.data.User
import com.google.android.material.tabs.TabLayout
import org.json.JSONObject


class ListUsersFragment : Fragment() {
    private lateinit var collectionPagerAdapter: UserListPagerAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var pseudo: String

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view =  inflater.inflate(R.layout.fragment_user_list, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val auth = Common.getAuth((activity as MainActivity).
            getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE),
            view.context)
        if(auth != null)
            pseudo = auth.pseudo
        else {
            findNavController().navigate(ListUsersFragmentDirections.actionUserListFragmentToLoginFragment())
            return
        }
        collectionPagerAdapter = UserListPagerAdapter(childFragmentManager, pseudo)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = collectionPagerAdapter

        val tabLayout = view.findViewById(R.id.tab_layout) as TabLayout
        tabLayout.setupWithViewPager(viewPager)
    }
}

class UserListPagerAdapter(fm: FragmentManager, val pseudo: String) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int  = 2

    override fun getItem(i: Int): Fragment {
        val urlFollowers = "https://mobop.liatti.ch/user/followers?pseudo=$pseudo"
        val urlFollowing = "https://mobop.liatti.ch/user/following?pseudo=$pseudo"
        val text = when (i) {
            0 -> urlFollowing
            1 -> urlFollowers
            else -> "autre"
        }

        return UserCardFragment.newInstance(text)
    }

    override fun getPageTitle(position: Int): CharSequence {

        return when (position) {
            0 -> "Following"
            1 -> "Followers"
            else -> "OBJECT ${(position + 1)}"
        }
    }
}

class UserCardFragment : Fragment() {

    private val requestController = VolleyRequestController()
    private var listener: OnListFragmentInteractionListener? = null

    private val args: UserCardFragmentArgs by navArgs()

    private fun getUsers(URL: String, context: Context, callback: ServerCallback<ArrayList<User>>) {
        val users: ArrayList<User> = ArrayList()
        requestController.httpGet(URL, context, object : ServerCallback<JSONObject> {
            override fun onSuccess(result: JSONObject) {
                val jsArray = result.getJSONArray("pseudos")
                for (i in 0 until jsArray.length()) {
                    val jsObj = jsArray.getString(i)
                    users.add(User(jsObj, "", ""))
                    callback.onSuccess(users)
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val view = inflater.inflate(R.layout.generic_list_items, container, false)

        arguments?.getString("URL", args.query)?.let {
            getUsers(it, view.context, object : ServerCallback<ArrayList<User>> {
                override fun onSuccess(result: ArrayList<User>) {
                    val myAdapter = object : GenericAdapter<User>(result, listener) {
                        override fun getLayoutId(position: Int, obj: User): Int {
                            return R.layout.user_card
                        }

                        override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
                            return UserViewHolder(view)
                        }

                    }

                    if (view is RecyclerView) {
                        view.layoutManager = LinearLayoutManager(view.context)
                        view.setHasFixedSize(true)
                        view.adapter = myAdapter
                    }
                }
            })
        }
        return view
    }

    companion object {
        fun newInstance(url: String) =
            UserCardFragment().apply {
                arguments = Bundle().apply {
                    putString("URL", url)
                }
            }
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

class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), GenericAdapter.Binder<User> {

    private var pseudo: TextView = itemView.findViewById(R.id.userName)
    var view: View = itemView

    override fun bind(data: User, clickListener: View.OnClickListener) {
        pseudo.text = data.pseudo
        view.tag = data
        view.setOnClickListener(clickListener)
    }
}
