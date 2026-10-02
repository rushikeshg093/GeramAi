package com.example

import android.app.Application
import android.util.Log
import com.example.data.notification.NotificationHelper
import com.example.data.remote.GrampanchayatMessagingService
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory

class GrampanchayatApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeFirebase()
        initializeAppCheck()
        try {
            NotificationHelper.createNotificationChannel(this)
            GrampanchayatMessagingService.subscribeToPublicTopics()
        } catch (e: Exception) {
            Log.e(TAG, "Error configuring notifications: ${e.message}")
        }
    }

    private fun initializeFirebase() {
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                val app = FirebaseApp.initializeApp(this)
                if (app != null) {
                    Log.i(TAG, "Firebase initialized successfully with app name: ${app.name}")
                } else {
                    Log.w(TAG, "FirebaseApp.initializeApp returned null. Check google-services.json.")
                }
            } else {
                Log.i(TAG, "Firebase already initialized by FirebaseInitProvider.")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize FirebaseApp: ${e.message}", e)
        }
    }

    private fun initializeAppCheck() {
        try {
            val firebaseAppCheck = FirebaseAppCheck.getInstance()
            if (BuildConfig.DEBUG) {
                firebaseAppCheck.installAppCheckProviderFactory(
                    DebugAppCheckProviderFactory.getInstance()
                )
                Log.i(TAG, "Firebase App Check initialized with Debug provider.")
            } else {
                firebaseAppCheck.installAppCheckProviderFactory(
                    PlayIntegrityAppCheckProviderFactory.getInstance()
                )
                Log.i(TAG, "Firebase App Check initialized with Play Integrity provider for production.")
            }
        } catch (e: Exception) {
            Log.w(TAG, "App Check initialization note: ${e.message}")
        }
    }

    companion object {
        private const val TAG = "GrampanchayatApp"
    }
}
