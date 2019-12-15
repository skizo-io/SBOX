package com.smackjeeves.ui.menu.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smackjeeves.ui.base.BaseFragment
import com.smackjeeves.R
import com.smackjeeves.preferences.UserPreference
import com.smackjeeves.ui.menu.authenticate.AccountInfoFragment
import com.smackjeeves.ui.menu.settings.coinhistory.CoinHistoryMainFragment
import com.smackjeeves.ui.menu.settings.itembox.ItemBoxMainFragment
import com.smackjeeves.utils.*
import kotlinx.android.synthetic.main.fragment_menu_settings.*
import kotlinx.android.synthetic.main.item_appbar.*
import kotlinx.android.synthetic.main.item_menu_button.view.*
import skizo.library.extensions.setOnClickListeners
import skizo.library.extensions.visible


class SettingsFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle?) =
            SettingsFragment().apply {
                arguments = bundle
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_menu_settings, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        appbar_title.setText(R.string.settings)
        appbar.setVisible(back = true)

        btn_settting_account.visible = !UserPreference.isGuest

        btn_settting_account.caption.setText(R.string.account)
        btn_settting_coin_history.caption.setText(R.string.coin_history)
        btn_settting_item_box.caption.setText(R.string.item_box)
        btn_settting_subscription.caption.setText(R.string.subscription)

        setOnClickListeners(this::onClick,
            btn_settting_account, btn_settting_coin_history,
            btn_settting_item_box, btn_settting_subscription)

    }

    fun onClick(view: View) {
        when(view) {
            btn_settting_account -> newFragment<AccountInfoFragment>()
            btn_settting_coin_history -> newFragment<CoinHistoryMainFragment>()
            btn_settting_item_box -> newFragment<ItemBoxMainFragment>()
            btn_settting_subscription -> {}
        }

    }

}