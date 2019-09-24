package com.smackjeeves.preferences.base

import android.content.Context
import android.content.SharedPreferences
import java.util.*

open class BasePreferences {

    companion object {
        private var pref: SharedPreferences? = null

        val base: BasePreferences = BasePreferences()

        fun init(context: Context, preferences_name: String = "skizo") {
            pref = context.getSharedPreferences(preferences_name, Context.MODE_PRIVATE)
            pref?.registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
                //                    dispatcher(PREFERENCES_NAME, key)
            }
        }

        /**
         * usage
         * BasePreferences.init(applicationContext)
         */
        /*
        var advertiginId: String = ""
            get() {
                field = base.get("advertiginId", "")
                if (field.isEmpty()) {
                    Thread(Runnable {
                        try {
                            field =
                                AdvertisingIdClient.getAdvertisingIdInfo(SmackApplication.instance)
                                    .id
                            base.set("advertiginId", field)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }).start()
                }
                return field
            }
        */
        var uuid: String = ""
            get() {
                field = base.get("uuid", "")
                if (field.isEmpty()) {
//                    field = android.provider.Settings.Secure.getString(BaseApplication.instance.contentResolver, Settings.Secure.ANDROID_ID)
                    field = UUID.randomUUID().toString()
                    base.set("uuid", field)
                }
                return field
            }
    }


    fun clear() {
        pref?.edit()?.clear()?.apply()
    }

    fun set(key: String, value: Any) {
        pref?.edit()?.apply {
            when(value) {
                is Int -> putInt(key, value)
                is String -> putString(key, value)
                is Float -> putFloat(key, value)
                is Long -> putLong(key, value)
                is Boolean -> putBoolean(key, value)
            }
        }?.apply()
    }

    fun <T> get(key: String, defalut: Any): T {
        pref?.apply {
            return when(defalut) {
                is Int -> getInt(key, defalut)
                is String -> getString(key, defalut)
                is Float -> getFloat(key, defalut)
                is Long -> getLong(key, defalut)
                is Boolean -> getBoolean(key, defalut)
                else -> defalut
            } as T
        }
        return defalut as T
    }


}