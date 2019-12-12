package ch.hes.master.mobopproject

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.hes.master.mobopproject.data.Item
import ch.hes.master.mobopproject.data.Movie

class ListMoviesRecyclerView {

    class ListMoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), GenericAdapter.Binder<Movie> {

        var title: TextView = itemView.findViewById(R.id.original_title)
        var overviewView: TextView = itemView.findViewById(R.id.overview)
        var img: ImageView = itemView.findViewById(R.id.img)
        var view: View = itemView

        override fun bind(m: Movie, clickListener: View.OnClickListener) {
            title.text = m.nameTitle
            overviewView.text = m.overview
            img.setImageBitmap(m.img)

            view.tag = m
            view.setOnClickListener(clickListener)
        }
    }

    fun setView(movies: ArrayList<Item>, view: RecyclerView, listener: OnListFragmentInteractionListener?) {
        val myAdapter = object : GenericAdapter<Item>(movies, listener) {
            override fun getLayoutId(position: Int, obj: Item): Int {
                return R.layout.fragment_movie
            }

            override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
                return ListMoviesViewHolder(view)
            }
        }
        view.layoutManager= LinearLayoutManager(view.context)
        view.setHasFixedSize(true)
        view.adapter=myAdapter

    }

    fun setViewMv(movies: ArrayList<Movie>, view: RecyclerView, listener: OnListFragmentInteractionListener?) {
        val myAdapter = object : GenericAdapter<Movie>(movies, listener) {
            override fun getLayoutId(position: Int, obj: Movie): Int {
                return R.layout.fragment_movie
            }

            override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
                return ListMoviesViewHolder(view)
            }
        }
        view.layoutManager= LinearLayoutManager(view.context)
        view.setHasFixedSize(true)
        view.adapter=myAdapter

    }

}