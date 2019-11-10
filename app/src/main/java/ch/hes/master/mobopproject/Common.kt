package ch.hes.master.mobopproject

import android.graphics.Bitmap
import android.widget.ImageView
import ch.hes.master.mobopproject.data.Movie
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest

object Common {

    fun setImage(urlImg: String?, image: ImageView, width: Int): ImageRequest {
        val url = "https://image.tmdb.org/t/p/w" + width + "/" + urlImg

        return ImageRequest(url,
            Response.Listener { response ->
                val img = Bitmap.createBitmap(response)
                image.setImageBitmap(img)
            }, 0, 0, null, null)

    }

    fun getImageInList(movie: Movie, frag: MovieFragment, i: Int): ImageRequest {
        val url = "https://image.tmdb.org/t/p/w300" + movie.urlImg

        return ImageRequest(url,
            Response.Listener { response ->
                val img = Bitmap.createBitmap(response)
                movie.img = img
                frag.updateCell(i)
            }, 0, 0, null, null)

    }
}