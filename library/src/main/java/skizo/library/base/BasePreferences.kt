package com.smackjeeves.preferences.base

import android.content.Context
import android.content.SharedPreferences
import java.util.*

open class BasePreferences {

    companion object {
        private val PREFERENCES_NAME = "pref_smackjeeves"

        private var pref: SharedPreferences? = null

        val base: BasePreferences = BasePreferences()

        fun init(context: Context) {
            pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
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

    fun set(key: String, value: Int): Int {
        pref?.edit()?.putInt(key, value)?.apply()
        return value
    }

    fun get(key: String, defValue: Int): Int {
        return pref?.getInt(key, defValue) ?: defValue
    }

    fun set(key: String, value: String): String {
        pref?.edit()?.putString(key, value)?.apply()
        return value
    }

    fun get(key: String, defValue: String): String {
        return pref?.getString(key, defValue) ?: defValue
    }

    fun set(key: String, value: Float): Float {
        pref?.edit()?.putFloat(key, value)?.apply()
        return value
    }

    fun get(key: String, defValue: Float): Float {
        return pref?.getFloat(key, defValue) ?: defValue
    }

    fun set(key: String, value: Long): Long {
        pref?.edit()?.putLong(key, value)?.apply()
        return value
    }

    fun get(key: String, defValue: Long): Long {
        return pref?.getLong(key, defValue) ?: defValue
    }

    fun set(key: String, value: Boolean): Boolean {
        pref?.edit()?.putBoolean(key, value)?.apply()
        return value
    }

    fun get(key: String, defValue: Boolean): Boolean {
        return pref?.getBoolean(key, defValue) ?: defValue
    }


}