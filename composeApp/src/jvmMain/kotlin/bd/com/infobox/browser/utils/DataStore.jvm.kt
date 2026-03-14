package bd.com.infobox.browser.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

actual fun createDataStore(context: Any?): DataStore<Preferences> {
    return getDataStore {
        DATASTORE_FILE_NAME
    }
}
