package ch.hes.master.mobopproject

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import ch.hes.master.mobopproject.data.Movie
import com.google.android.material.tabs.TabLayout

class ListLikesMoviesFragment : Fragment() {
    private lateinit var collectionPagerAdapter: LikeMoviesPagerAdapter
    private lateinit var viewPager: ViewPager

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_user_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        collectionPagerAdapter = LikeMoviesPagerAdapter(childFragmentManager)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = collectionPagerAdapter

        val tabLayout = view.findViewById(R.id.tab_layout) as TabLayout
        tabLayout.setupWithViewPager(viewPager)
    }

}

class LikeMoviesPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int  = 2

    override fun getItem(i: Int): Fragment {
        val user = "max"
        val urlLikes = "https://mobop.liatti.ch/user/likes?pseudo=$user"
        val urlDislikes = "https://mobop.liatti.ch/user/dislikes?pseudo=$user"
        val text = when (i) {
            0 -> urlLikes
            1 -> urlDislikes
            else -> "other"
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

class MovieLikesCardFragment(private var url: String) : Fragment() {

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val view = inflater.inflate(R.layout.generic_list_items, container, false)

        Common.getMovies(url, "movies", "urlPath", view.context, object : ServerCallback<ArrayList<Movie>> {
            override fun onSuccess(result: ArrayList<Movie>) {
                val lmv = ListMoviesRecyclerView()
                lmv.setView(result, view as RecyclerView, listener)
            }
        })
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