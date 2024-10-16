// DataStore.kt
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.buffetec.data.PreferencesKey.ROL
import kotlinx.coroutines.flow.first

private const val DATASTORE = "my_datastore"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE)

private val TOKEN = stringPreferencesKey("token")

suspend fun Context.saveToken(token: String) {

    dataStore.edit { preferences ->
        preferences[TOKEN] = token
    }
}


suspend fun Context.saveRol(rol: String) {

    dataStore.edit { preferences ->
        preferences[ROL] = rol
    }
}

suspend fun Context.getToken(): String {
    val preferences = dataStore.data.first() // Ensure you have imported kotlinx.coroutines.flow.first
    return preferences[TOKEN] ?: "" // Default value
}

suspend fun Context.getRol(): String {
    val preferences = dataStore.data.first() // Ensure you have imported kotlinx.coroutines.flow.first
    return preferences[ROL] ?: "" // Default value
}

