package co.idwall.iddog

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Response
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_list_dogs.*

class ListDogsActivity : AppCompatActivity() {

    /**
     * Keep track if dogs listing is loading
     */
    private var isLoading: Boolean = false

    /**
     * Hold user token on activity start
     */
    private var token: String? = null

    /**
     * Hold current selected category on activity start
     */
    private var category = "husky"
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        if (isLoading) {
            return@OnNavigationItemSelectedListener false
        }

            // Verify the new chosen category
        var newCategory = category
        when (item.itemId) {
            R.id.navigation_husky -> {
                newCategory = "husky"
            }
            R.id.navigation_hound -> {
                newCategory = "hound"
            }
            R.id.navigation_pug -> {
                newCategory = "pug"
            }
            R.id.navigation_labrador -> {
                newCategory = "labrador"
            }
        }

            // Verify if was selected a different category
        if (newCategory != category) {
                category = newCategory // Change the current selected category
                loadDogs() // Load the dogs for the new selected category
            return@OnNavigationItemSelectedListener true
        }

        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_dogs)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        view_dogs.layoutManager = GridLayoutManager(this, 4)
//        view_dogs.layoutManager = StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
    }

    override fun onResume() {
        super.onResume()
        token = Util.getToken(this) // Get user token

        if (token == null) { // If not has token (no user logged)
            doLogin() // Do login
        } else { // If has token (user logged)
            loadDogs() // Load dogs
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Setting action bar
        menuInflater.inflate(R.menu.action_bar, menu)
        return true
    }

    // Action bar clicked
    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.button_logout -> { // Action bar logout button
            doLogin() // Logout user and go to login activity
            true
        }
        else -> super.onOptionsItemSelected(item)
    }


    /**
     * Go to login activity, removing any token saved
     */
    private fun doLogin() {
        Util.setToken(this, null) // Remove any token save
        startActivity(Intent(this, LoginActivity::class.java)) // Go to login
    }

    /**
     * Load dogs for selected category from API
     */
    private fun loadDogs() {
        isLoading = true // Set the is loading

        Util.showProgress(this, true, progress, null) // Show to the user that is loading

        // Do API request
        API.getInstance(this).feed(
            category,
            token!!,
            Response.Listener { response -> // On success response
                val jsonArray = response.getJSONArray("list") // Get list of dogs from response

                // Iterate over the list of JsonObject to transform in list of string (url of image)
                val dogs = Array(jsonArray.length()) {
                    jsonArray.getString(it)
                }
                // Set the list of dogs in RecyclerView using the adapter
                view_dogs.adapter = ListDogsAdapter(this, dogs)

                // Remove loading
                Util.showProgress(this, false, progress, null)
                isLoading = false
            },
            Response.ErrorListener { // On error
                // TODO: Show error message to the user

                // Remove loading
                Util.showProgress(this, false, progress, null)
                isLoading = false
            }
        )
    }
}
