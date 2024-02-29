package net.sharetrip.b2b

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import net.sharetrip.b2b.base.FeatureEnum
import net.sharetrip.b2b.util.*
import net.sharetrip.b2b.util.analytics.B2BEvent
import net.sharetrip.b2b.util.analytics.B2BEvent.FlightEvent.CLICK_HOME

class MainActivity : AppCompatActivity(), GlobalNavigationHandler {
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var navController: NavController
    private lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        bottomNav = findViewById(R.id.bottom_navigation)
        bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.flight_search_dest -> showBottomNav(FeatureEnum.HOME)
                R.id.booking_history -> showBottomNav(FeatureEnum.BOOKING)
                R.id.transaction_list_dest -> showBottomNav(FeatureEnum.TRANSACTION)
                R.id.payment_dest -> showBottomNav(FeatureEnum.PAYMENT)
                R.id.more_dest -> showBottomNav(FeatureEnum.MORE)
                else -> hideBottomNav()
            }
        }

        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        fetchRemoteConfigData()
    }

    private fun showBottomNav(featureEnum: FeatureEnum) {
        when (featureEnum) {
            FeatureEnum.HOME -> {
                ShareTripB2B.getB2BAnalyticsManager(this)
                    .trackEvent(CLICK_HOME)
            }
            FeatureEnum.BOOKING -> {
                ShareTripB2B.getB2BAnalyticsManager(this)
                    .trackEvent(B2BEvent.FlightEvent.CLICK_BOOKING)
            }
            FeatureEnum.TRANSACTION -> {
                ShareTripB2B.getB2BAnalyticsManager(this)
                    .trackEvent(B2BEvent.FlightEvent.CLICK_TRANSACTION)
            }
            FeatureEnum.PAYMENT -> {
                ShareTripB2B.getB2BAnalyticsManager(this)
                    .trackEvent(B2BEvent.FlightEvent.CLICK_PAYMENT)
            }
            FeatureEnum.MORE -> {
                ShareTripB2B.getB2BAnalyticsManager(this)
                    .trackEvent(B2BEvent.FlightEvent.CLICK_MORE)
            }
        }

        bottomNav.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        bottomNav.visibility = View.GONE
    }

    override fun onBackPressed() {
        when (navController.currentDestination?.id) {
            R.id.booking_history, R.id.transaction_list_dest, R.id.payment_dest, R.id.more_dest -> {
                finish()
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        GlobalNavigator.registerHandler(this)
    }

    override fun onStop() {
        super.onStop()
        GlobalNavigator.unregisterHandler()
    }

    override fun logout() {
        runOnUiThread {
            AppSharedPreference.accessToken = ""
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController()
                ?.navigate(R.id.action_to_login)
        }
    }

    private fun fetchRemoteConfigData() {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    AppSharedPreference.b2bFlightPromotionImageUrl = remoteConfig.getString(B2B_Flight_Banner_Image_Url)
                    AppSharedPreference.appVersionName = remoteConfig.getString(REMOTE_CONFIG_APP_VERSION_NAME)
                    AppSharedPreference.latestCodeVersion = remoteConfig.getLong(REMOTE_CONFIG_APP_VERSION_CODE).toInt()
                }
            }
    }
}
