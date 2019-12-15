package skizo.sbox.ui.activity

import android.os.Bundle
import android.widget.ImageView
import skizo.library.extensions.delayed
import skizo.library.extensions.newActivity
import skizo.library.extensions.toPx
import skizo.library.extensions.trace
import skizo.sbox.R
import skizo.sbox.ui.base.BaseActivity

class SplashActivity: BaseActivity() {

    init {

        trace("""SBOX
            |오토클릭
            |스케줄러
            |웹페이지파싱 (예약어 등록, 푸시, 리로드 타임 설정, 페이지 주소 설정, 카카오 메세지...)
            |투두리스트
            |번역
            |사진합치기
            |손전등,화면어둡게하기
            |
        """.trimMargin())



    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ImageView(applicationContext).let {
            it.setImageResource(R.mipmap.ic_launcher)
            it.setPadding(16.toPx, 0, 16.toPx, 0)
            setContentView(it)
        }

        initialize()
    }


    private fun initialize() {

        startMainActivity()

    }

    private fun startMainActivity() {
        delayed({
            newActivity<MainActivity>()
            finish()
        })
    }


    private fun guestLoginError() {

    }

}