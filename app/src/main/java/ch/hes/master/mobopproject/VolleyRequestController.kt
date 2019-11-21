package ch.hes.master.mobopproject
//
//import android.util.Log
//import ch.hes.master.mobopproject.data.Movie
//
//class VolleyRequestController {
//    fun VolleyRequest(URL: String, callback: ServerCallback) {
//        JsonObjectRequest jsonObjReq =
//
//
//    }
//}
//
//
//JsonObjectRequest(
//Request.Method.GET, url, null,
//Response.Listener { response ->
//    val fragMovie = MovieFragment.newInstance(1, movies)
//    val results = response.getJSONArray("results")
//    Log.println(Log.DEBUG, this.javaClass.name, "makeRequest : $results")
//    for (i in 0 until results.length()) {
//        val res = results.getJSONObject(i)
//        val movie = Movie(res.getInt("id"), res.getString("title"), res.getString("overview"), res.getString("poster_path"),null)
//        HttpQueue.getInstance(this).addToRequestQueue(Common.getImageInList(movie, fragMovie, i))
//        movies.add(movie)
//    }
//
//    supportFragmentManager
//        .beginTransaction()
//        .add(R.id.fragment_container, fragMovie, "moviefragment")
//        .addToBackStack("moviefragment")
//        .commit()
//},
//Response.ErrorListener { error ->
//    Log.println(Log.DEBUG, this.javaClass.name, "error in makeRequest : $error")
//}
//)
//
