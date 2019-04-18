package co.idwall.iddog

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

/**
 * A login screen that offers login via email.
 */
class LoginActivity : AppCompatActivity() {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private var mAuthTask: Request<JSONObject>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        button_sign_in.setOnClickListener { attemptLogin() }
    }

    override fun onResume() {
        super.onResume()
        if (Util.getToken(this) != null) {
            finish()
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {
        if (mAuthTask != null) {
            return
        }

        // Reset errors.
        email.error = null

        // Store values at the time of the login attempt.
        val emailStr = email.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            email.error = getString(R.string.error_field_required)
            focusView = email
            cancel = true
        } else if (!isEmailValid(emailStr)) {
            email.error = getString(R.string.error_invalid_email)
            focusView = email
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            Util.showProgress(this, true, progress, login_form)
            mAuthTask = API.getInstance(this).signup(
                emailStr,
                Response.Listener { response ->
                    mAuthTask = null

                    Util.setToken(this, response.getJSONObject("user").getString("token"))
                    finish()
                },
                Response.ErrorListener {
                    mAuthTask = null
                    Util.showProgress(this, false, progress, login_form)
                    email.error = getString(R.string.error_invalid_email)
                    focusView = email
                    focusView?.requestFocus()
                }
            )
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}
