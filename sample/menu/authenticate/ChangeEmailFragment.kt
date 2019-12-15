package com.smackjeeves.ui.menu.authenticate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smackjeeves.R
import com.smackjeeves.ui.base.BaseFragment
import skizo.library.extensions.setOnClickListeners
import kotlinx.android.synthetic.main.fragment_change_email.*
import kotlinx.android.synthetic.main.item_appbar.*
import kotlinx.android.synthetic.main.item_menu_button.view.*

class ChangeEmailFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle? = null) =
            ChangeEmailFragment().apply {
                arguments = Bundle().apply {
                    // putString(ARG_PARAM1, param1)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_change_email, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        appbar_title.setText(R.string.change_email)
        appbar.setVisible(back = true)


        btn_change_email_current.caption.text = "sma*******@gmail.com"
        btn_change_email_new.caption.hint = getString(R.string.new_email_address)
        btn_change_email_confirm.caption.hint = getString(R.string.confirm_new_email_address)


        setOnClickListeners(this::onClick,
            btn_change_email_new, btn_change_email_confirm,
            btn_change_email_cancel, btn_change_email_save)

    }

    fun onClick(view: View) {
        when(view) {
            btn_change_email_new -> {}
            btn_change_email_confirm -> {}
            btn_change_email_cancel -> activity?.finish()
            btn_change_email_save -> {}
        }

    }




}

