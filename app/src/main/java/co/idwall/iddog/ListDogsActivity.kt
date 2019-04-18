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

    private var isLoading: Boolean = false
    private var token: String? = null
    private var category = "husky"
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        if (isLoading) {
            return@OnNavigationItemSelectedListener false
        }

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

        if (newCategory != category) {
            category = newCategory
            loadDogs()
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
        token = Util.getToken(this)

        if (token == null) {
            doLogin()
        } else {
            loadDogs()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.button_logout -> {
            doLogin()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    fun doLogin() {
        Util.setToken(this, null)
        startActivity(Intent(this, LoginActivity::class.java))
    }

    fun loadDogs() {
        isLoading = true
        Util.showProgress(this, true, progress, null)
        API.getInstance(this).feed(
            category,
            token!!,
            Response.Listener { response ->
                Util.showProgress(this, false, progress, null)
                val jsonArray = response.getJSONArray("list")
                val dogs = Array(jsonArray.length()) {
                    jsonArray.getString(it)
                }
                view_dogs.adapter = ListDogsAdapter(this, dogs)
                isLoading = false
            },
            Response.ErrorListener {
                Util.showProgress(this, false, progress, null)
                isLoading = false
            }
        )
    }
}
