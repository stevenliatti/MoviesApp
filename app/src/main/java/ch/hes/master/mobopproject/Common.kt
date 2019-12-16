package ch.hes.master.mobopproject

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.gridlayout.widget.GridLayout
import androidx.navigation.findNavController
import ch.hes.master.mobopproject.data.Auth
import ch.hes.master.mobopproject.data.Item
import ch.hes.master.mobopproject.data.Movie
import org.json.JSONObject

enum class From {
    MOVIE, PEOPLE
}

object Common {

    private val requestController = VolleyRequestController()

    fun getMovies(url: String, keyResult: String, keyPath: String, context: Context, callback: ServerCallback<ArrayList<Movie>>) {
        val movies: ArrayList<Movie> = ArrayList()
        requestController.httpGet(url, context, object : ServerCallback<JSONObject> {
            override fun onSuccess(result: JSONObject) {
                val jsArray = result.getJSONArray(keyResult)
                for (i in 0 until jsArray.length()) {
                    val jsObj = jsArray.getJSONObject(i)
                    requestController.getPosterImage(jsObj.getString(keyPath), context, object : ServerCallback<Bitmap> {
                        override fun onSuccess(img: Bitmap) {
                            movies.add(Movie(
                                jsObj.getInt("id"),
                                jsObj.getString("title"),
                                img,
                                jsObj.getString(keyPath),
                                jsObj.getString("overview")
                            ))
                            if (i == jsArray.length()-1) callback.onSuccess(movies)
                        }
                    })
                }
            }
        })
    }

    fun makeGridMovieCell(view: View, item: Item, from: From): LinearLayout {
        val linearLayoutVertical = LinearLayout(view.context)
        linearLayoutVertical.layoutParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        linearLayoutVertical.orientation = LinearLayout.VERTICAL

        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lp.setMargins(10, 0, 10, 0)

        val iv = ImageView(view.context)
        iv.layoutParams = lp
        iv.setImageBitmap(item.img)
        iv.setOnClickListener {
            val action = when (from) {
                From.MOVIE -> MovieDetailsFragmentDirections
                    .actionListMoviesFragmentToMovieDetailsFragment(item.id, item.urlImg)
                From.PEOPLE -> PeopleDetailsFragmentDirections
                    .actionPeopleDetailsFragmentToMovieDetailsFragment(item.id, item.urlImg)
            }
            view.findNavController().navigate(action)
        }

        val name = TextView(view.context)
        name.text = cropText(item.nameTitle)

        linearLayoutVertical.addView(iv)
        linearLayoutVertical.addView(name)

        return linearLayoutVertical
    }

    fun makeGridOf(view: View, items: List<Item>, grid: GridLayout, from: From,
                   makeCell: (view: View, item: Item, from: From) -> LinearLayout) {
        val total = items.size
        val columnsNumber = 3
        var row = 0
        var col = 0

        grid.columnCount = columnsNumber
        grid.rowCount = if (total / columnsNumber <= 0) 1 else total / columnsNumber
        for (item in items) {
            if (col == columnsNumber) {
                col = 0
                row++
            }

            val rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 1)
            val colSpan = GridLayout.spec(GridLayout.UNDEFINED, 1)
            val gridParam: GridLayout.LayoutParams = GridLayout.LayoutParams(rowSpan, colSpan)

            grid.addView(makeCell(view, item, from), gridParam)
            col++
        }
    }

    fun getMoviesGrid(view: View, url: String, keyResult: String, keyPath: String, grid: GridLayout,
                      from: From, makeCell: (view: View, item: Item, from: From) -> LinearLayout) {
        getMovies(url, keyResult, keyPath, view.context, object : ServerCallback<ArrayList<Movie>> {
            override fun onSuccess(result: ArrayList<Movie>) {
                makeGridOf(view, result, grid, from, makeCell)
            }
        })
    }

    fun cropText(txt: String): String {
        val maxSize = 15
        if (txt.length > maxSize)
            return txt.substring(0, maxSize - 3) + "..."
        return txt
    }

    fun getAuth(sharedPref: SharedPreferences, context: Context): Auth? {
        val defaultValue = ""
        val pseudo = sharedPref.getString(context.getString(R.string.pseudo), defaultValue)
        val email = sharedPref.getString(context.getString(R.string.email), defaultValue)
        val token = sharedPref.getString(context.getString(R.string.token), defaultValue)
        if(token != "" && pseudo != null && email != null) {
            return Auth(pseudo, email, token!!)
        }
        return null
    }
}