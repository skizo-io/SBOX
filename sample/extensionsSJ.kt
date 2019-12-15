package com.smackjeeves.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.smackjeeves.R
import com.smackjeeves.SmackApplication
import com.smackjeeves.model.base.BaseResponse
import com.smackjeeves.network.send
import com.smackjeeves.ui.activity.EmptyActivity
import com.smackjeeves.ui.base.BaseFragment
import com.smackjeeves.ui.search.SearchActivity
import com.smackjeeves.ui.webview.WebViewFragment
import retrofit2.Call
import skizo.library.extensions.browse
import skizo.library.extensions.getSimpleName
import skizo.library.extensions.put
import skizo.library.extensions.trace
import kotlin.reflect.KFunction



fun Any?.getContext(): Context? {
    return takeIf { this is Context }?.let { it as Context } ?: SmackApplication.instance
}


//inline fun <reified T : Context> Context.newIntent(extras: Bundle): Intent = newIntent<T>(0, extras)
inline fun <reified T : Activity> Fragment.newActivity(vararg pairs: Pair<String, Any?>) =
    activity?.apply { newActivity<T>(*pairs) }

inline fun <reified T : Activity> Context.newActivity(vararg pairs: Pair<String, Any?>): Unit =
    this.startActivity(newIntent<T>(*pairs))

inline fun bundleOf(vararg pairs: Pair<String, Any?>) = Bundle(pairs.size).apply { put(*pairs) }

inline fun <reified T : BaseFragment> Any?.newFragment(bundle: Bundle? = null) {
    var argument = bundle ?: Bundle()
    argument.putString(EmptyActivity.FRAGMENT, T::class.java.canonicalName)
    getContext()?.startActivity(
        Intent(getContext(), EmptyActivity::class.java)
            .apply { argument?.let { putExtras(it) } }
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    )
}

@SuppressLint("WrongConstant")
inline fun <reified T : BaseFragment> Fragment.stackFragment(bundle: Bundle? = null) {
    var fragment = T::class.java.getMethod("newInstance", Bundle::class.java)?.let {
        it.invoke(null, bundle) as T
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

inline fun <reified T : BaseResponse> Any?.invokeApi(
    noinline complete: (T) -> Unit,
    api: KFunction<Call<T>>,
    vararg query: Any
) {
    api.call(*query).send(complete)
}


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
*/



fun View.showSnackbar(snackbarText: String, timeLength: Int) {
    Snackbar.make(this, snackbarText, timeLength).show()
}

fun ImageView.load(imageUrl: String, isProfile: Boolean = false) {
    val options =
        RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(if (isProfile) R.drawable.icon_profile else R.drawable.default_image)
            .error(if (isProfile) R.drawable.icon_profile else R.drawable.default_image)
    if (isProfile) options.optionalCircleCrop()
    Glide.with(this)
        .load(imageUrl)
        .apply(options)
        .thumbnail(0.1f)
        .into(this)
}




fun Context.openScheme(scheme: String) {
    var uri = Uri.parse(scheme)

    if( TextUtils.equals(uri.scheme, "smackjeeves") ) {

        schemeParser(uri, "comic/exclusive")?.apply {

            return
        }

        schemeParser(uri, "comic/discover/titlepage/{titleId}")?.apply {

            return
        }

        schemeParser(uri, "comic/discover/ranking")?.apply {

            return
        }

        schemeParser(uri, "comic/discover/comment/{titleId}/{articleId}")?.apply {

            return
        }

        schemeParser(uri, "web")?.apply {
            var url = get("url").toString()
            if(get("outbrowser") as Boolean) browse(url)
            else newFragment<WebViewFragment>(WebViewFragment.getBundle(url))
            return
        }

        schemeParser(uri, "tag/{tag}")?.apply {
            newActivity<SearchActivity>( SearchActivity.INTENT_KEYWORD.to(get("tag")) )
            return
        }



        schemeParser(uri, "sj/format/{var}")?.apply {
            return
        }
    }
}


private fun schemeParser(uri: Uri, format: String): HashMap<String, Any>? {
    var path = uri.host + uri.path

    var pathList = path.split("/")
    var formatList = format.split("/")

    if(pathList.size == formatList.size) {

        var params: HashMap<String, Any> = HashMap()
        var cnt = pathList.size

        for (i in 0 until cnt) {
            var f = formatList.get(i)
            var p = pathList.get(i)
            if(f.equals(p)) {
            } else if(Regex("""^\{.*\}$""").matches(f)) {
                params.put(f.replace("""^\{|\}$""".toRegex(), ""), p)
            } else return null
        }

        var paramNames = uri.queryParameterNames
        paramNames.forEach {
            params.put(it, uri.getQueryParameter(it))
        }

        trace("### scheme matches : ", path, params)

        return params
    }
    return null
}




/*
file check (db check)

download content
fun downloadContent(json: String)

load content (titleid, articleid)

loadContentList (from db, with file check)
return value : HashMap(titleid, List<articleid>)

delete content (titleid, articleid = null)
 */








