package com.kram.vlad.weather

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.runner.AndroidJUnit4

import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.gson.Gson
import com.kram.vlad.weather.settings.Preferences
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import android.support.test.rule.ActivityTestRule;
import android.support.test.InstrumentationRegistry.getTargetContext
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import com.kram.vlad.weather.activitys.MainActivity
import android.app.Activity
import android.support.test.espresso.core.internal.deps.guava.collect.Iterables
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.lifecycle.Stage
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.kram.vlad.weather.R.string.a
import org.junit.Rule
import android.support.test.InstrumentationRegistry.getTargetContext
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.matcher.ViewMatchers.withText
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector


@RunWith(AndroidJUnit4::class)
class MainActivityTest {


    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Rule
    @JvmField
    var grantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION",
                    "android.permission.ACCESS_COARSE_LOCATION")

    @Test
    @Throws(Exception::class)
    fun testAddCity() {
        Thread.sleep(5000)
        testAddCity("Киев", "50.3430813", "31.0031754")
        testAddCity("Лондон", "51.5287718", "-0.2416822")
        val device = UiDevice.getInstance(getInstrumentation())
        device.pressHome()

        Thread.sleep(10000)
        device.pressRecentApps()
        device.findObject(UiSelector().text("weather")).click()

        onView(withId(R.id.pager)).perform(swipeRight())
        onView(withText("Лондон")).perform(click())
        onView(withId(R.id.pager)).perform(swipeLeft())
        onView(withText("Киев")).perform(click())
    }

    private fun testAddCity(cityName: String, latitude: String, longitude: String) {
        val oldCitys = Preferences.CITYS.size
        activityTestRule.runOnUiThread{activityTestRule.activity.onPlaceSelected(cityName, latitude, longitude)}

        Thread.sleep(2500)
        assertNotEquals(oldCitys, Preferences.CITYS.size)
    }
}
