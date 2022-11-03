package com.bios.findmefood

import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith



@RunWith(AndroidJUnit4::class)
class AuthActivityTest {

    @Test fun testEvent() {
        launchActivity<AuthActivity>().use {
        }
    }
}
