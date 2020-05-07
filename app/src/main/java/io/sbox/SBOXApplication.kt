package io.sbox

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.multidex.MultiDexApplication
import com.bumptech.glide.Glide

class SBOXApplication: MultiDexApplication() {


    companion object {
        var instance: SBOXApplication? = null
            private set
    }





    override fun onCreate() {
        super.onCreate()

/*

        instance = this

        Config.isDebugMode = BuildConfig.DEBUG_MODE

        Comico.startInit(this)
            .isDebugMode(Config.isDebugMode, listOf("skizo80@gmail.com"))


        Stetho.initializeWithDefaults(this)

        Glide.get(this).setMemoryCategory(MemoryCategory.HIGH)
*/


//        OneSignal.startInit(this)
//            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//            .unsubscribeWhenNotificationsAreDisabled(true)
//            .init()

//        if(Config.isDebugMode) {
//            OneSignal.getTags { tags -> OneSignal.sendTags(tags) }
//            OneSignal.sendTag("PUSH_TEST", "OK")
//            OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
//        }

    }


    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)

        Glide.get(this).trimMemory(level)
    }





}