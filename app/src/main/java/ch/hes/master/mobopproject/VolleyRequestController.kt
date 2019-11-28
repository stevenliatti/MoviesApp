package ch.hes.master.mobopproject

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import ch.hes.master.mobopproject.data.Movie
import ch.hes.master.mobopproject.data.MvDetails
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONArray
import org.json.JSONObject

class VolleyRequestController {
    fun volleyRequest(URL: String, context: Context, callback: ServerCallback<JSONObject>) {
        val jsonObjReq = JsonObjectRequest(Request.Method.GET, URL, null,
        Response.Listener { response ->
            callback.onSuccess(response) // call call back function here
        },
        Response.ErrorListener { error ->
            Log.println(Log.DEBUG, this.javaClass.name, "error in makeRequest : $error")
        })

        // Adding request to request queue
        HttpQueue.getInstance(context).addToRequestQueue(jsonObjReq)
    }

    fun getMovies(URL: String, context: Context, callback: ServerCallback<ArrayList<Movie>>) {
        val movies: ArrayList<Movie> = ArrayList()
        volleyRequest(URL, context, object : ServerCallback<JSONObject> {
            override fun onSuccess(response: JSONObject) {
                val jsArray = response.getJSONArray("results")
                for (i in 0 until jsArray.length()) {
                    val jsObj = jsArray.getJSONObject(i)
                    getPosterImage(jsObj.getString("poster_path"), context, object : ServerCallback<Bitmap> {
                        override fun onSuccess(img: Bitmap) {
                            movies.add(Movie(jsObj.getInt("id"), jsObj.getString("title"), jsObj.getString("overview"), jsObj.getString("poster_path"), img))
                            if(i == jsArray.length()-1) {
                                callback.onSuccess(movies)
                            }
                        }
                    })
                }
            }
        })
    }

    fun getMovieDetails(URL: String, context: Context, callback: ServerCallback<MvDetails>) {
        volleyRequest(URL, context, object : ServerCallback<JSONObject> {
            override fun onSuccess(res: JSONObject) {
                val mvDetails = MvDetails(
                    res.getInt("id"),
                    res.getString("title"),
                    res.getString("overview"),
                    buildStringListFromJsonSArray(res.getJSONArray("genres"), "name"),
                    res.getDouble("popularity"),
                    buildStringListFromJsonSArray(res.getJSONArray("production_countries"), "name"),
                    res.getString("release_date"),
                    res.getString("tagline"),
                    res.getInt("vote_count")
                )
                callback.onSuccess(mvDetails)
            }
        })
    }

    private fun buildStringListFromJsonSArray(jsonArray: JSONArray, key: String): ArrayList<String> {
        val strings: ArrayList<String> = ArrayList()
        for (i in 0 until jsonArray.length()) {
            val res = jsonArray.getJSONObject(i)
            strings.add(res.getString(key))
        }
        return strings
    }


    fun getPosterImage(posterPath: String, context: Context, callback: ServerCallback<Bitmap>) {
        val url = "https://image.tmdb.org/t/p/w300" + posterPath
        val imgRequest = ImageRequest(url,
            Response.Listener { response ->
                callback.onSuccess(Bitmap.createBitmap(response))
            }, 0, 0, null, null)

        HttpQueue.getInstance(context).addToRequestQueue(imgRequest)
    }

    fun setImageView(urlImg: String?, image: ImageView, width: Int, context: Context) {
        val url = "https://image.tmdb.org/t/p/w" + width + "/" + urlImg

        val imgRequest = ImageRequest(url,
            Response.Listener { response ->
                val img = Bitmap.createBitmap(response)
                image.setImageBitmap(img)
            }, 0, 0, null, null)

        HttpQueue.getInstance(context).addToRequestQueue(imgRequest)
    }

    fun setYoutubeImageView(youtubeId: String?, image: ImageView, context: Context) {
        val url = "https://img.youtube.com/vi/" + youtubeId + "/1.jpg"

        val imgRequest = ImageRequest(url,
            Response.Listener { response ->
                val img = Bitmap.createBitmap(response)
                image.setImageBitmap(img)
            }, 0, 0, null, null)

        HttpQueue.getInstance(context).addToRequestQueue(imgRequest)
    }
}