package ch.hes.master.mobopproject

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import ch.hes.master.mobopproject.data.Movie
import com.google.android.material.tabs.TabLayout

class ListLikesMoviesFragment : Fragment() {
    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
    val args: ListLikesMoviesFragmentArgs by navArgs()
    private lateinit var demoCollectionPagerAdapter: LikeMoviestPagerAdapter
    private lateinit var viewPager: ViewPager

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_user_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val pseudo = if(args.pseudo != null) args.pseudo!! else "bob"
        demoCollectionPagerAdapter = LikeMoviestPagerAdapter(childFragmentManager, pseudo)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = demoCollectionPagerAdapter

        val tabLayout = view.findViewById(R.id.tab_layout) as TabLayout
        tabLayout.setupWithViewPager(viewPager)
    }
}

class LikeMoviestPagerAdapter(fm: FragmentManager, pseudo: String) : FragmentPagerAdapter(fm) {

    private val user = pseudo

    override fun getCount(): Int  = 2

    override fun getItem(i: Int): Fragment {
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

        requestController.getMovies(url, "movies",view.context, object : ServerCallback<ArrayList<Movie>> {
            override fun onSuccess(movies: ArrayList<Movie>) {
                val lmv = ListMoviesRecyclerView()
                lmv.setViewMv(movies, view as RecyclerView, listener)
            }
        })
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}