package skizo.sbox.ui.activity

import android.os.Bundle
import android.widget.ImageView
import skizo.library.extensions.delayed
import skizo.library.extensions.newActivity
import skizo.library.extensions.toPx
import skizo.sbox.R
import skizo.sbox.ui.base.BaseActivity

class SplashActivity: BaseActivity() {

    init {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ImageView(applicationContext).let {
            it.setImageResource(R.drawable.ic_notifications_black_24dp)
            it.setPadding(16.toPx, 0, 16.toPx, 0)
            setContentView(it)
        }

        initialize()
    }


    private fun initialize() {

        startMainActivity()

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