package ch.hes.master.mobopproject

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import ch.hes.master.mobopproject.data.*
import ch.hes.master.mobopproject.data.Movie
import ch.hes.master.mobopproject.data.MovieDetails
import ch.hes.master.mobopproject.data.User
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONArray
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

    fun getPeoples(URL: String, context: Context, callback: ServerCallback<ArrayList<People>>) {
        val peoples: ArrayList<People> = ArrayList()
        httpGet(URL, context, object : ServerCallback<JSONObject> {
            override fun onSuccess(response: JSONObject) {
                val jsArray = response.getJSONArray("results")
                for (i in 0 until jsArray.length()) {
                    val jsObj = jsArray.getJSONObject(i)
                    getPosterImage(jsObj.getString("profile_path"), context, object : ServerCallback<Bitmap> {
                        override fun onSuccess(img: Bitmap) {
                            val knownForJson = jsObj.getJSONArray("known_for")
                            val knownFor: ArrayList<Movie> = ArrayList()
                            for (j in 0 until knownForJson.length()) {
                                val propsMovie = knownForJson.getJSONObject(j)
                                if (propsMovie.getString("media_type") == "movie") {
                                    knownFor.add(Movie(
                                        propsMovie.getInt("id"),
                                        propsMovie.getString("title"),
                                        null,
                                        propsMovie.getString("poster_path"),
                                        propsMovie.getString("overview")))
                                }
                            }

                            peoples.add(People(
                                jsObj.getInt("id"),
                                jsObj.getString("name"),
                                img,
                                jsObj.getString("profile_path"),
                                jsObj.getString("known_for_department"),
                                knownFor))

                            if(i == jsArray.length()-1) {
                                callback.onSuccess(peoples)
                            }
                        }
                    })
                }
            }
        })
    }

    fun getMovieDetails(URL: String, context: Context, callback: ServerCallback<MovieDetails>) {
        httpGet(URL, context, object : ServerCallback<JSONObject> {
            override fun onSuccess(res: JSONObject) {
                val movieDetails = MovieDetails(
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
                callback.onSuccess(movieDetails)
            }
        })
    }

    fun getPeopleDetails(URL: String, context: Context, callback: ServerCallback<PeopleDetails>) {
        httpGet(URL, context, object : ServerCallback<JSONObject> {
            override fun onSuccess(res: JSONObject) {
                val peopleDetails = PeopleDetails(
                    res.getInt("id"),
                    res.getString("name"),
                    res.getString("biography"),
                    res.getString("known_for_department"),
                    res.getDouble("popularity"),
                    res.getString("birthday"),
                    res.getString("place_of_birth"),
                    res.getString("deathday"),
                    res.getString("gender"),
                    res.getString("homepage")
                )
                callback.onSuccess(peopleDetails)
            }
        })
    }

    fun getUsers(URL: String, context: Context, callback: ServerCallback<ArrayList<User>>) {
        val users: ArrayList<User> = ArrayList()
        httpGet(URL, context, object : ServerCallback<JSONObject> {
            override fun onSuccess(response: JSONObject) {
                val jsArray = response.getJSONArray("pseudos")
                for (i in 0 until jsArray.length()) {
                    val jsObj = jsArray.getString(i)
                    users.add(User(jsObj, "", ""))
                    callback.onSuccess(users)
                }
            }
        })
    }

    fun getMovies(URL: String, keyresult: String, context: Context, callback: ServerCallback<ArrayList<Movie>>) {
        val movies: ArrayList<Movie> = ArrayList()
        volleyRequest(URL, context, object : ServerCallback<JSONObject> {
            override fun onSuccess(response: JSONObject) {
                val jsArray = response.getJSONArray(keyresult)
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
            }, 0, 0, null,
            Response.ErrorListener { error ->
                callback.onSuccess(Bitmap.createBitmap(42, 42, Bitmap.Config.ALPHA_8))
            })

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