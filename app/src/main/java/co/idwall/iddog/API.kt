package co.idwall.iddog

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.collection.LruCache
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


class API constructor(context: Context) {

    private var URL_BASE: String = "https://api-iddog.idwall.co/"


    companion object {
        @Volatile
        private var INSTANCE: API? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: API(context).also {
                    INSTANCE = it
                }
            }
    }

    /**
     * ImageLoader with cache and RequestQueue to use with NetworkImageView
     */
    val imageLoader: ImageLoader by lazy {
        ImageLoader(requestQueue,
            object : ImageLoader.ImageCache {
                private val cache = LruCache<String, Bitmap>(20)
                override fun getBitmap(url: String): Bitmap? {
                    return cache.get(url)
                }
                override fun putBitmap(url: String, bitmap: Bitmap) {
                    cache.put(url, bitmap)
                }
            })
    }

    /**
     * RequestQueue to queue network request
     */
    val requestQueue: RequestQueue by lazy {
        // applicationContext is key, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(context.applicationContext)
    }

    /**
     * Add the request to the RequestQueue
     * @param req The request to be add
     */
    private fun <T> addToRequestQueue(req: Request<T>): Request<T> {
        return requestQueue.add(req)
    }

    /**
     * Make request to API
     *
     * @param method Method to use
     * @param path The path requested
     * @param [query] Query to send
     * @param [body] Body to send
     * @param [token] Authorization token to send
     * @param [onSuccess] Listener to receive success response
     * @param [onError] Listener to receive error response
     */
    private fun request(method: Int, path: String, query: String?, body: JSONObject?, token: String?, onSuccess: Response.Listener<JSONObject>?, onError: Response.ErrorListener?): Request<JSONObject> {
        Log.d("API", "Request started: $method - $path")
        val req = object : JsonObjectRequest(
            method,
            listOfNotNull(URL_BASE, path, query).joinToString(""),
            body,
            Response.Listener { response ->
                Log.d("API", "Request finished with success: $method - $path")
                onSuccess?.onResponse(response)
            },
            Response.ErrorListener { response ->
                Log.d("API", "Request finished with error: $method - $path")
                onError?.onErrorResponse(response)
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                if (token != null) {
                    headers["Authorization"] = token
                }
                return headers
            }
        }

        return this.addToRequestQueue(req)
    }

    /**
     * Send POST /signup request to API
     *
     * @param email The user e-mail
     * @param onSuccess Listener to receive success response
     * @param [onError] Listener to receive error response
     */
    fun signup(email: String, onSuccess: Response.Listener<JSONObject>, onError: Response.ErrorListener?): Request<JSONObject> {
        val data = JSONObject()
        data.put("email", email)

        return this.request(
            Request.Method.POST,
            "signup",
            null,
            data,
            null,
            onSuccess,
            onError
        )
    }

    /**
     * Send GET /feed request to API
     *
     * @param category The category to send
     * @param token The authorization token to send
     * @param onSuccess Listener to receive success response
     * @param [onError] Listener to receive error response
     */
    fun feed(category: String, token: String, onSuccess: Response.Listener<JSONObject>, onError: Response.ErrorListener?): Request<JSONObject> {
        return this.request(
            Request.Method.GET,
            "feed",
            "?category=$category",
            null,
            token,
            onSuccess,
            onError
        )
    }

}