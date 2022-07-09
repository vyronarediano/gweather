package com.ced.gweather_core.data.memory

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.ced.authentication.domain.repository.SessionRepository
import javax.inject.Inject

/**
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 *
 * Setting of shared preferences in a more secure way
 * using EncryptedSharedPreferences - Part of Android Jetpack
 *
 * The library uses the builder pattern to provide safe default settings for the following security levels:
 * -Strong security that balances great encryption and good performance.
 * -Maximum security.
 */
class SessionMemoryRepository @Inject constructor(context: Context) :
    SessionRepository {

    private val masterKeyAlias = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences =
        EncryptedSharedPreferences.create(
            context,
            "com.ced.gweather",
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    private val KEY_OPEN_WEATHER_MAP = "KEY_OPEN_WEATHER_MAP"
    private val KEY_LOGGED_IN_USER_ID = "GWEATHER_PREFERENCES_USER_ID"
    private val KEY_LOGGED_IN_USER_NAME = "GWEATHER_PREFERENCES_USER_NAME"
    private val KEY_LOGGED_IN_USER_EMAIL = "GWEATHER_PREFERENCES_USER_EMAIL"
    private val KEY_ALLOWED_TO_SAVE = "KEY_ALLOWED_TO_SAVE"

    override var openWeatherMapKey: String?
        get() = sharedPreferences.getString(KEY_OPEN_WEATHER_MAP, "YOUR_API_KEY")
        set(value) {
            sharedPreferences.edit().putString(KEY_OPEN_WEATHER_MAP, value).apply()
        }

    override var loggedInUserId: String?
        get() = sharedPreferences.getString(KEY_LOGGED_IN_USER_ID, null)
        set(value) {
            sharedPreferences.edit().putString(KEY_LOGGED_IN_USER_ID, value).apply()
        }

    override var loggedInUserName: String?
        get() = sharedPreferences.getString(KEY_LOGGED_IN_USER_NAME, null)
        set(value) {
            sharedPreferences.edit().putString(KEY_LOGGED_IN_USER_NAME, value).apply()
        }

    override var loggedInEmail: String?
        get() = sharedPreferences.getString(KEY_LOGGED_IN_USER_EMAIL, null)
        set(value) {
            sharedPreferences.edit().putString(KEY_LOGGED_IN_USER_EMAIL, value).apply()
        }

    override var isAllowedToSave: Boolean?
        get() = sharedPreferences.getBoolean(KEY_ALLOWED_TO_SAVE, false)
        set(value) {
            sharedPreferences.edit().putBoolean(KEY_ALLOWED_TO_SAVE, value ?: false).apply()
        }

    override fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}