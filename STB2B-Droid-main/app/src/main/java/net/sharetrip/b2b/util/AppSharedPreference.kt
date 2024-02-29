package net.sharetrip.b2b.util

import android.content.Context
import android.content.SharedPreferences
import net.sharetrip.b2b.BuildConfig

object AppSharedPreference {
    private const val NAME = "ShareTripB2B"
    private const val MODE = Context.MODE_PRIVATE
    private const val ACCESS_TOKEN = "ACCESS_TOKEN"
    private const val USER_NAME = "USER_NAME"
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var accessToken: String
        get() = preferences.getString(ACCESS_TOKEN, "")!!
        set(value) = preferences.edit {
            it.putString(ACCESS_TOKEN, value)
        }

    var userName: String
        get() = preferences.getString(USER_NAME, "")!!
        set(value) = preferences.edit {
            it.putString(USER_NAME, value)
        }

    var b2bFlightPromotionImageUrl: String
        get() = preferences.getString(FLIGHT_BANNER_IMAGE_URL, "")!!
        set(imageUrl) = preferences.edit {
            it.putString(FLIGHT_BANNER_IMAGE_URL, imageUrl)
        }

    val versionCode: Int
        get() = BuildConfig.VERSION_CODE

    var appVersionName: String
        get() = preferences.getString(REMOTE_CONFIG_APP_VERSION_NAME, "")!!
        set(appVersion) = preferences.edit {
            it.putString(REMOTE_CONFIG_APP_VERSION_NAME, appVersion)
        }

    var latestCodeVersion: Int
        get() = preferences.getInt(REMOTE_CONFIG_APP_VERSION_CODE, 0)!!
        set(latestCodeVersion) = preferences.edit {
            it.putInt(REMOTE_CONFIG_APP_VERSION_CODE, latestCodeVersion)
        }


    fun put(key: String, value: String?) {
        preferences.edit().putString(key, value).apply()
    }

    fun put(key: String, value: Int) {
        preferences.edit().putInt(key, value).apply()
    }
    fun put(key: String, value: Long) {
        preferences.edit().putLong(key, value).apply()
    }

    fun put(key: String, value: Float) {
        preferences.edit().putFloat(key, value).apply()
    }

    fun put(key: String, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }

    operator fun get(key: String, defaultValue: String): String {
        return preferences.getString(key, defaultValue) ?: defaultValue
    }

    operator fun get(key: String, defaultValue: Int): Int {
        return preferences.getInt(key, defaultValue)
    }

    operator fun get(key: String, defaultValue: Float): Float {
        return preferences.getFloat(key, defaultValue)
    }

    operator fun get(key: String, defaultValue: Boolean): Boolean {
        return preferences.getBoolean(key, defaultValue)
    }

    operator fun get(key: String, defaultValue: Long): Long {
        return preferences.getLong(key, defaultValue)
    }

    fun deleteSavedData(key: String) {
        preferences.edit().remove(key).apply()
    }


}
