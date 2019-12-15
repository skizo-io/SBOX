package com.smackjeeves.ui.component.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.LayoutRes
import com.smackjeeves.R
import kotlinx.android.synthetic.main.item_dialog.*
import skizo.library.extensions.click
import skizo.library.extensions.isNotEmptyFunc
import skizo.library.extensions.notNull
import skizo.library.extensions.visible


class SJDialog : Dialog {

    enum class DialogType {
        TYPE_DEFALUT, TYPE_VIEW, TYPE_RESOURCE
    }


    var title: String = ""
    var message: String = ""
    var positive: String? = ""
    var negatave: String? = ""

    constructor(
        context: Context,
        title: String = "",
        message: String = "",
        positive: String? = "",
        negatave: String? = ""
    ) : super(context) {
        this.title = title
        this.message = message
        this.positive = positive
        this.negatave = negatave
        init(DialogType.TYPE_DEFALUT)
    }

    var view: View? = null

    constructor(
        context: Context,
        view: View
    ) : super(context) {
        this.view = view
        init(DialogType.TYPE_VIEW)
    }

    var resId: Int? = null

    constructor(
        context: Context,
        @LayoutRes resId: Int
    ) : super(context) {
        this.resId = resId
        init(DialogType.TYPE_RESOURCE)
    }


    var type: DialogType = DialogType.TYPE_DEFALUT
    fun init(type: DialogType) {
        this.type = type
    }


    var positiveListener: (() -> Unit)? = null
    var negativeListener: (() -> Unit)? = null
    var callbackListener: (() -> Unit)? = null
    open fun setOnDialogListener(
        positive: (() -> Unit)? = null,
        negative: (() -> Unit)? = null,
        callback: (() -> Unit)? = null
    ): SJDialog {
        positiveListener = positive
        setOnCancelListener { negative?.let { it() } }
        setOnDismissListener { callback?.let { it() } }
        return this
    }

    open fun disableTouchCancel(): SJDialog {
        setCanceledOnTouchOutside(false)
        return this
    }

    open fun disableCancel(): SJDialog {
        setCancelable(false)
        return this
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        var layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.8f
        window.attributes = layoutParams
        window.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        requestWindowFeature(Window.FEATURE_NO_TITLE)


        if (type == DialogType.TYPE_RESOURCE)
            resId?.let { setContentView(it) }
        else if (type == DialogType.TYPE_VIEW)
            view?.let { setContentView(it) }
        else {
            setContentView(R.layout.item_dialog)

            if (type == DialogType.TYPE_DEFALUT) {
                dialog_title.text = title
                dialog_message.text = message

                positive.notNull({
                    it.isNotEmptyFunc { dialog_positive.text = it }

                    dialog_positive.click {
                        positiveListener?.let { it() }
                        dismiss()
                    }
                }, {
                    dialog_positive.visible = false
                })

                negatave.notNull({
                    disableCancel()
                    disableTouchCancel()

                    it.isNotEmptyFunc { dialog_negative.text = it }

                    dialog_negative.click {
                        cancel()
                    }
                }, {
                    dialog_negative.visible = false
                })
            }
        }


    }


}
