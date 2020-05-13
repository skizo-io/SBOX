package io.sbox.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.comico.library.extensions.trace
import io.comico.library.Builder
import io.sbox.ui.activity.OverlayActivity

abstract class BaseActivity: AppCompatActivity() {


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

        if(Builder.isReturnedToForeground) {
            if(this is OverlayActivity) {
                trace("##BaseActivity## App Status firstStart")
            } else {
                trace("##BaseActivity## App Status returnToForeground")
            }

        }

    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()

        if(Builder.isBackground) {
            trace("##BaseActivity## App Status isBackground")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }



}



