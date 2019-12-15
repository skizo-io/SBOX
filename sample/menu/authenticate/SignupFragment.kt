package com.smackjeeves.ui.menu.authenticate

import android.os.Bundle
import com.smackjeeves.ui.base.BaseFragment

class SignupFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle? = null) =
            SignupFragment().apply {
                arguments = Bundle().apply {
                    // putString(ARG_PARAM1, param1)
                }
            }
    }




}