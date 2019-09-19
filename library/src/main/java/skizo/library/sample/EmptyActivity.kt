package skizo.library.sample

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


class EmptyActivity : AppCompatActivity() {

    companion object {
        val FRAGMENT = "FRAGMENT"
    }

    var fragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        var layout = FrameLayout(this)
        setContentView(layout)


        try {
            val bundle = intent.extras
            val frag = Class.forName(bundle.getString(FRAGMENT))
            fragment = frag.getMethod("newInstance", Bundle::class.java)?.let {
                it.invoke(null, bundle) as Fragment
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

    }
}
