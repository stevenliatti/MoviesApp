package ch.hes.master.mobopproject

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import ch.hes.master.mobopproject.data.Item
import ch.hes.master.mobopproject.data.Movie
import com.google.android.material.tabs.TabLayout

class ListLikesMoviesFragment : Fragment() {
    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
    private lateinit var demoCollectionPagerAdapter: LikeMoviestPagerAdapter
    private lateinit var viewPager: ViewPager

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_user_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        demoCollectionPagerAdapter = LikeMoviestPagerAdapter(childFragmentManager)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = demoCollectionPagerAdapter

        val tabLayout = view.findViewById(R.id.tab_layout) as TabLayout
        tabLayout.setupWithViewPager(viewPager)
    }

}

class LikeMoviestPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int  = 2

    override fun getItem(i: Int): Fragment {
        val user = "max"
        val urlLikes = "https://mobop.liatti.ch/user/likes?pseudo=$user"
        val urlDislikes = "https://mobop.liatti.ch/user/dislikes?pseudo=$user"
        val text = when (i) {
            0 -> urlLikes
            1 -> urlDislikes
            else -> "autre"
        }
        return MovieLikesCardFragment(text)
    }

    override fun getPageTitle(position: Int): CharSequence {

        return when (position) {
            0 -> "Likes"
            1 -> "Dislikes"
            else -> "OBJECT ${(position + 1)}"
        }
    }
}

// Instances of this class are fragments representing a single
// object in our collection.
class MovieLikesCardFragment(private var url: String) : Fragment() {

    private val requestController = VolleyRequestController()
    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val view = inflater.inflate(R.layout.generic_list_items, container, false)

        val movie = Movie(42, "bob", Bitmap.createBitmap(42,42, Bitmap.Config.ALPHA_8), "", "")

        requestController.getItems(url, movie, movie, view.context, object : ServerCallback<ArrayList<Item>> { // TODO: REVOIR CELA (Movie)
            override fun onSuccess(movies: ArrayList<Item>) {
                val lmv = ListMoviesRecyclerView()
                lmv.setView(movies, view as RecyclerView, listener)
            }
        })
        return view
    }
}