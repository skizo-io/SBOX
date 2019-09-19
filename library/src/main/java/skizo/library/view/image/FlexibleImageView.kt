package skizo.library.view.image

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView


@SuppressLint("AppCompatCustomView")
class FlexibleImageView : ImageView {

    var isFixWidth = true

    private var isCustomSize = false

    private var rate = 0f

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val d = drawable

        var width = MeasureSpec.getSize(widthMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)
        if (d != null && isCustomSize == false) {
            if (isFixWidth) {
                height =
                    Math.ceil((width.toFloat() * d.intrinsicHeight.toFloat() / d.intrinsicWidth.toFloat()).toDouble())
                        .toInt()
            } else {
                width =
                    Math.ceil((height.toFloat() * d.intrinsicWidth.toFloat() / d.intrinsicHeight.toFloat()).toDouble())
                        .toInt()
            }
            setMeasuredDimension(width, height)
        } else if (isCustomSize && rate > 0) {
            if (isFixWidth) {
                height = (width * rate).toInt()
            } else {
                width = (height * rate).toInt()
            }
            setMeasuredDimension(width, height)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    fun setRate(rate: Float) {
        isCustomSize = true
        this.rate = rate
    }


    fun setWidthSize(height: Int, rate: Float) {
        isCustomSize = true
        val width = (height * rate).toInt()
        setMeasuredDimension(width, height)
    }

    fun setHeightSize(width: Int, rate: Float) {
        isCustomSize = true
        val height = (width * rate).toInt()
        setMeasuredDimension(width, height)
    }
}