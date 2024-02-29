package net.sharetrip.b2b.util

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.analytics.FirebaseAnalytics
import com.jakewharton.threetenabp.AndroidThreeTen
import net.sharetrip.b2b.util.analytics.B2BAnalyticsManager

class ShareTripB2B : Application() {

    override fun onCreate() {
        super.onCreate()
        disableNightMode()
        AppSharedPreference.init(this)
        AndroidThreeTen.init(this)
    }

    private fun disableNightMode() {
        try {
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO
            )
        } catch (e: Exception) {
        }
    }

    companion object {
        private var analyticsManager: B2BAnalyticsManager? = null
        private var firebaseAnalytics: FirebaseAnalytics? = null

        private fun getFirebaseAnalytics(context: Context): FirebaseAnalytics {
            if (firebaseAnalytics != null)
                return firebaseAnalytics!!
            firebaseAnalytics = FirebaseAnalytics.getInstance(context)
            return firebaseAnalytics!!
        }

        fun getB2BAnalyticsManager(context: Context): B2BAnalyticsManager {
            if (analyticsManager != null)
                return analyticsManager!!
            analyticsManager = B2BAnalyticsManager.Builder()
                .setFirebaseAnalytics(getFirebaseAnalytics(context)).build()
            return analyticsManager!!
        }
    }
}