package skizo.library.view.image

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView


@SuppressLint("AppCompatCustomView")
class FlexibleImageView(context: Context, attrs: AttributeSet? = null) : ImageView(context, attrs) {

    var isFixWidth = true

    private var isCustomSize = false
    private var rate = 0f

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

    fun setSize(width: Int = 0, height: Int = 0, rate: Float = 0f) {


        isCustomSize = true
        val width = (height * rate).toInt()
        setMeasuredDimension(width, height)
    }

    fun setHeightSize(width: Int, rate: Float) {
        isCustomSize = true
        val height = (width * rate).toInt()
        setMeasuredDimension(width, height)
    }

    fun setRate(rate: Float) {
        isCustomSize = true
        this.rate = rate
    }

}