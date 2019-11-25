package ch.hes.master.mobopproject

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import ch.hes.master.mobopproject.data.Movie
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

class VolleyRequestController {
    fun VolleyRequest(URL: String, context: Context, callback: ServerCallback<JSONObject>) {
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

    fun getPosterImage(posterPath: String, context: Context, callback: ServerCallback<Bitmap>) {
        val url = "https://image.tmdb.org/t/p/w300" + posterPath
        val imgRequest = ImageRequest(url,
            Response.Listener { response ->
                callback.onSuccess(Bitmap.createBitmap(response))
            }, 0, 0, null, null)

        HttpQueue.getInstance(context).addToRequestQueue(imgRequest)
    }
}