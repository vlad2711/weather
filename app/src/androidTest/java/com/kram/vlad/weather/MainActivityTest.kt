package com.kram.vlad.weather

import android.support.design.widget.TabLayout
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.PerformException
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.runner.AndroidJUnit4
import com.kram.vlad.weather.settings.Preferences
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import android.support.test.rule.ActivityTestRule;
import com.kram.vlad.weather.activitys.MainActivity
import android.support.test.rule.GrantPermissionRule
import android.view.View
import org.hamcrest.Matchers.allOf
import org.junit.Rule


@RunWith(AndroidJUnit4::class)
class MainActivityTest {


    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Rule
    @JvmField
    var grantPermissionRule: GrantPermissionRule? = null
    @Test
    @Throws(Exception::class)
    fun testAddCity() {
        grantPermissionRule = GrantPermissionRule.grant("android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION")

        Thread.sleep(5000)
        testAddCity("Киев", "50.3430813", "31.0031754")
        testAddCity("Лондон", "51.5287718", "-0.2416822")

        onView(withId(R.id.pager)).perform(swipeRight())
        onView(withId(R.id.cities)).perform(selectTabAtPosition(2))
        onView(withId(R.id.pager)).perform(swipeLeft())
        onView(withId(R.id.cities)).perform(selectTabAtPosition(0))

    }

    private fun testAddCity(cityName: String, latitude: String, longitude: String) {
        val oldCitys = Preferences.CITYS.size
        activityTestRule.runOnUiThread{activityTestRule.activity.onPlaceSelected(cityName, latitude, longitude)}

        Thread.sleep(2500)
        assertNotEquals(oldCitys, Preferences.CITYS.size)
    }

    fun selectTabAtPosition(tabIndex: Int): ViewAction {
        return object : ViewAction {
            override fun getDescription() = "with tab at index $tabIndex"

            override fun getConstraints() = allOf(isDisplayed(), isAssignableFrom(TabLayout::class.java))

            override fun perform(uiController: UiController, view: View) {
                val tabLayout = view as TabLayout
                val tabAtIndex: TabLayout.Tab = tabLayout.getTabAt(tabIndex)
                        ?: throw PerformException.Builder()
                                .withCause(Throwable("No tab at index $tabIndex"))
                                .build()

                tabAtIndex.select()
            }
        }
    }
}

