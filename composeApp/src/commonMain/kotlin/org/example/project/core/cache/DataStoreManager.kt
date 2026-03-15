package org.example.project.core.cache

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

interface DataStoreManager {

    interface SaveToken {
        suspend fun saveUserToken(token: String)
    }

    interface ReadToken {
        suspend fun userToken(): String?
    }

    interface ReadOnboarding {
        suspend fun isOnboarded(): Boolean
    }

    interface FinishOnboarding {
        suspend fun finishOnboarding()
    }

    interface TokenOperations : ReadToken, SaveToken
    interface Read : ReadToken, ReadOnboarding
    interface All : Read, SaveToken, FinishOnboarding, TokenOperations

    class Base(private val dataStore: DataStore<Preferences>) : All {

        override suspend fun saveUserToken(token: String) {
            dataStore.edit {
                it[USER_TOKEN_KEY] = token
            }
        }

        override suspend fun userToken(): String? {
            val token = dataStore.data.map {
                it[USER_TOKEN_KEY]
            }.first()
            return token
        }

        override suspend fun isOnboarded(): Boolean {
            val flag = dataStore.data.map {
                it[ONBOARDING_KEY]
            }.first()
            return flag ?: false
        }

        override suspend fun finishOnboarding() {
            dataStore.edit {
                it[ONBOARDING_KEY] = true
            }
        }

        companion object {
            private val ONBOARDING_KEY = booleanPreferencesKey("ONBOARDING_KEY")
            private val USER_TOKEN_KEY = stringPreferencesKey("USER_TOKEN_KEY")

        }
    }

}
