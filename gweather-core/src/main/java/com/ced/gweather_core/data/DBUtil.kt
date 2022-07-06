package com.ced.gweather_core.data

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

/**
 *
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */
object DBUtil {

    private val PREFERENCES_APP_ID = "com.ced.gweather"

    var context: Context? = null

    fun init(context: Context) {
        this.context = context

        FirebaseFirestore.getInstance().apply {
            this.firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .setSslEnabled(true)
                .build()
        }
    }
}