package ch.hes.master.mobopproject

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.gridlayout.widget.GridLayout
import androidx.navigation.findNavController
import ch.hes.master.mobopproject.data.Movie
import org.json.JSONObject

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

    fun getMoviesGrid(view: View, url: String, keyResult: String, keyPath: String, grid: GridLayout) {
        getMovies(url, keyResult, keyPath, view.context, object : ServerCallback<ArrayList<Movie>> {
            override fun onSuccess(result: ArrayList<Movie>) {
                val total = result.size
                val columnsNumber = 3
                var row = 0
                var col = 0

                grid.columnCount = columnsNumber
                grid.rowCount = if (total / columnsNumber <= 0) 1 else total / columnsNumber
                for (movie in result) {
                    if (col == columnsNumber) {
                        col = 0
                        row++
                    }

                    val rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 1)
                    val colSpan = GridLayout.spec(GridLayout.UNDEFINED, 1)
                    val gridParam: GridLayout.LayoutParams = GridLayout.LayoutParams(rowSpan, colSpan)

                    val textView = TextView(view.context)
                    val iv = ImageView(view.context)
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
                    iv.layoutParams = lp

                    iv.setOnClickListener {
                        val action = if (keyResult == "cast" || keyResult == "crew") {
                            PeopleDetailsFragmentDirections
                                .actionPeopleDetailsFragmentToMovieDetailsFragment(
                                    movie.id,
                                    movie.urlImg
                                )
                        }
                        else {
                            MovieDetailsFragmentDirections
                                .actionListMoviesFragmentToMovieDetailsFragment(movie.id, movie.urlImg)
                        }
                        view.findNavController().navigate(action)
                    }

                    textView.text = cropText(movie.nameTitle)
                    iv.setImageBitmap(movie.img)

                    linearLayoutVertical.addView(iv)
                    linearLayoutVertical.addView(textView)

                    grid.addView(linearLayoutVertical, gridParam)
                    col++
                }
            }
        })
    }

    fun cropText(txt: String): String {
        val maxSize = 15
        if (txt.length > maxSize)
            return txt.substring(0, maxSize - 3) + "..."
        return txt
    }
}