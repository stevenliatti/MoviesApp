package ch.hes.master.mobopproject

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
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