package ch.hes.master.mobopproject

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import ch.hes.master.mobopproject.data.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

interface ServerCallback<T> {
    fun onSuccess(result: T)
}

class VolleyRequestController {
    fun httpGet(URL: String, context: Context, callback: ServerCallback<JSONObject>) {
        val jsonObjReq = JsonObjectRequest(Request.Method.GET, URL, null,
        Response.Listener { response ->
            callback.onSuccess(response) // call call back function here
        },
        Response.ErrorListener { error ->
            Log.println(Log.DEBUG, this.javaClass.name, "error in httpGet : $error,\n$URL\n$callback")
        })

        // Adding request to request queue
        HttpQueue.getInstance(context).addToRequestQueue(jsonObjReq)
    }

    fun httpPost(URL: String, jsonData: JSONObject, context: Context, callback: ServerCallback<JSONObject>) {
        val jsonObjReq = JsonObjectRequest(Request.Method.POST, URL, jsonData,
            Response.Listener { response ->
                callback.onSuccess(response) // call call back function here
            },
            Response.ErrorListener { error ->
                Log.println(Log.DEBUG, this.javaClass.name, "error in httpPost : $error,\n$URL\n$jsonData\n$callback")
            })

        // Adding request to request queue
        HttpQueue.getInstance(context).addToRequestQueue(jsonObjReq)
    }

    fun getItems(url: String, itemFrom: Item, itemTo: Item, context: Context, callback: ServerCallback<ArrayList<Item>>) {
        val items: ArrayList<Item> = ArrayList()
        httpGet(url, context, object : ServerCallback<JSONObject> {
            override fun onSuccess(response: JSONObject) {
                val resultsName = when (itemFrom) {
                    is People -> if (itemFrom.knowFor == "Acting") "cast" else "crew"
                    else -> "results"
                }

                val jsArray = response.getJSONArray(resultsName)
                for (i in 0 until jsArray.length()) {
                    val jsObj = jsArray.getJSONObject(i)
                    val path = when (itemTo) {
                        is People -> "profile_path"
                        else -> "poster_path"
                    }
                    getPosterImage(jsObj.getString(path), context, object : ServerCallback<Bitmap> {
                        override fun onSuccess(img: Bitmap) {
                            if (itemTo is Movie) {
                                items.add(Movie(
                                    jsObj.getInt("id"),
                                    jsObj.getString("title"),
                                    img,
                                    jsObj.getString("poster_path"),
                                    jsObj.getString("overview")
                                ))
                            }
                            if (itemTo is People) {
                                items.add(People(
                                    jsObj.getInt("id"),
                                    jsObj.getString("name"),
                                    img,
                                    jsObj.getString("profile_path"),
                                    "",
                                    listOf()
                                ))
                            }
                            if(i == jsArray.length()-1) {
                                callback.onSuccess(items)
                            }
                        }
                    })
                }
            }
        })
    }

    fun getMovies(URL: String, keyResult: String, context: Context, callback: ServerCallback<ArrayList<Movie>>) {
        val movies: ArrayList<Movie> = ArrayList()
        httpGet(URL, context, object : ServerCallback<JSONObject> {
            override fun onSuccess(response: JSONObject) {
                val jsArray = response.getJSONArray(keyResult)
                for (i in 0 until jsArray.length()) {
                    val jsObj = jsArray.getJSONObject(i)
                    getPosterImage(jsObj.getString("urlPath"), context, object : ServerCallback<Bitmap> {
                        override fun onSuccess(img: Bitmap) {
                            movies.add(Movie(jsObj.getInt("id"), jsObj.getString("title"), img, jsObj.getString("urlPath"), jsObj.getString("overview")))
                            if(i == jsArray.length()-1) {
                                callback.onSuccess(movies)
                            }
                        }
                    })
                }
            }
        })
    }

    fun getPosterImage(posterPath: String, context: Context, callback: ServerCallback<Bitmap>) {
        val url = "https://image.tmdb.org/t/p/w300$posterPath"
        val imgRequest = ImageRequest(url,
            Response.Listener { response ->
                callback.onSuccess(Bitmap.createBitmap(response))
            }, 0, 0, null,
            Response.ErrorListener { error ->
                callback.onSuccess(Bitmap.createBitmap(42, 42, Bitmap.Config.ALPHA_8))
            })

        HttpQueue.getInstance(context).addToRequestQueue(imgRequest)
    }

    fun setImageView(urlImg: String?, image: ImageView, width: Int, context: Context) {
        val url = "https://image.tmdb.org/t/p/w$width/$urlImg"

        val imgRequest = ImageRequest(url,
            Response.Listener { response ->
                val img = Bitmap.createBitmap(response)
                image.setImageBitmap(img)
            }, 0, 0, null, null)

        HttpQueue.getInstance(context).addToRequestQueue(imgRequest)
    }
}