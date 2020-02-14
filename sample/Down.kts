package com.smackjeeves.ui.article.sub.select

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.OnLifecycleEvent
import com.google.gson.Gson
import com.smackjeeves.BuildConfig
import com.smackjeeves.R
import com.smackjeeves.model.ContentInfo
import com.smackjeeves.network.Api
import com.smackjeeves.network.send
import com.smackjeeves.ui.base.BaseFragment
import com.smackjeeves.utils.bundleOf
import com.smackjeeves.utils.load
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_select_download.*
import kotlinx.android.synthetic.main.item_appbar.*
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import okio.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.Result.response
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.http.GET
import skizo.library.extensions.trace
import skizo.library.extensions.visible
import java.io.*


class SelectDownloadFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle?) =
            SelectDownloadFragment().apply {
                arguments = bundle
            }

        @JvmStatic
        fun getBundle(titleId: Int, listSelectId: List<Int>) = bundleOf(
            "titleId".to(titleId),
            "listSelectId".to(listSelectId)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_select_download, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        appbar_title.setText(R.string.download)
        appbar.setVisible(close = true)


        arguments?.let {
            var titleId = it.getInt("titleId")
            var listSelectId = it.getSerializable("listSelectId") as List<Int>

            downloadStart(titleId, listSelectId)
        }

    }

    var currentCount = 0
    fun downloadStart(titleId: Int, listSelectId: List<Int>) {
        trace("--------------------------")
        trace(
            "##DOWNLOAD## downloadStart",
            titleId,
            listSelectId
        )

        getContent(titleId, listSelectId)
    }

    fun getContent(titleId: Int, listSelectId: List<Int>) {
        if(listSelectId.size > currentCount) {
            val articleId = listSelectId.get(currentCount)
            trace(
                "##DOWNLOAD## getContent",
                listSelectId.size,
                currentCount,
                articleId
            )
            Api.service.getComicDetail(titleId, articleId).send(this::downloadContent)
            currentCount++
        }
    }

    @SuppressLint("NewApi")
    fun downloadContent(vo: ContentInfo) {
        trace(
            "##DOWNLOAD## downloadContent",
            vo.data.article.id,
            vo.data.article.name,
            vo.data.article.thmUrl
        )
        trace(
            "##DOWNLOAD## downloadContent",
            vo.toString()
        )

        select_download_image.load(vo.data.article.thmUrl)
        select_download_title.text = vo.data.article.name


        trace(
            "##DOWNLOAD## Gson.fromJson",
            Gson().fromJson(Gson().toJson(vo), ContentInfo::class.java)
        )

        val titleid = vo.data.title.id
        val articleid = vo.data.article.id
        val name = "$titleid" + "_$articleid"

        val job = Job()
        val scope = CoroutineScope(Dispatchers.Default + job)

        scope.launch {
            File(activity?.filesDir, "download/$name").mkdirs()
            File(activity?.filesDir, "download/$name/contentInfo.json").let {
                if(!it.exists()) it.createNewFile()
                it.writeText(Gson().toJson(vo))
            }

            async(Dispatchers.IO) {




            }.await()



        }
//        job.cancel()






        buildingRetrofit {
            activity?.runOnUiThread {
                //                progressBar.progress = it
                trace(
                    "##DOWNLOAD## download progress",
                    it
                )
            }
        }


//        trace("##DOWNLOAD## getContentImage", vo.data.contents.imgs)
//        Api.download.image(vo.data.article.thmUrl).send()
//        vo.data.contents.imgs.forEach {
//
//        }




        // load
        if(true) { //(check db, file, folder)
            File(activity?.filesDir, "download/$name/contentInfo.json").let {
                trace(
                    "##DOWNLOAD## openFileInput",
                    Gson().fromJson(it.reader().readText(), ContentInfo::class.java)
                )
            }
        }
    }


    fun setDataBase() {


        // check db, folder, file

        downloadComplete()
    }

    interface RetrofitService {

        /**
         * 파일 요청.
         */
        @GET("article/64/2/thumbnail/64_2_SMALL_1560495046768.jpg")
        fun requestFile(): Flowable<ResponseBody>
    }


    class ProgressResponseBody(val responseBody: ResponseBody, val onAttachmentDownloadUpdate: (Int) -> Unit) : ResponseBody() {

        private var bufferedSource = source(responseBody.source()).buffer()

        override fun contentLength(): Long {
            return responseBody.contentLength()
        }

        override fun contentType(): MediaType? {
            return responseBody.contentType()
        }

        override fun source(): BufferedSource {
            return bufferedSource
        }

        private fun source(source: Source): Source {
            return object : ForwardingSource(source) {
                var totalBytesRead = 0L
                override fun read(sink: Buffer, byteCount: Long): Long {
                    val bytesRead = super.read(sink, byteCount)

                    totalBytesRead += if (bytesRead != -1L) bytesRead else 0

                    val percent = if (bytesRead == -1L) 100f else totalBytesRead.toFloat() / responseBody.contentLength().toFloat() * 100

                    onAttachmentDownloadUpdate(percent.toInt())

                    return bytesRead
                }
            }
        }
    }


    private fun createOkHttpProgressClient(onAttachmentDownloadUpdate: (Int) -> Unit): OkHttpClient {
        if (BuildConfig.DEBUG) {
            val builder = OkHttpClient.Builder()
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(interceptor)
            builder.addInterceptor { chain ->
                val originalResponse = chain.proceed(chain.request())
                originalResponse.newBuilder()
                    .body(originalResponse.body()?.let { ProgressResponseBody(it, onAttachmentDownloadUpdate) })
                    .build()
            }
            return builder.build()
        } else {
            val builder = OkHttpClient.Builder()
            return builder.build()
        }
    }

    private fun buildingRetrofit(onAttachmentDownloadUpdate: (Int) -> Unit){

        val disposable = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("https://alpha-images.smackjeeves.com/")
            .client(createOkHttpProgressClient(onAttachmentDownloadUpdate))
            .build()
            .create(RetrofitService::class.java)
            .requestFile()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //TODO: 네트워크 완료 후 처리
                trace(
                    "##DOWNLOAD## download complete",
                    activity?.filesDir,
                    it
                )

                it?.let {

                    try { // todo change the file location/name according to your needs
                        val futureStudioIconFile =
                            File(activity?.filesDir.toString() + File.separator + "download" + File.separator + "Future Studio Icon.png")
                        var inputStream: InputStream? = null
                        var outputStream: OutputStream? = null
                        try {
                            val fileReader = ByteArray(4096)
                            val fileSize: Long = it.contentLength()
                            var fileSizeDownloaded: Long = 0
                            inputStream = it.byteStream()
                            outputStream = FileOutputStream(futureStudioIconFile)
                            while (true) {
                                val read: Int = inputStream.read(fileReader)
                                if (read == -1) {
                                    break
                                }
                                outputStream.write(fileReader, 0, read)
                                fileSizeDownloaded += read.toLong()
                                trace(
                                    "@@@@@@@ File Download: ",
                                    "$fileSizeDownloaded of $fileSize"
                                )
                            }
                            outputStream.flush()
                            true
                        } catch (e: IOException) {
                            false
                        } finally {
                            if (inputStream != null) {
                                inputStream.close()
                            }
                            if (outputStream != null) {
                                outputStream.close()
                            }
                        }
                    } catch (e: IOException) {
                        false
                    }



                }


                trace("@@@@@@@@@@@@ Do OKOKOK ")
            }, {
                //TODO: 네트워크 에러 처리
                trace("##DOWNLOAD## download error", it)
            })
//        compositeDisposable.add(disposable)
    }



    fun downloadComplete() {

        appbar_title.setText(R.string.complete)
        appbar.setVisible(close = true)
        select_download_complete.visible = true
    }

    fun downloadFail() {

        appbar_title.setText(R.string.download_fail)
        appbar.setVisible(close = true)
        select_download_complete.visible = true

    }

}