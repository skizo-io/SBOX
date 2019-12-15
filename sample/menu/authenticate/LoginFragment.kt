package com.smackjeeves.ui.menu.authenticate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smackjeeves.R
import com.smackjeeves.network.Api
import com.smackjeeves.network.send
import com.smackjeeves.ui.base.BaseFragment
import com.smackjeeves.ui.webview.WebViewFragment
import com.smackjeeves.utils.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.item_appbar.*
import kotlinx.android.synthetic.main.item_menu_text.view.*
import skizo.library.extensions.TextType
import skizo.library.extensions.replace
import skizo.library.extensions.visible

class LoginFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle? = null) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    // putString(ARG_PARAM1, param1)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        appbar.setVisible(close = true)
        appbar_line.visible = false

        input_login_username.caption.hint = getString(R.string.username)
        input_login_password.caption.hint = getString(R.string.password)

        btn_login_create_account.replace(getString(R.string.create_account), TextType.UNDERLINE, block = {
            newFragment<RegisterFragment>()
            activity?.finish()
        })
        btn_login_forgot_password.replace(getString(R.string.forgot_password), TextType.UNDERLINE, block = {
            newFragment<ResetPasswordFragment>()
            activity?.finish()
        })
        btn_login_privacy_policy.replace(getString(R.string.privacy_policy), block = {
            newFragment<WebViewFragment>(WebViewFragment.getBundle("https://dev-ux.smackjeeves.com/html/legal/privacypolicy.html", getString(R.string.privacy_policy)))
        })
        btn_login_privacy_policy.replace(getString(R.string.terms_of_use), block = {
            newFragment<WebViewFragment>(WebViewFragment.getBundle("https://dev-ux.smackjeeves.com/html/legal/terms.html", getString(R.string.terms_of_use)))
        })



        Api.service.getSessionKey(Api.ApiService.SessionType.LOGIN).send()

    }




}