package com.smackjeeves.network

import com.smackjeeves.network.base.ApiCallBack
import com.smackjeeves.network.base.BaseApi
import com.smackjeeves.BuildConfig
import com.smackjeeves.model.*
import com.smackjeeves.model.item.Balance
import com.smackjeeves.model.item.Profile
import com.smackjeeves.network.body.DeleteBookshelfBody
import com.smackjeeves.network.body.LoginBody
import com.smackjeeves.network.body.RecoveryBody
import com.smackjeeves.network.body.UserProfileBody
import com.smackjeeves.utils.Config
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.POST



/*
Api.service.getAppInfo().send(object : ApiCallBack<SampleModel>() { ... }
Api.service.getAppInfo().send()
Api.service.getDaily().send(
    this::func
)
Api.service.getDaily().send({
    func()
})
*/


class Api : BaseApi() {

    companion object {
        val service: ApiService by lazy {
            base.getService(BuildConfig.BASE_URL, ApiService::class.java)
        }

        val download: ApiDownload by lazy {
            base.getService("", ApiDownload::class.java)
        }


        val mockService: ApiService by lazy {
            var url = BuildConfig.BASE_URL
            if(Config.isDebugMode) url = "https://007a4c0e-9b82-4832-8ccb-c7a35a68aa17.mock.pstmn.io/"
            base.getService(url, ApiService::class.java)
        }

    }

    interface ApiDownload {
        @GET
        fun image(@Url url: String): Call<ResponseBody>
    }

    interface ApiService {

        // quest
        @POST("neoid/guest/login")
        fun guestLogin(): Call<NeoId>
        @POST("neoid/guest/convert")
        fun guestConvert(): Call<Default>
        @PUT("neoid/guest/recovery")
        fun guestRecovery(@Body body: RecoveryBody): Call<NeoId>


        enum class SessionType {
            LOGIN, SIGN_UP, CHANGE_PASSWORD, RESET_PASSWORD
        }
        @GET("neoid/rsa/{sessionType}")
        fun getSessionKey(@Path("sessionType") sessionType: SessionType): Call<SessionKey>

        @POST("neoid/member/login")
        fun login(@Body body: LoginBody): Call<NeoId>



        // User
        @GET("userinfo")
        fun getUserInfo(): Call<UserInfo>
        @GET("userinfo/balance")
        fun getUserInfoBalance(): Call<Balance>
        @GET("userinfo/profile")
        fun getUserInfoProfile(): Call<Profile>
        @GET("userinfo/profile")
        fun setUserInfoProfile(@Body body: UserProfileBody): Call<Profile>

        @POST("userinfo/profile/image")
        fun setUserProfileImage(): Call<Default>
        @DELETE("userinfo/profile/image")
        fun deleteUserProfileImage(): Call<Default>


        @GET("appInfo/aos")
        fun getAppInfo(): Call<AppInfo>

        @GET("help")
        fun getHelp(): Call<Help>


        @GET("news/categories")
        fun getNewsCategories(): Call<NewsCategories>
        // categoryKey: all / important / event / notice
        @GET("news/contents/{categoryKey}")
        fun getNews(@Path("categoryKey") categoryKey: String = "all",
                    @Query("lastId") lastId: Any = "",
                    @Query("count") count: Int = 50): Call<News>




        @GET("giftbox/messages")
        fun getGiftboxList(): Call<Giftbox>
        @PUT("giftbox/messages/{messageId}/receive")
        fun receiveGiftbox(@Path("messageId") messageId: Any = "all"): Call<Default>




        @GET("itembox")
        fun getItemBox(): Call<ItemBox>

        @GET("coin/history/purchase")
        fun getCoinHistory(): Call<CoinHistory>

        @GET("coin/history/usage")
        fun getCoinUsageHistory(): Call<CoinHistory>






        @GET("home")
        fun getHome(): Call<Home>


        //today / mon / tue / wed / thu / fri / sat / sun / comp
        @GET("exclusive/daily/{weekday}")
        fun getDaily(@Path("weekday") weekday: String = "today"): Call<Titles>

        //mostread, mostliked, trending, new
        @GET("exclusive/ranking/{category}")
        fun getRanking(@Path("category") category: String = "mostread"): Call<Titles>

