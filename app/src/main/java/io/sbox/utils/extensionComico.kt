package io.comico.utils


/*

fun Any?.getContext(): Context? {
    return takeIf { this is Context }?.let { it as Context } ?: ComicoApplication.instance
}


inline fun <reified T : BaseFragment> Any?.newFragment(bundle: Bundle? = null) {
    val argument = bundle ?: Bundle()
    argument.putString(EmptyActivity.FRAGMENT, T::class.java.canonicalName)
    getContext()?.startActivity(
        Intent(getContext(), EmptyActivity::class.java)
            .apply { putExtras(argument) }
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    )
}

private var ACCOUNT_ERROR_REQUEST_CODE = 401

fun Activity.accountErrorActivityForResult(): Unit =
    this.startActivityForResult(
        newIntent<AccountActivity>(FRAGMENT_INTENT_KEY to FragmentIntentValue.ACCOUNTERROR.name),
        ACCOUNT_ERROR_REQUEST_CODE
    )

@SuppressLint("WrongConstant")
inline fun <reified T : BaseFragment> Fragment.stackFragment(bundle: Bundle? = null) {

    var fragment = if (bundle == null) {
        T::class.java.getMethod("newInstance")?.let {
            it.invoke(null) as T
        }
    } else {
        T::class.java.getMethod("newInstance", Bundle::class.java)?.let {
            it.invoke(null, bundle) as T
        }
    }

    activity?.let {
        val transaction = it.supportFragmentManager.beginTransaction()
        fragment?.let {
            transaction.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
            if (it.isAdded) transaction.show(it).commit()
            else transaction.add(id, it, fragment.getSimpleName).commit()
            transaction.addToBackStack(null)
        }
    }
}

*/
/**
 * invokeApi<Model>({function(model)}, Api.service::getTitle /api: KFunction<Call<Model>>/ , "")
 *//*

inline fun <reified T : BaseResponse> Any?.invokeApi(
    noinline complete: (T) -> Unit,
    api: KFunction<Call<T>>,
    vararg query: Any
) {
    api.call(*query).send(complete)
}


*/
/*

fun Fragment.addFragment(fragment: Fragment) {
    fragmentManager.beginTransaction()
        .setCustomAnimations(R.anim.slide_left_in,
            R.anim.slide_left_out,
            R.anim.slide_right_in,
            R.anim.slide_right_out)
        .add(R.id.container, fragment)
        .addToBackStack(fragment::class.java.simpleName)
        .hide(this)
        .commit()
*//*




fun View.showSnackbar(snackbarText: String, timeLength: Int) {
    Snackbar.make(this, snackbarText, timeLength).show()
}


fun ImageView.load(
    imageUrl: String,
    placeholder: Int? = null,
    isCircle: Boolean = false
) {
    var resId = placeholder.nullInit(R.color.transparent)
    val options =
        RequestOptions()
            .placeholder(resId)
            .error(resId)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
    drawable?.let {
        options.placeholder(it)
    }

    if (isCircle) options.optionalCircleCrop()
//    if (isRounded) options.transform(RoundedCorners(8.toPx) as Transformation<Bitmap>)
    Glide.with(this)
        .load(imageUrl)
        .apply(options)
        .dontAnimate()
//        .transform(CenterCrop(), RoundedCorners(30.toPx))
        .thumbnail(0.1f)
        .into(this)
}


fun Context.openScheme(scheme: String) {
    var uri = Uri.parse(scheme)

    if (TextUtils.equals(uri.scheme, "comico")) {

        schemeParser(uri, "comic/{comicId}")?.apply {
            val comicId = get("comicId")?.toInt() ?: 0
            newActivity<ComicActivity>(ComicActivity.INTENT_COMIC_ID.to(comicId))
            return
        }
        schemeParser(uri, "comic/{comicId}/chapter/{chapterId}")?.apply {
            val comicId = get("comicId")?.toInt() ?: 0
            val chapterId = get("chapterId")?.toInt() ?: 0
            newActivity<ComicActivity>(ComicActivity.INTENT_COMIC_ID.to(comicId))
            newActivity<ComicViewerActivity>(
                ComicViewerActivity.INTENT_COMIC_ID.to(comicId),
                ComicViewerActivity.INTENT_CHAPTER_ID.to(chapterId)
            )
            return
        }
        schemeParser(uri, "home")?.apply {
            dispatcherEvent(EventType.MAIN_SCHEME, R.id.navigation_home.to(""))
            return
        }
        schemeParser(uri, "daily/{type}")?.apply {
            dispatcherEvent(EventType.MAIN_SCHEME, R.id.navigation_daily.to(get("type")))
            return
        }
        schemeParser(uri, "ranking/{type}")?.apply {
            dispatcherEvent(EventType.MAIN_SCHEME, R.id.navigation_ranking.to(get("type")))
            return
        }
        schemeParser(uri, "ranking/genre/{type}")?.apply {
            dispatcherEvent(EventType.MAIN_SCHEME, R.id.navigation_ranking.to(get("type")))
            return
        }
        schemeParser(uri, "inbox/{type}")?.apply {
            //gift, message
            dispatcherEvent(EventType.MAIN_SCHEME, R.id.navigation_inbox.to(get("type")))
            return
        }
        schemeParser(uri, "library/{type}")?.apply {
            //subscribed, unlocked, recent
            dispatcherEvent(EventType.MAIN_SCHEME, R.id.navigation_library.to(get("type")))
            return
        }
        schemeParser(uri, "genre/{genre}")?.apply {
            newFragment<GenreListFragment>(GenreListFragment.getBundle(get("genre").toString()))
            return
        }
        schemeParser(uri, "search/{keyword}")?.apply {
            newActivity<SearchActivity>(SearchActivity.INTENT_KEYWORD.to(get("keyword")))
            return
        }
        schemeParser(uri, "menu")?.apply {
            newActivity<MenuActivity>()
            return
        }
        schemeParser(uri, "coin/history/distributed")?.apply {
            return
        }
        schemeParser(uri, "coin/history/used")?.apply {
            return
        }
        schemeParser(uri, "coin/sales")?.apply {
            newFragment<PurchaseCoinListFragment>(null)
            return
        }
        schemeParser(uri, "webview")?.apply {
            var url = get("url").toString()
            if (get("outbrowser") as Boolean) browse(url)
//            else newFragment<WebViewFragment>(WebViewFragment.getBundle(url))
            return
        }
        schemeParser(uri, "outbrowser")?.apply {
            var url = get("url").toString()
            if (get("outbrowser") as Boolean) browse(url)
//            else newFragment<WebViewFragment>(WebViewFragment.getBundle(url))
            return
        }


        // TODO TEST用スキーマ
        schemeParser(uri, "banner")?.apply {
            return
        }
    }
}


private fun schemeParser(uri: Uri, format: String): HashMap<String, String>? {
    var path = uri.host + uri.path

    var pathList = path.split("/")
    var formatList = format.split("/")

    if (pathList.size == formatList.size) {

        var params: HashMap<String, String> = HashMap()
        var cnt = pathList.size

        for (i in 0 until cnt) {
            var f = formatList.get(i)
            var p = pathList.get(i)
            if (f.equals(p)) {
            } else if (Regex("""^\{.*\}$""").matches(f)) {
                params.put(f.replace("""^\{|\}$""".toRegex(), ""), p)
            } else return null
        }

        var paramNames = uri.queryParameterNames
        paramNames.forEach {
            //            params.put(it, uri.getQueryParameter(it))
        }

        trace("### scheme matches : ", path, params)

        return params
    }
    return null
}


fun <T : View> T.backKeyInvalid(isWork: Boolean = true) = setOnKeyListener(object : View.OnKeyListener {
    override fun onKey(view: View, keyCode: Int, keyEvent: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.action == KeyEvent.ACTION_UP) {
            return isWork
        }
        return false
    }
})

fun <T : View> T.backKeyRestartApp(context: Context?) =
    setOnKeyListener(object : View.OnKeyListener {
        override fun onKey(view: View, keyCode: Int, keyEvent: KeyEvent): Boolean {
            if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.action == KeyEvent.ACTION_UP) {
                ExitProcess.restartApp(context!!)
                return true
            }
            return false
        }
    })

fun <T : View> T.backKeyEndApp(context: Context?) = setOnKeyListener(object : View.OnKeyListener {
    override fun onKey(view: View, keyCode: Int, keyEvent: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.action == KeyEvent.ACTION_UP) {
            ExitProcess.endApp(context!!)
            return true
        }
        return false
    }
})

fun restartApplication(context: Context) = ExitProcess.restartApp(context)
fun userResetRestartApplication(context: Context) = GlobalScope.launch {
    withContext(Dispatchers.Default) {
        UserPreference.userPreferenceReset()
    }
    if (UserPreference.neoIdUid.isNullOrBlank()) {
        ExitProcess.restartApp(context)
    }
}


fun EditText.makeClearableEditText(
    onIsNotEmpty: (() -> Unit?)?,
    onClear: (() -> Unit?)?,
    clearDrawable: Drawable?
) {
    val updateRightDrawable = {
        this.setCompoundDrawables(
            compoundDrawables[0], null,
            if (text.isNotEmpty()) clearDrawable else null,
            null
        )
    }
    updateRightDrawable()

    this.doAfterTextChanged {
        if (it!!.isNotEmpty()) {
            onIsNotEmpty?.invoke()
        }
        updateRightDrawable()
    }
    this.onRightDrawableClicked {
        this.text.clear()
        this.setCompoundDrawables(compoundDrawables[0], null, null, null)
        onClear?.invoke()
        this.requestFocus()
    }
}

fun EditText.makeClearableEditText(
    onIsNotEmpty: (() -> Unit?)?,
    onCleared: (() -> Unit?)?,
    isCompoundDrawables: Boolean
) {
    compoundDrawables[2]?.let { clearDrawable ->
        makeClearableEditText(
            onIsNotEmpty,
            onCleared,
            if (isCompoundDrawables) clearDrawable else null
        )
    }
}

@SuppressLint("ClickableViewAccessibility")
fun EditText.onRightDrawableClicked(onClicked: (view: EditText) -> Unit) {
    this.setOnTouchListener { v, event ->
        var hasConsumed = false
        if (v is EditText) {
            if (event.x >= v.width - v.totalPaddingRight) {
                if (event.action == MotionEvent.ACTION_UP) {
                    onClicked(this)
                }
                hasConsumed = true
            }
        }
        hasConsumed
    }
}



fun EditText.checkPatten(): Boolean {
    var checkedCount = 0
    if (this.text.toString().matches(AppPreference.strNumberPattern)) {
        checkedCount++
    }

    if (this.text.toString().matches(AppPreference.strAlphabetPattern)) {
        checkedCount++
    }

    if (this.text.toString().matches(AppPreference.strSymbolPattern)) {
        checkedCount++
    }
    return checkedCount >= 2
}


*/

