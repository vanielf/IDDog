package co.idwall.iddog

import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
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
class ListDogsTest {

    companion object {

        private const val TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJpZGRvZy1zZXJ2aWNlIiwic3ViIjoiNWNiNTZiM2M1YTk0MDE3YzAxYTVkMjc1IiwiaWF0IjoxNTU1MzkzMzQwLCJleHAiOjE1NTY2ODkzNDB9.dS9zSS9LS6DbnKl2W2RHHgH9QwnvBGqjspsNox1M5DY"

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
        Util.setToken(mContext, TOKEN)
        activityTestRule.launchActivity(null)
    }


    @Test
    fun listing_home() {
        onView(withId(R.id.navigation_husky))
            .check(matches(isSelected()))

        onView(withId(R.id.view_dogs))
            .check(matches(isDisplayed()))
            .check(matches(hasMinimumChildCount(12)))
    }

    @Test
    fun listing_expand() {
        onView(withId(R.id.expanded_container))
            .check(matches(not(isDisplayed())))

        onView(withId(R.id.view_dogs))
            .check(matches(isDisplayed()))
            .check(matches(hasMinimumChildCount(12)))
            .perform(RecyclerViewActions.actionOnItemAtPosition<ListDogsAdapter.ViewHolder>((0..12).random(), click()))

        onView(withId(R.id.expanded_container))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.expanded_container))
            .check(matches(not(isDisplayed())))
    }

    @Test
    fun listing_husky() {
        onView(withId(R.id.navigation_husky))
            .perform(click())

        onView(withId(R.id.navigation_husky))
            .check(matches(isSelected()))

        onView(withId(R.id.view_dogs))
            .check(matches(isDisplayed()))
            .check(matches(hasMinimumChildCount(12)))
    }

    @Test
    fun listing_hound() {
        onView(withId(R.id.navigation_hound))
            .perform(click())

        onView(withId(R.id.navigation_hound))
            .check(matches(isSelected()))

        onView(withId(R.id.view_dogs))
            .check(matches(isDisplayed()))
            .check(matches(hasMinimumChildCount(12)))
    }

    @Test
    fun listing_pug() {
        onView(withId(R.id.navigation_pug))
            .perform(click())

        onView(withId(R.id.navigation_pug))
            .check(matches(isSelected()))

        onView(withId(R.id.view_dogs))
            .check(matches(isDisplayed()))
            .check(matches(hasMinimumChildCount(12)))
    }

    @Test
    fun listing_labrador() {
        onView(withId(R.id.navigation_labrador))
            .perform(click())

        onView(withId(R.id.navigation_labrador))
            .check(matches(isSelected()))

        onView(withId(R.id.view_dogs))
            .check(matches(isDisplayed()))
            .check(matches(hasMinimumChildCount(12)))
    }

    @Test
    fun logout(){
        openActionBarOverflowOrOptionsMenu(mContext)

        onView(withText(R.string.logout))
            .perform(click())

        onView(withId(R.id.activity_login))
            .check(matches(isDisplayed()))
    }

}