        @GET("genres")
        fun getGenre(): Call<GenreId>
        @GET("exclusive/genre/{genreId}")
        fun getGenre(@Path("genreId") genreId: Int): Call<Titles>

        //newarrival / popularity
        @GET("discover/genre/{orderType}/{genreId}")
        fun getGenreDiscover(@Path("orderType") orderType: String,
                             @Path("genreId") genreId: Int,
                             @Query("startIndex") startIndex: Int,
                             @Query("count") count: Int): Call<Titles>

        @GET("discover/spotlight")
        fun getSpotlight(): Call<Titles>

        @GET("discover/popular")
        fun getPopular(): Call<Titles>


        // bookshelf
        @GET("bookshelf/favorite")
        fun getFavorite(): Call<Titles>
        @POST("bookshelf/favorite/{titleId}")
        fun setFavorite(@Path("titleId") titleId: Int): Call<Default>
        @DELETE("bookshelf/favorite/{titleId}")
        fun deleteFavorite(@Path("titleId") titleId: Int): Call<Default>
        @DELETE("bookshelf/favorite")
        fun deleteFavorite(@Body body: DeleteBookshelfBody): Call<Default>

        @GET("bookshelf/purchase")
        fun getPurchases(): Call<Titles>

        @GET("bookshelf/history")
        fun getHistory(): Call<Titles>



        @GET("search/trendwords")
        fun getTrendwords(): Call<TrendWords>
//        @FormUrlEncoded
        @GET("search/titles")
        fun getSearch(@Field("word", encoded = true) word: String): Call<Titles>




        @GET("title/{titleId}")
        fun getTitle(@Path("titleId") titleId: Int): Call<TitleInfo>



        @GET("title/{titleId}/article/{articleId}")
        fun getComicDetail(@Path("titleId") titleId: Int,
                           @Path("articleId") articleId: Int): Call<ContentInfo>

        @POST("title/{titleId}/article/{articleId}/read")
        fun postReadComic(@Path("titleId") titleId: Int,
                          @Path("articleId") articleId: Int): Call<Default>

        @POST("title/{titleId}/article/{articleId}/support")
        fun postSupportComic(@Path("titleId") titleId: Int,
                               @Path("articleId") articleId: Int): Call<Default>





        // test
        @GET("comment")
        fun getComment(): Call<CommentList>



        // comment
        //recent | popularity
        @GET("title/{titleId}/article/{articleId}/comment/{sort}")
        fun getComment(@Path("titleId") titleId: Int,
                       @Path("articleId") articleId: Int,
                       @Path("sort") sort: String,
                       @Query("lastId") lastCommentId: Any = "",
                       @Query("count") count: Int = 50): Call<CommentList>

        @POST("title/{titleId}/article/{articleId}/comment")
        fun postComment(@Path("titleId") titleId: Int,
                        @Path("articleId") articleId: Int,
                        @Body body: String): Call<Default>

        @DELETE("title/{titleId}/article/{articleId}/comment/{commentId}")
        fun deleteComment(@Path("titleId") titleId: Int,
                          @Path("articleId") articleId: Int,
                          @Query("commentId") commentId: Int): Call<Default>

        @POST("title/{titleId}/article/{articleId}/comment/{commentId}/report")
        fun reportComment(@Path("titleId") titleId: Int,
                          @Path("articleId") articleId: Int,
                          @Query("commentId") commentId: Int): Call<Default>

        @POST("title/{titleId}/article/{articleId}/comment/{commentId}/good")
        fun goodComment(@Path("titleId") titleId: Int,
                        @Path("articleId") articleId: Int,
                        @Query("commentId") commentId: Int): Call<Default>



    }


    interface NclickService {

    }


}


fun <T> Call<T>.send(callback: ApiCallBack<T> ) {
    enqueue(callback)
}
fun <T> Call<T>.send() {
    send(object : ApiCallBack<T>(this) {})
}
fun <T> Call<T>.send(complete: (T)->(Unit), fail: (()->Unit)? = null) {
    send(object : ApiCallBack<T>(this) {
        override fun onComplete(body: T) {
            super.onComplete(body)
            complete.invoke(body)
        }
        override fun onFail(url: String, code: Int, message: String) {
            super.onFail(url, code, message)
            fail?.apply {
                invoke()
            }
        }
    })
}





