package skizo.library.base

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import com.smackjeeves.ui.base.BaseActivity
import com.smackjeeves.ui.base.BaseFragment


class EmptyActivity : BaseActivity() {

    companion object {
        val FRAGMENT = "FRAGMENT"
    }

    var fragment: BaseFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        var layout = FrameLayout(this)
        setContentView(layout)


        try {
            val bundle = intent.extras
            val frag = Class.forName(bundle.getString(FRAGMENT))
            fragment = frag.getMethod("newInstance", Bundle::class.java)?.let {
                it.invoke(null, bundle) as BaseFragment
            }

            fragment?.let {
                val transaction = supportFragmentManager.beginTransaction()
                if (it.isAdded) transaction.show(it).commit()
                else transaction.replace(layout.id, it).commit()
            }
        } catch (e: Exception) {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fragment?.resultActivity(requestCode, resultCode, data)

    }
}
