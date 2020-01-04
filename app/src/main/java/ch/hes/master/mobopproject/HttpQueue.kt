package ch.hes.master.mobopproject

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack

class HttpQueue constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: HttpQueue? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: HttpQueue(context).also {
                    INSTANCE = it
                }
            }
    }

    // Instantiate the cache
    private val cache = DiskBasedCache(context.cacheDir, 1024 * 1024 * 100)

    // Set up the network to use HttpURLConnection as the HTTP client.
    private val network = BasicNetwork(HurlStack())

    // Instantiate the RequestQueue with the cache and network. Start the queue.
    private val requestQueue = RequestQueue(cache, network).apply {
        start()
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }
}