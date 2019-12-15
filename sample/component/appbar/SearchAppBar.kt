package com.smackjeeves.ui.component.appbar

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View.OnFocusChangeListener
import android.view.View.OnKeyListener
import android.widget.RelativeLayout
import com.smackjeeves.R
import skizo.library.extensions.click
import skizo.library.extensions.hideKeyboard
import skizo.library.extensions.visible
import kotlinx.android.synthetic.main.item_search_appbar.view.*

class SearchAppBar : RelativeLayout {

    interface OnSearchActionListener {
        fun onSearch(keyword: String)
    }

    constructor(context: Context) : super(context) {
        if (context is OnSearchActionListener)
            listener = context
        initView()
    }

    lateinit var listener: OnSearchActionListener

    private fun initView() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_search_appbar, this)



        search_input.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->

            if (!hasFocus) hideKeyboard()


        }

        search_input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                viewCheck()
            }
        })

        search_input.setOnKeyListener(OnKeyListener { v, keyCode, event ->

            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                hideKeyboard()
                listener.onSearch(search_input.text.toString())
                search_input.setText("")
                return@OnKeyListener true
            }
            false
        })

        search_cancel.click {
            search_input.setText("")
        }

        viewCheck()
    }


    private fun viewCheck() {
        search_cancel.visible = search_input.text.isNotEmpty()
    }


}