package bd.com.infobox.browser.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile

actual fun createDataStore(context: Any?): DataStore<Preferences> {
    require(context is Context) { "Android DataStore requires a Context" }
    return getDataStore {
        context.filesDir.resolve(DATASTORE_FILE_NAME).absolutePath
    }
}
