package ch.hes.master.mobopproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import ch.hes.master.mobopproject.data.User
import com.google.android.material.tabs.TabLayout


class ListUsersFragment : Fragment() {
    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
    private lateinit var demoCollectionPagerAdapter: UserListPagerAdapter
    private lateinit var viewPager: ViewPager

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_user_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        demoCollectionPagerAdapter = UserListPagerAdapter(childFragmentManager)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = demoCollectionPagerAdapter

        val tabLayout = view.findViewById(R.id.tab_layout) as TabLayout
        tabLayout.setupWithViewPager(viewPager)
    }
}

// Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
class UserListPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int  = 2

    override fun getItem(i: Int): Fragment {
        val user = "max"
        val urlFollowers = "https://mobop.liatti.ch/user/followers?pseudo=$user"
        val urlFollowing = "https://mobop.liatti.ch/user/following?pseudo=$user"
        val text = when (i) {
            0 -> urlFollowing
            1 -> urlFollowers
            else -> "autre"
        }
        return UserCardFragment(text)
    }

    override fun getPageTitle(position: Int): CharSequence {

        return when (position) {
            0 -> "Following"
            1 -> "Followers"
            else -> "OBJECT ${(position + 1)}"
        }
    }
}

// Instances of this class are fragments representing a single
// object in our collection.
class UserCardFragment(private var url: String) : Fragment() {

    private val requestController = VolleyRequestController()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val view = inflater.inflate(R.layout.generic_list_items, container, false)

        requestController.getUsers(url, view.context, object : ServerCallback<ArrayList<User>> {
            override fun onSuccess(users: ArrayList<User>) {

                val myAdapter = object : GenericAdapter<User>(users) {
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
        return view
    }
}

class UserViewHolder : RecyclerView.ViewHolder, GenericAdapter.Binder<User> {

    var pseudo: TextView
    var view: View
    constructor( itemView: View):super(itemView){
        pseudo = itemView.findViewById(R.id.userName)
        view = itemView
    }
    override fun bind(user: User, clickListener: View.OnClickListener) {
        pseudo.text = user.pseudo
        view.tag = user
    }
}
