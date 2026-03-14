package bd.com.infobox.browser.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

fun getDataStore(producePath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath() }
    )

expect fun createDataStore(context: Any? = null): DataStore<Preferences>

const val DATASTORE_FILE_NAME = "settings.preferences_pb"
