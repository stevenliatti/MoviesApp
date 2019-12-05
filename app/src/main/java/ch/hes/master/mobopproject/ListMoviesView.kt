package ch.hes.master.mobopproject

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import ch.hes.master.mobopproject.ListMoviesFragment.OnListFragmentInteractionListener
import ch.hes.master.mobopproject.data.Movie

class ListMoviesView(
    private val mValues: List<Movie>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<ListMoviesView.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Movie
            mListener?.onListFragmentInteraction(item, v)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.titleView.text = item.title
        holder.overviewView.text = item.overview
        holder.img.setImageBitmap(item.img)

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val titleView: TextView = mView.findViewById(R.id.original_title)
        val overviewView: TextView = mView.findViewById(R.id.overview)
        val img: ImageView = mView.findViewById(R.id.img)

        override fun toString(): String {
            return super.toString() + " '" + overviewView.text + "'"
        }
    }
}
