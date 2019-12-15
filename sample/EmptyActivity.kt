package com.smackjeeves.ui.activity

import android.app.AlertDialog
import android.os.Bundle
import com.smackjeeves.R
import com.smackjeeves.ui.base.BaseActivity
import com.smackjeeves.ui.base.BaseFragment
import androidx.core.app.NotificationCompat.getExtras
import android.content.Intent
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import android.widget.Toast
import com.smackjeeves.utils.*
import java.lang.Exception





class EmptyActivity : BaseActivity() {

    companion object {
        val FRAGMENT = "FRAGMENT"
    }

    var fragment: BaseFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty)


        try {
            val bundle = intent.extras
            val frag = Class.forName(bundle.getString(FRAGMENT))
            fragment = frag.getMethod("newInstance", Bundle::class.java)?.let {
                it.invoke(null, bundle) as BaseFragment
            }

            fragment?.let {
                val transaction = supportFragmentManager.beginTransaction()
                if (it.isAdded) transaction.show(it).commit()
                else transaction.replace(R.id.empty_container, it).commit()
            }
        } catch (e: Exception) {

        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fragment?.resultActivity(requestCode, resultCode, data)

    }


}