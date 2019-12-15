package com.smackjeeves.ui.menu.authenticate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smackjeeves.R
import com.smackjeeves.ui.base.BaseFragment
import com.smackjeeves.utils.newFragment
import skizo.library.extensions.replace
import skizo.library.extensions.visible
import kotlinx.android.synthetic.main.fragment_reset_password.*
import kotlinx.android.synthetic.main.item_appbar.*
import kotlinx.android.synthetic.main.item_menu_text.view.*

class ResetPasswordFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle? = null) =
            ResetPasswordFragment().apply {
                arguments = Bundle().apply {
                    // putString(ARG_PARAM1, param1)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_reset_password, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        appbar.setVisible(close = true)
        appbar_line.visible = false

        input_reset_password_email.caption.hint = getString(R.string.email_address)

        btn_reset_password_login.replace(getString(R.string.log_in), color = R.color.colorPrimaryDark, block = {
            newFragment<LoginFragment>()
            activity?.finish()
        })
    }


}