package com.fabricethilaw.gozem.businesscase

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.fabricethilaw.gozem.MainActivity
import com.fabricethilaw.gozem.R
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests for some navigation flows
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class BasicTest {
    /**
     * Basic instrumented test for Sign in  and Signup UI navigation
     */
    @Test
    fun navigationBetweenSignInAndSIgnUpFlow() {
        // Launch the main activity
        launchActivity<MainActivity>()

        // Sign In screen //
        // focus on email field
        onView(withId(R.id.input_email))
        // focus on password field
        onView(withId(R.id.input_password))
        // Submit for login
        onView(withId(R.id.btn_login)).perform(click())

        // Or Move to Sign up screen
        onView(withId(R.id.go_to_signup)).perform(click())
        // focus on full name field
        onView(withId(R.id.input_full_name))
        // focus on email field
        onView(withId(R.id.input_email))
        // focus on password field
        onView(withId(R.id.input_password))
        // focus on confirm password field
        onView(withId(R.id.input_confirm_password))
        // Submit for registration
        onView(withId(R.id.btn_register)).perform(click())

    }
}
