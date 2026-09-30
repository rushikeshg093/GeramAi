package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppLanguage
import com.example.data.local.InitialData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun testAppStringResources() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Grampanchayat", appName)
    }

    @Test
    fun testInitialDataIntegrity() {
        assertNotNull(InitialData.initialProfile)
        assertEquals(3, InitialData.initialProfile.wardNumber)
        assertTrue(InitialData.officials.isNotEmpty())
        assertTrue(InitialData.initialComplaints.isNotEmpty())
        assertTrue(InitialData.initialWaterSchedules.size >= 6)
        assertTrue(InitialData.initialNotices.isNotEmpty())
    }

    @Test
    fun testLanguageSupport() {
        assertEquals("मराठी", AppLanguage.MARATHI.nativeName)
        assertEquals("English", AppLanguage.ENGLISH.displayName)
    }
}
