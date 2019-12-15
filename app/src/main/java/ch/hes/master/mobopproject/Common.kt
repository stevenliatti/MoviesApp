package ch.hes.master.mobopproject

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.gridlayout.widget.GridLayout
import androidx.navigation.findNavController
import ch.hes.master.mobopproject.data.Item
import ch.hes.master.mobopproject.data.Movie
import ch.hes.master.mobopproject.data.People

object Common {

    private val requestController = VolleyRequestController()

    fun getGridItems(view: View, url: String, itemFrom: Item, itemTo: Item, grid: GridLayout) {
        requestController.getItems(url, itemFrom, itemTo, view.context, object : ServerCallback<ArrayList<Item>> {
            override fun onSuccess(result: ArrayList<Item>) {
                val total = result.size
                val columnsNumber = 3
                var row = 0
                var col = 0

                grid.columnCount = columnsNumber
                grid.rowCount = total / columnsNumber
                for (item in result) {
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
                        val action =
                            when (itemFrom) {
                                is People ->
                                    if (itemTo is Movie) {
                                        PeopleDetailsFragmentDirections
                                            .actionPeopleDetailsFragmentToMovieDetailsFragment(
                                                item.id,
                                                item.urlImg
                                            )
                                    }
                                    else {
                                        MovieDetailsFragmentDirections
                                            .actionMovieDetailsFragmentToPeopleDetailsFragment(
                                                item.id,
                                                item.urlImg,
                                                item.knowFor
                                            )
                                    }
                                else -> MovieDetailsFragmentDirections
                                    .actionListMoviesFragmentToMovieDetailsFragment(
                                        item.id,
                                        item.urlImg
                                    )
                            }
                        view.findNavController().navigate(action)
                    }

                    textView.text = croptext(item.nameTitle)
                    iv.setImageBitmap(item.img)

                    linearLayoutVertical.addView(iv)
                    linearLayoutVertical.addView(textView)

                    grid.addView(linearLayoutVertical, gridParam)
                    col++
                }
            }
        })
    }

    fun croptext(txt: String): String {
        val maxSize = 15
        if (txt.length > maxSize)
            return txt.substring(0, maxSize - 3) + "..."
        return txt
    }
}