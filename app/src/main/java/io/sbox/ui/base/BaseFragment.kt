package io.sbox.ui.base

import android.content.Intent
import androidx.fragment.app.Fragment

abstract class BaseFragment: Fragment() {
    /*
    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle?) =
            BaseFragment().apply {
                arguments = bundle
            }

        @JvmStatic
        fun getBundle() = bundleOf()
    }
    */



    fun resultActivity(requestCode: Int, resultCode: Int, data: Intent?) {

    }



}