package com.smackjeeves.network.base

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.smackjeeves.BuildConfig
import com.smackjeeves.model.base.BaseResponse
import com.smackjeeves.network.Api
import com.smackjeeves.network.body.RecoveryBody
import com.smackjeeves.network.send
import com.smackjeeves.preferences.AppPreference
import com.smackjeeves.preferences.UserPreference
import com.smackjeeves.ui.menu.authenticate.LoginFragment
import com.smackjeeves.utils.*
import okhttp3.ConnectionPool
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import skizo.library.extensions.trace
import java.security.MessageDigest
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.sql.Timestamp
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlin.collections.ArrayList
import kotlin.experimental.and

open class BaseApi {

    companion object {
        val base: BaseApi = BaseApi()
        var retryApi: ArrayList<ApiCallBack<*>> = arrayListOf()
        fun retry() {
            retryApi.forEach {
                it.retryCount++
            }
            retryApi.clear()
        }
    }


    fun sha256Hex(s: String): String? {
        var SHA: String? = s
        try {
            val sh = MessageDigest.getInstance("SHA-256")
            sh.update(s.toByteArray())
            val byteData = sh.digest()
            val sb = StringBuffer()
            for (i in byteData.indices) {
                sb.append(Integer.toString((byteData[i] and 0xff.toByte()) + 0x100, 16).substring(1))
            }
            SHA = sb.toString()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            SHA = null
        }

        return SHA
    }


    private fun getHttpClient(): OkHttpClient {

        val ts = Timestamp(Date().time).time.toString()
        val hashKey = "3fadc2ab9383ec66db1b6e745d6ce73a"
        val token = hashKey + AppPreference.uuid + ts + UserPreference.neoNo
//        val hashToken = DigestUtils.sha256Hex( token )
//        val hashToken = String(Hex.encodeHex(DigestUtils.sha256( token.toByteArray() )))
//        val hashToken = sha256Hex(token)
        val hashToken = String(Hex.encodeHex(DigestUtils.sha256( token )))

        val interceptors = ArrayList<Interceptor>()
        interceptors.add(Interceptor { chain ->
            val builder = chain.request().newBuilder()
            builder.addHeader("X-smackjeeves-access-token", UserPreference.accessToken)
                .addHeader("X-smackjeeves-neono", UserPreference.neoNo)
                .addHeader("X-smackjeeves-client-os", "aos")
                .addHeader("X-smackjeeves-client-version", BuildConfig.VERSION_CODE.toString())
                .addHeader("X-smackjeeves-client-uuid", AppPreference.uuid)
                .addHeader("X-smackjeeves-client-adid", AppPreference.advertiginId)
                .addHeader("X-smackjeeves-request-time", ts)
                .addHeader("X-smackjeeves-hash-token", hashToken)
                .addHeader("Content-Type", "application/json")
            chain.proceed(builder.build())
        })

        if (Config.isDebugMode) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            interceptors.add(interceptor)
            interceptors.add(StethoInterceptor())
        }


        val trustAllManager = object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }
        val trustAllCerts = arrayOf<TrustManager>(trustAllManager)

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())
        val sslSocketFactory = sslContext.socketFactory

        val bFollowRedirects = false
        val TIMEOUT = 15000L
        var GLOBAL_CONNECTION_POOL = ConnectionPool(5, 1, TimeUnit.MINUTES)

        val builder = OkHttpClient.Builder()
        builder.followRedirects(bFollowRedirects)
        builder.sslSocketFactory(sslSocketFactory, trustAllManager)
        builder.hostnameVerifier { hostname, session -> true }
        builder.readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
        builder.connectionPool(GLOBAL_CONNECTION_POOL)


        if (interceptors != null && !interceptors.isEmpty()) {
            for (interceptor in interceptors) {
                builder.addInterceptor(interceptor)
            }
        }

        return builder.build()
    }


    fun <T> getService(baseUrl: String, service: Class<T>): T {
        val gson = GsonBuilder()
//            .registerTypeAdapterFactory(NullStringToEmptyAdapterFactory())
            .enableComplexMapKeySerialization()
//            .setDateFormat(DateFormat.LONG)
//            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
//            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .serializeNulls()
            .create()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(baseUrl)
            .client(getHttpClient())
            .build()
        return retrofit.create(service)
    }
}

/*
class StringAdapter : TypeAdapter<String>() {
    override fun read(reader: JsonReader): String {
        if(reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            return "0"
        }
        return reader.nextString()
    }

    override fun write(write: JsonWriter?, value: String?) {
        if(value == null)
            write?.nullValue()
        else
            write?.value(value)
    }
}

class NullStringToEmptyAdapterFactory<T> : TypeAdapterFactory {
    override fun <T : Any?> create(gson: Gson?, type: TypeToken<T>?): TypeAdapter<T>? {
        var rawType: Class<T> = type.rawType as (Class<T>)
        if (rawType != String.javaClass) {
            return null
        }
        return StringAdapter() as (TypeToken<T>)
    }
}
*/


open class ApiCallBack<T>(var call: Call<T>) : Callback<T> {

    val REQUEST_SUCCESS_CODE = 200
    val OPTINAL_UPDATE = 209
    val FORCE_UPDATE = 0
    var MAINTENANCE = 0

    val BAD_REQUEST = 400
    val FORBIDDEN = 403
    val NOT_FOUND = 404
    val INTERNAL_SERVER_ERROR = 500

    val CONTENT_TYPE_ERROR = 9000
    val NETWORK_EXCEPTION = 9001
    val PARSE_EXCEPTION = 9002
    val INTERFACE_EXCEPTION = 9003

    var retryCount = 0
        set(value) {
            if(value < 3) {
                call.clone().send(this)
                field = value
            }
        }

    override fun onResponse(call: Call<T>, response: retrofit2.Response<T>) {
        val url = response.raw().request().url().toString()

        if (response.isSuccessful) {
            val subtype = response.raw().body()?.contentType()?.subtype()

            if ("json" == subtype) {
                try {
                    response.body()?.let {
                        if (it is BaseResponse) {
                            val code = it.result?.code ?: INTERFACE_EXCEPTION
                            when (code) {
                                in 200..299 -> {
                                    it.sync()
                                    onComplete(it)
                                }
                                401 -> {
                                    if(UserPreference.isGuest) {
                                        BaseApi.retryApi.add(this)

                                        Api.service.guestRecovery( RecoveryBody() ).send({
                                            BaseApi.retry()
                                        })

                                    } else {
                                        newFragment<LoginFragment>()
                                    }
                                }
                                409 -> {
                                    AppPreference.reset()
                                    BaseApi.retryApi.add(this)
                                    BaseApi.retry()
                                }
                                else -> {
                                    onFail(url, code, it.result?.message ?: "")
                                }
                            }

                        }
                    }
                } catch (e: Exception) {
                    onFail(url, PARSE_EXCEPTION, e.message?:"")
                }
            } else {
                onFail(url, CONTENT_TYPE_ERROR, response.raw().body()?.contentType()?.toString() ?: "")
            }
        } else {
            onFail(url, INTERNAL_SERVER_ERROR, response.raw().toString())
        }

    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        onFail("", NETWORK_EXCEPTION, t.message ?: "")
    }

    open fun onComplete(body: T) {
        trace("##BaseApi## onComplete : body", Gson().toJson(body))
    }

    open fun onFail(url: String, code: Int, message: String) {
        trace(
            "##BaseApi## onFail : ",
            url,
            code,
            message
        )
//        showToast("$message ($code)")
    }

}

