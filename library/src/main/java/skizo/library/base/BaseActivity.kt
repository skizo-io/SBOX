package com.smackjeeves.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.smackjeeves.utils.trace
import skizo.library.base.BaseApplication

abstract class BaseActivity : AppCompatActivity() {


    companion object {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        if (BaseApplication.instance.isReturnedToForeground) {
            /*
            if(this is SplashActivity)
                trace("App Status firstStart")
            else
                trace("App Status returnToForeground")
            Api.service.getAppInfo().send()
            Api.service.getUserInfo().send()
            */

        }


    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        if (!BaseApplication.instance.isForeground) {
            trace("App Status isBackground")

        }


    }

    override fun onDestroy() {
        super.onDestroy()
    }


}



