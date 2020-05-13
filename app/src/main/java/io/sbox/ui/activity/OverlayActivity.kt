package io.sbox.ui.activity

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import io.comico.library.Builder
import io.comico.library.extensions.trace
import io.sbox.R
import io.sbox.core.MainService
import io.sbox.ui.base.BaseActivity


@RequiresApi(Build.VERSION_CODES.M)
class OverlayActivity : BaseActivity() {

    init {
        trace(
            """SBOX
            |오토클릭
            |스케줄러
            |웹페이지파싱 (예약어 등록, 푸시, 리로드 타임 설정, 페이지 주소 설정, 카카오 메세지...)
            |투두리스트
            |번역
            |사진합치기
            |손전등,화면어둡게하기
            |
        """.trimMargin()
        )
    }

    private val canOverlay = registerForActivityResult(OverlayActivityResultContract()) {
        runService()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        runService()
    }

    private fun runService() {
        if (Settings.canDrawOverlays(this)) {
            if(Builder.isForeground) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                startForegroundService(MainService.newService(this))
                    startService(MainService.newService(this))
                } else {
                    startService(MainService.newService(this))
                }
                finish()
            }
        } else {
            AlertDialog.Builder(this)
                .setTitle(R.string.reject_dialog_title)
                .setMessage(R.string.reject_dialog_message)
                .setCancelable(false)
                .setPositiveButton(R.string.ok) { _, _ ->
                    canOverlay.launch("package:$packageName")
                }
                .setNegativeButton(R.string.finish) { _, _ ->
                    finish()
                }
                .show()
        }
    }
}


class OverlayActivityResultContract : ActivityResultContract<String, Boolean>() {

    override fun createIntent(context: Context, input: String?): Intent =
        Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse(input))

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean =
        resultCode == Activity.RESULT_OK
}