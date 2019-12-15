package com.smackjeeves.ui.menu.authenticate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smackjeeves.R
import com.smackjeeves.ui.base.BaseFragment
import skizo.library.extensions.setOnClickListeners
import kotlinx.android.synthetic.main.fragment_change_password.*
import kotlinx.android.synthetic.main.item_appbar.*
import kotlinx.android.synthetic.main.item_menu_button.view.*

class ChangePasswordFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle? = null) =
            ChangePasswordFragment().apply {
                arguments = Bundle().apply {
                    // putString(ARG_PARAM1, param1)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        appbar_title.setText(R.string.change_password)
        appbar.setVisible(back = true)


        btn_change_password_current.caption.hint = getString(R.string.current_password)
        btn_change_password_new.caption.hint = getString(R.string.new_password)
        btn_change_password_confirm.caption.hint = getString(R.string.confirm_new_password)


        setOnClickListeners(this::onClick,
            btn_change_password_cancel, btn_change_password_save)

    }

    fun onClick(view: View) {
        when(view) {
            btn_change_password_cancel -> activity?.finish()
            btn_change_password_save -> {}
        }

    }




}

