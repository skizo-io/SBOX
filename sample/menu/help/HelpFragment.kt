package com.smackjeeves.ui.menu.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smackjeeves.ui.base.BaseFragment
import com.smackjeeves.R
import com.smackjeeves.model.Help
import com.smackjeeves.network.Api
import com.smackjeeves.network.send
import com.smackjeeves.ui.webview.WebViewFragment
import com.smackjeeves.utils.*
import kotlinx.android.synthetic.main.fragment_menu_help.*
import kotlinx.android.synthetic.main.item_appbar.*
import kotlinx.android.synthetic.main.item_menu_text.view.*
import skizo.library.extensions.click
import skizo.library.extensions.getLayoutFromResource
import skizo.library.extensions.resizeHeight
import skizo.library.extensions.toPx


class HelpFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle? = null) =
            HelpFragment().apply {
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
        return inflater.inflate(R.layout.fragment_menu_help, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        appbar_title.setText(R.string.help)
        appbar.setVisible(back = true)


        Api.service.getHelp().send(this::createList)

        /*
        // API... getHelp
        createList(
            Help(Result(200, ""), HelpData(
                listOf(
                    HelpContent("About", "www.smackjeeves.com/"),
                    HelpContent("Privacy Policy", "www.smackjeeves.com/privacy.php"),
                    HelpContent("Terms of Use", "www.smackjeeves.com/terms_of_use.php"),
                    HelpContent("Copyright Policy", "www.smackjeeves.com/copyright.php"),
                    HelpContent("FAQ/Contact Us", "www.smackjeeves.com/contact.php")
                )
            ))
        )
        */

    }


    fun createList(vo: Help) {
        help_layout.removeAllViews()
        vo.data.help.forEach {
            var helpContent = it
            context?.getLayoutFromResource(R.layout.item_menu_button)?.apply {
                help_layout.addView(this)
                resizeHeight(52.toPx)
                caption.text = helpContent.caption
                click { newFragment<WebViewFragment>(WebViewFragment.getBundle(helpContent.url, helpContent.caption)) }
            }
        }
    }


}