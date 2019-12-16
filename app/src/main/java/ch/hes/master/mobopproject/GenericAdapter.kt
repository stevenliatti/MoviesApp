package ch.hes.master.mobopproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import ch.hes.master.mobopproject.data.Movie
import ch.hes.master.mobopproject.data.People
import ch.hes.master.mobopproject.data.User

interface OnListFragmentInteractionListener {
    fun onListFragmentInteraction(item: Any, view: View) {
        var action = when (item) {
            is Movie  -> ListMoviesFragmentDirections.actionListMoviesFragmentToMovieDetailsFragment(item.id, item.urlImg)
            is People -> ListPeoplesFragmentDirections.actionListPeoplesFragmentToPeopleDetailsFragment(item.id, item.urlImg, item.knowFor)
            is User -> ListUsersFragmentDirections.actionUserListFragmentToListLikesMoviesFragment(item.pseudo)
            else -> ListMoviesFragmentDirections.actionListMoviesFragmentSelf()
        }
        if (view.findNavController().currentDestination!!.id == R.id.listLikesMoviesFragment && item is Movie){
            action = ListLikesMoviesFragmentDirections.actionListLikesMoviesFragmentToMovieDetailsFragment(item.id, item.urlImg)
        }

        if (view.findNavController().currentDestination!!.id == R.id.userCardFragment && item is User){
            action = UserCardFragmentDirections.actionUserCardFragmentToListLikesMoviesFragment(item.pseudo)
        }

        view.findNavController().navigate(action)
    }
}

abstract class GenericAdapter<T>: RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private var listItems: List<T>
    private val mOnClickListener: View.OnClickListener
    private lateinit var mListener: OnListFragmentInteractionListener

    constructor(listItems: List<T>, mListener: OnListFragmentInteractionListener?) {
        this.listItems = listItems
        this.mListener = mListener!!
    }

    constructor(listItems: List<T>) {
        this.listItems = listItems
    }

    constructor() {
        listItems = emptyList()
    }

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag
            mListener.onListFragmentInteraction(item, v)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return getViewHolder(LayoutInflater.from(parent.context)
            .inflate(viewType, parent, false)
            , viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as Binder<T>).bind(listItems[position], mOnClickListener)
    }

    override fun getItemCount(): Int = listItems.size

    override fun getItemViewType(position: Int): Int {
        return getLayoutId(position, listItems[position])
    }


    protected abstract fun getLayoutId(position: Int, obj: T): Int

    abstract fun getViewHolder(view: View, viewType: Int):RecyclerView.ViewHolder

    internal interface Binder<T> {
        fun bind(data: T, clickListener: View.OnClickListener)
    }

}
