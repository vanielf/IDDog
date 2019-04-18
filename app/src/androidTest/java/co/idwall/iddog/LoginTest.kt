package co.idwall.iddog

import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginTest {

    companion object {

        private const val USER_EMAIL = "vanielf@gmail.com"
        private const val WRONG_EMAIL = "vanielf@"

        private const val ERROR_LOGIN = "This email address is invalid"

    }


    @get:Rule
    var activityTestRule = ActivityTestRule<ListDogsActivity>(ListDogsActivity::class.java, false, false)

    private var mContext: Context = getInstrumentation().targetContext

    init {
        val volleyResources = VolleyIdlingResource("VolleyCalls", mContext)
        IdlingRegistry.getInstance().register(volleyResources)
    }


    @Before
    fun init() {
        Util.setToken(mContext, null)
        activityTestRule.launchActivity(null)
    }


    @Test
    fun login_failed() {
        onView(withId(R.id.email))
            .check(matches(isDisplayed()))
            .check(matches(not(hasErrorText(ERROR_LOGIN))))
            .perform(typeText(WRONG_EMAIL), closeSoftKeyboard())

        onView(withId(R.id.button_sign_in))
            .perform(click())

        onView(withId(R.id.email))
            .check(matches(isDisplayed()))
            .check(matches(hasErrorText(ERROR_LOGIN)))
    }

    @Test
    fun login_success() {
        onView(withId(R.id.email))
            .check(matches(isDisplayed()))
            .check(matches(not(hasErrorText(ERROR_LOGIN))))
            .perform(typeText(USER_EMAIL), closeSoftKeyboard())

        onView(withId(R.id.button_sign_in))
            .perform(click())

        onView(withId(R.id.activity_list_dogs))
            .check(matches(isDisplayed()))
    }


}
