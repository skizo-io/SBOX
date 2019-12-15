package com.smackjeeves.ui.menu.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smackjeeves.R
import com.smackjeeves.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.item_appbar.*
import kotlinx.android.synthetic.main.item_menu_text.view.*

class ProfileFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle? = null) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    // putString(ARG_PARAM1, param1)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        appbar_title.setText(R.string.edit_profile)
        appbar.setVisible(back = true)

        txt_edit_profile_username.caption.setText(R.string.username)
        txt_edit_profile_name.caption.setText(R.string.name)
        txt_edit_profile_birthday.caption.setText(R.string.birthday)

        input_edit_profile_username.caption.setText("SmackJeevesTest")
        input_edit_profile_name.caption.hint = "John Doe"
        input_edit_profile_birthday.caption.hint = "mm/dd/yyyy"
    }

    fun onClick(view: View) {

    }






}