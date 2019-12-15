package com.smackjeeves.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smackjeeves.utils.Config
import com.smackjeeves.ui.base.BaseFragment
import com.smackjeeves.R
import com.smackjeeves.event.EventType
import skizo.library.extensions.addEventReceiver
import com.smackjeeves.preferences.UserPreference
import com.smackjeeves.ui.menu.authenticate.LoginFragment
import com.smackjeeves.ui.menu.download.DownloadFragment
import com.smackjeeves.ui.menu.giftbox.GiftboxFragment
import com.smackjeeves.ui.menu.help.HelpFragment
import com.smackjeeves.ui.menu.news.NewsMainFragment
import com.smackjeeves.ui.menu.profile.ProfileFragment
import com.smackjeeves.ui.menu.settings.SettingsFragment
import com.smackjeeves.ui.webview.WebViewFragment
import com.smackjeeves.utils.*
import kotlinx.android.synthetic.main.fragment_main_menu.*
import skizo.library.extensions.browse
import skizo.library.extensions.setOnClickListeners
import skizo.library.extensions.visible


class MenuFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle? = null) =
            MenuFragment().apply {
                arguments = Bundle().apply {
                    // putString(ARG_PARAM1, param1)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            // param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_main_menu, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        menu_login_layout.visible = UserPreference.isGuest
        btn_menu_login.visible = UserPreference.isGuest


        setUserInfo()

        addEventReceiver(
            EventType.DATA_RELOAD_BALANCE,
            this::setUserInfo
        )
        addEventReceiver(
            EventType.DATA_RELOAD_USERINFO,
            this::setUserInfo
        )


        setOnClickListeners(this::onClick,
            btn_menu_login_layout_cancel, btn_menu_purchase,
            image_menu_profile, text_menu_username,
            btn_menu_login, btn_menu_login_layout_login, btn_menu_purchase, btn_menu_giftbox,
            btn_menu_download, btn_menu_sitenews, btn_menu_setting, btn_menu_help, btn_menu_forum)
    }



    fun setUserInfo() {


        menu_coin.text = UserPreference.balanceCoin?.value.toString()
        menu_point.text = UserPreference.balanceTicket?.daily.toString()


    }





    fun onClick(view: View) {
        when(view) {
            btn_menu_login_layout_cancel -> {
                menu_login_layout.visible = false
            }
            btn_menu_login_layout_login,
            btn_menu_login -> {
                newFragment<LoginFragment>()
            }
            image_menu_profile,
            text_menu_username -> {
                newFragment<ProfileFragment>()
            }
            btn_menu_purchase -> newFragment<WebViewFragment>(WebViewFragment.getBundle("https://www.comico.jp/", getString(R.string.coin_purchase)))
            btn_menu_giftbox -> newFragment<GiftboxFragment>()
            btn_menu_download -> newFragment<DownloadFragment>()
            btn_menu_sitenews -> newFragment<NewsMainFragment>()
            btn_menu_setting -> stackFragment<SettingsFragment>()
            btn_menu_help -> stackFragment<HelpFragment>()
            btn_menu_forum ->  browse(Config.PATH_FORUM)


        }

    }



}