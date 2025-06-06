package vn.edu.stu.tranthanhsang.healthy_app.data.local.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferenceRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
){
    private object PreferencesKeys {
        val USER_ID = stringPreferencesKey("user_id")
        val ROLE = stringPreferencesKey("role")
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val EMAIL = stringPreferencesKey("email")
    }
    suspend fun saveEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.EMAIL] = email
        }
    }

    val email: Flow<String?> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.EMAIL]
    }

    suspend fun saveUserSession(userId: String, role: String, accessToken: String, refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_ID] = userId
            preferences[PreferencesKeys.ROLE] = role
            preferences[PreferencesKeys.ACCESS_TOKEN] = accessToken
            preferences[PreferencesKeys.REFRESH_TOKEN] = refreshToken
        }
    }

    suspend fun clearUserSession() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    val userId: Flow<String?> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_ID]
    }

    val userRole: Flow<String?> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.ROLE]
    }

    val accessToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.ACCESS_TOKEN]
    }

    val refreshToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.REFRESH_TOKEN]
    }

    val userSession: Flow<Map<String, String?>> = dataStore.data.map { preferences ->
        mapOf(
            "user_id" to preferences[PreferencesKeys.USER_ID],
            "role" to preferences[PreferencesKeys.ROLE],
            "access_token" to preferences[PreferencesKeys.ACCESS_TOKEN],
            "refresh_token" to preferences[PreferencesKeys.REFRESH_TOKEN]
        )
    }
}