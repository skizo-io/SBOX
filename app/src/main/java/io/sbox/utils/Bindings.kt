package io.comico.utils

object Bindings {

/*

    @JvmStatic
    @BindingAdapter("url")
    fun loadImage(@NonNull imageView: ImageView, url: String?) {
        url?.apply { imageView.load(this) }
    }

    @JvmStatic
    @BindingAdapter("url")
    fun loadImage(@NonNull roundImageView: RoundImageView, url: String?) {
        url?.apply { roundImageView.imageView.load(this, R.color.gray04) }
    }


    @JvmStatic
    @BindingAdapter("url_circle")
    fun loadImageCircle(@NonNull imageView: ImageView, url: String?) {
        url?.apply { imageView.load(this, isCircle = true) }
    }

    @JvmStatic
    @BindingAdapter("status")
    fun setStatus(imageView: ImageView, status: String?) {
        when (status) {
//            "New" -> imageView.setImageResource(R.drawable.icon_status_new)
//            "Update" -> imageView.setImageResource(R.drawable.icon_status_update)
//            "Complete" -> imageView.setImageResource(R.drawable.icon_status_complete)
            else -> imageView.visible = false
        }
    }

    enum class Icon {
        event, pickup
    }





    @JvmStatic
    @BindingAdapter("visible")
    fun visible(@NonNull view: View, value: Boolean) {
        view.visibility = if (value) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("android:visibility")
    fun setVisibility(view: View, visible: Boolean) {
        view.setVisibility(if (visible) View.VISIBLE else View.GONE)
    }


    @BindingAdapter("android:drawableLeft")
    fun setDrawableLeft(textView: TextView, resourceId: Int) {
        var drawable: Drawable? = ContextCompat.getDrawable(textView.context, resourceId)
        drawable?.let {
            it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)
            textView.setCompoundDrawables(drawable, null, null, null)
        }
    }


    @JvmStatic
    @BindingAdapter("android:htmltext")
    fun setHtmlText(textView: TextView, html: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.text = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
        } else {
            textView.text = Html.fromHtml(html)
        }
    }

    private fun getColor(color: String?): Int {
        color?.let {
            if (it.startsWith("#"))
                return Color.parseColor(it)
            return  Color.parseColor("#$it")
        }
        return getColorFromRes(R.color.primaryDark)
    }
    @JvmStatic
    @BindingAdapter("backgroundColor")
    fun setBackgroundColor(view: View, color: String?) {
        view.setBackgroundColor(getColor(color))
    }

    @JvmStatic
    @BindingAdapter("cardBackgroundColor")
    fun setCardBackgroundColor(view: CardView, color: String?) {
        view.setCardBackgroundColor(getColor(color))
    }
*/


    /*
 @BindingAdapter("android:src")
    public static void setImageResource(ImageView imageView, int resource){
        imageView.setImageResource(resource);
    }


    @BindingAdapter(value = {"android:visibility", "visibleAnimType", "goneAnimType "}, requireAll = false)
    public static void setVisibility(View view, int visibility, int visibleAnimType, int goneAnimType ) {
    }


    */


}