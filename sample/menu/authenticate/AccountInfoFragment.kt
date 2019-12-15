package com.smackjeeves.ui.menu.authenticate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smackjeeves.R
import com.smackjeeves.ui.base.BaseFragment
import com.smackjeeves.utils.newFragment
import skizo.library.extensions.setOnClickListeners
import kotlinx.android.synthetic.main.fragment_account_info.*
import kotlinx.android.synthetic.main.item_appbar.*
import kotlinx.android.synthetic.main.item_menu_button.view.*

class AccountInfoFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle? = null) =
            AccountInfoFragment().apply {
                arguments = Bundle().apply {
                    // putString(ARG_PARAM1, param1)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        appbar_title.setText(R.string.account)
        appbar.setVisible(back = true)


        btn_accountinfo_nickname.caption.text = "Member# 2342"
        btn_accountinfo_email.caption.text = "sma*******@gmail.com"
        btn_accountinfo_password.caption.text = "*********"


        setOnClickListeners(this::onClick,
            btn_accountinfo_nickname, btn_accountinfo_email, btn_accountinfo_password)

    }

    fun onClick(view: View) {
        when(view) {
            btn_accountinfo_nickname -> {}
            btn_accountinfo_email -> newFragment<ChangeEmailFragment>()
            btn_accountinfo_password -> newFragment<ChangePasswordFragment>()
        }

    }








}