package com.smackjeeves.ui.activity

import android.os.Bundle
import android.widget.ImageView
import com.smackjeeves.R
import com.smackjeeves.network.Api
import com.smackjeeves.network.send
import com.smackjeeves.ui.base.BaseActivity
import com.smackjeeves.utils.*
import skizo.library.extensions.delayed
import skizo.library.extensions.toPx


class SplashActivity: BaseActivity() {

    init {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ImageView(applicationContext).let {
            it.setImageResource(R.drawable.smack_jeeves)
            it.setPadding(16.toPx, 0, 16.toPx, 0)
            setContentView(it)
        }

        initialize()
    }


    private fun initialize() {
        if(Config.isFirstRun) {
            Api.service.guestLogin().send(
                {startMainActivity()},
                {guestLoginError()})
        } else {
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        delayed({
            newActivity<MainActivity>()
            finish()
        })
    }


    private fun guestLoginError() {

    }



}