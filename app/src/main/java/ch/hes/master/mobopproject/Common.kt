package ch.hes.master.mobopproject

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.gridlayout.widget.GridLayout
import androidx.navigation.findNavController
import ch.hes.master.mobopproject.data.Movie

object Common {

    private val requestController = VolleyRequestController()

    fun getGridMovies(view: View, url: String, resultsName: String, grid: GridLayout) {
        requestController.getMovies(url, resultsName, view.context, object : ServerCallback<ArrayList<Movie>> {
            override fun onSuccess(result: ArrayList<Movie>) {
                val total = result.size
                val columnsNumber = 3
                var row = 0
                var col = 0

                grid.columnCount = columnsNumber
                grid.rowCount = total / columnsNumber
                for (movie in result) {
                    if (col == columnsNumber) {
                        col = 0
                        row++
                    }

                    val rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 1)
                    val colspan = GridLayout.spec(GridLayout.UNDEFINED, 1)

                    val gridParam: GridLayout.LayoutParams =
                        GridLayout.LayoutParams(rowSpan, colspan)

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
                        // TODO: find a better way ...
                        val action =
                            if (resultsName == "results") MovieDetailsFragmentDirections
                                .actionListMoviesFragmentToMovieDetailsFragment(movie.id, movie.urlImg)
                            else {
                                PeopleDetailsFragmentDirections
                                    .actionPeopleDetailsFragmentToMovieDetailsFragment(movie.id, movie.urlImg)
                            }
                        view.findNavController().navigate(action)
                    }

                    textView.text = croptext(movie.title)
                    iv.setImageBitmap(movie.img)

                    linearLayoutVertical.addView(iv)
                    linearLayoutVertical.addView(textView)

                    grid.addView(linearLayoutVertical, gridParam)
                    col++
                }
            }
        })
    }

    private fun croptext(txt: String): String {
        val maxSize = 15
        if (txt.length > maxSize)
            return txt.substring(0, maxSize - 3) + "..."
        return txt
    }
}