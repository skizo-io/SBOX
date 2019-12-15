package com.smackjeeves.ui.menu.authenticate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smackjeeves.R
import com.smackjeeves.ui.base.BaseFragment
import com.smackjeeves.ui.webview.WebViewFragment
import com.smackjeeves.utils.newFragment
import skizo.library.extensions.replace
import skizo.library.extensions.visible
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.item_appbar.*
import kotlinx.android.synthetic.main.item_menu_text.view.*

class RegisterFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle? = null) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    // putString(ARG_PARAM1, param1)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        appbar.setVisible(close = true)
        appbar_line.visible = false

        input_register_username.caption.hint = getString(R.string.username)
        input_register_email.caption.hint = getString(R.string.email_address)
        input_register_password.caption.hint = getString(R.string.password)
        input_register_confirm_password.caption.hint = getString(R.string.confirm_password)

        btn_register_login.replace(getString(R.string.log_in), color = R.color.colorPrimaryDark, block = {
            newFragment<LoginFragment>()
            activity?.finish()
        })

        btn_register_privacy_policy.replace(getString(R.string.privacy_policy), color = R.color.colorPrimaryDark, block = {
            newFragment<WebViewFragment>(WebViewFragment.getBundle("https://dev-ux.smackjeeves.com/html/legal/privacypolicy.html", getString(R.string.privacy_policy)))
        })
        btn_register_privacy_policy.replace(getString(R.string.terms_of_use), color = R.color.colorPrimaryDark, block = {
            newFragment<WebViewFragment>(WebViewFragment.getBundle("https://dev-ux.smackjeeves.com/html/legal/terms.html", getString(R.string.terms_of_use)))
        })
    }


}