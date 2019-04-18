package co.idwall.iddog

import android.content.Context
import androidx.test.espresso.IdlingResource
import com.android.volley.RequestQueue
import java.lang.reflect.Field

class VolleyIdlingResource constructor(private val resourceName: String, private val context: Context) : IdlingResource {

    private var requestQueue: RequestQueue? = null
    private var requests: Field? = null
    private var resourceCallback: IdlingResource.ResourceCallback? = null

    init {
        requestQueue = API.getInstance(context).requestQueue
        requests = RequestQueue::class.java.getDeclaredField("mCurrentRequests")
        requests?.isAccessible = true
    }

    override fun getName(): String {
        return resourceName
    }

    override fun isIdleNow(): Boolean {
        API.getInstance(context).requestQueue.sequenceNumber
        try {
            val set = requests?.get(requestQueue)
            if (set is Set<*>) {
                val count = set.size
                if (count == 0) {
                    resourceCallback!!.onTransitionToIdle()
                } else {
                }
                return count == 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return true
    }

    override fun registerIdleTransitionCallback(resourceCallback: IdlingResource.ResourceCallback) {
        this.resourceCallback = resourceCallback
    }

}