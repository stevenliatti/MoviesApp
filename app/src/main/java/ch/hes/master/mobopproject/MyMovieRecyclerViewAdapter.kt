package ch.hes.master.mobopproject

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView


import ch.hes.master.mobopproject.MovieFragment.OnListFragmentInteractionListener
import ch.hes.master.mobopproject.data.Movie

/**
 * [RecyclerView.Adapter] that can display a [PopularMovies.Movie] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyMovieRecyclerViewAdapter(
    private val mValues: List<Movie>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyMovieRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Movie
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        // holder.idView.text = item.id.toString()
        holder.originalTitleView.text = item.originalTitle
        holder.overviewView.text = item.overview
        holder.img.setImageBitmap(item.img)

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        // val idView: TextView = mView.findViewById(R.id.movie_id)
        val originalTitleView: TextView = mView.findViewById(R.id.original_title)
        val overviewView: TextView = mView.findViewById(R.id.overview)
        val img: ImageView = mView.findViewById(R.id.img)

        override fun toString(): String {
            return super.toString() + " '" + overviewView.text + "'"
        }
    }
}
