package io.sbox

import android.app.Application
import com.bumptech.glide.Glide
import com.bumptech.glide.MemoryCategory
import com.facebook.stetho.Stetho
import com.onesignal.OneSignal
import io.comico.library.Comico
import io.sbox.data.Config

class SBOXApplication : Application() {


    companion object {
        var instance: SBOXApplication? = null
            private set
    }


    override fun onCreate() {
        super.onCreate()

        instance = this

        Config.isDebugMode = BuildConfig.DEBUG_MODE

        Glide.get(this).setMemoryCategory(MemoryCategory.HIGH)

        OneSignal.startInit(this)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init()

        if(Config.isDebugMode) {
            OneSignal.getTags { tags -> OneSignal.sendTags(tags) }
            OneSignal.sendTag("PUSH_TEST", "OK")
            OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

            Comico.startInit(this)
                .isDebugMode(Config.isDebugMode, arrayListOf("skizo80@gmail.com"))

            Stetho.initializeWithDefaults(this)
        }

    }


    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)

        Glide.get(this).trimMemory(level)
    }


}