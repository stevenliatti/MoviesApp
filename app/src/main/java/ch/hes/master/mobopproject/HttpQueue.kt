package ch.hes.master.mobopproject

import android.content.Context
import android.graphics.Bitmap
import android.util.LruCache
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.*
import java.io.File

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
    val imageLoader: ImageLoader by lazy {
        ImageLoader(requestQueue,
            object : ImageLoader.ImageCache {
                private val cache = LruCache<String, Bitmap>(20)
                override fun getBitmap(url: String): Bitmap {
                    return cache.get(url)
                }
                override fun putBitmap(url: String, bitmap: Bitmap) {
                    cache.put(url, bitmap)
                }
            })
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