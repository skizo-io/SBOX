package skizo.library.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.multidex.MultiDexApplication
import com.bumptech.glide.Glide
import com.bumptech.glide.MemoryCategory
import com.facebook.stetho.Stetho
import com.smackjeeves.preferences.base.BasePreferences

class BaseApplication: MultiDexApplication(), Application.ActivityLifecycleCallbacks {


    companion object {
        lateinit var instance: BaseApplication
            private set
    }





    override fun onCreate() {
        super.onCreate()

        instance = this


        registerActivityLifecycleCallbacks(SJActivityLifecycleCallbacks())

        Stetho.initializeWithDefaults(this)

        Glide.get(this).setMemoryCategory(MemoryCategory.HIGH)

        BasePreferences.init(applicationContext)

    }


    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)

        Glide.get(this).trimMemory(level)
    }



    override fun onActivityPaused(p0: Activity?) {
    }

    override fun onActivityResumed(p0: Activity?) {
    }

    override fun onActivityStarted(p0: Activity?) {
    }

    override fun onActivityDestroyed(p0: Activity?) {
    }

    override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {
    }

    override fun onActivityStopped(p0: Activity?) {
    }

    override fun onActivityCreated(p0: Activity?, p1: Bundle?) {
    }


    enum class AppStatus {
        BACKGROUND,                // app is background
        RETURNED_TO_FOREGROUND,    // app returned to foreground(or first launch)
        FOREGROUND;                // app is foreground
    }

    var mAppStatus: AppStatus =
        AppStatus.FOREGROUND
    var isReturnedToForeground: Boolean = false
        get() = mAppStatus == AppStatus.RETURNED_TO_FOREGROUND
    var isForeground: Boolean = false
        get() = mAppStatus.ordinal > AppStatus.BACKGROUND.ordinal

    inner class SJActivityLifecycleCallbacks: ActivityLifecycleCallbacks {
        var running: Int = 0

        override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        }

        override fun onActivityStarted(activity: Activity?) {
            if (++running == 1) {
                mAppStatus = AppStatus.RETURNED_TO_FOREGROUND;
            } else if (running > 1) {
                mAppStatus = AppStatus.FOREGROUND;
            }
        }

        override fun onActivityResumed(activity: Activity?) {
        }

        override fun onActivityPaused(activity: Activity?) {
        }

        override fun onActivityStopped(activity: Activity?) {
            if (--running == 0) {
                mAppStatus = AppStatus.BACKGROUND;
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        }

        override fun onActivityDestroyed(activity: Activity?) {
        }
    }

}
