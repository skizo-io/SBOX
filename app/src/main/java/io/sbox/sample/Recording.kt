package io.sbox.sample

import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Environment
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.annotation.RequiresApi
import io.comico.library.extensions.getSimpleName
import io.comico.library.extensions.trace
import io.sbox.ui.base.BaseActivity
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class Recording : BaseActivity() {




    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getWebsite2() {

        trace("@@@@@@@@@@@@@ getWebsite2")

//        mMediaRecorder = MediaRecorder()

//        mProjectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager

//        windowManager.defaultDisplay.getMetrics(mDisplayMetrics)

//        prepareRecording()


//        startRecording()
    }





    lateinit var mProjectionManager: MediaProjectionManager
    private var CAST_PERMISSION_CODE = 22
    private var mDisplayMetrics: DisplayMetrics? = null
    private var mMediaProjection: MediaProjection? = null
    private var mVirtualDisplay: VirtualDisplay? = null
    private var mMediaRecorder: MediaRecorder? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun startRecording() {
        // If mMediaProjection is null that means we didn't get a context, lets ask the user
        if (mMediaProjection == null) {
            // This asks for user permissions to capture the screen
            startActivityForResult(mProjectionManager?.createScreenCaptureIntent(), CAST_PERMISSION_CODE);
            return;
        }
        mVirtualDisplay = getVirtualDisplay();
        mMediaRecorder?.start();
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun stopRecording() {

        if (mMediaRecorder != null) {
            mMediaRecorder?.stop();
            mMediaRecorder?.reset();
        }
        if (mVirtualDisplay != null) {
            mVirtualDisplay?.release();
        }
        if (mMediaProjection != null) {
            mMediaProjection?.stop();
        }

        prepareRecording();
    }

    fun getCurSysDate(): String {
        return SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Date());
    }

    fun prepareRecording() {
        try {
            mMediaRecorder?.prepare();
        } catch (e: Exception) {
            return
        }

        var directory = "${Environment.getExternalStorageDirectory()} ${File.separator} Recordings";

        trace("@@@@@@@@@@@ ", directory)

        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Toast.makeText(this, "Failed to get External Storage", Toast.LENGTH_SHORT).show();
            return;
        }
        var folder = File(directory);
        var success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        var filePath:String
        if (success) {
            var videoName = ("capture_" + getCurSysDate() + ".mp4");
            filePath = directory + File.separator + videoName;
        } else {
            Toast.makeText(this, "Failed to create Recordings directory", Toast.LENGTH_SHORT).show();
            return;
        }

        var width = mDisplayMetrics?.widthPixels ?: 0
        var height = mDisplayMetrics?.heightPixels ?: 0

        mMediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder?.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder?.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mMediaRecorder?.setVideoEncodingBitRate(512 * 1000);
        mMediaRecorder?.setVideoFrameRate(30);
        mMediaRecorder?.setVideoSize(width, height);
        mMediaRecorder?.setOutputFile(filePath);
    }
/*

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode != CAST_PERMISSION_CODE) {
            // Where did we get this request from ? -_-
            trace("@@@@@@@@ Unknown request code: " + requestCode);
            return;
        }
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "Screen Cast Permission Denied :(", Toast.LENGTH_SHORT).show();
            return;
        }
        mMediaProjection = mProjectionManager.getMediaProjection(resultCode, data);
        // TODO Register a callback that will listen onStop and release & prepare the recorder for next recording
        // mMediaProjection.registerCallback(callback, null);
        mVirtualDisplay = getVirtualDisplay();
        mMediaRecorder?.start();
    }
*/


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun getVirtualDisplay() : VirtualDisplay? {
        var screenDensity = mDisplayMetrics?.densityDpi ?: 0
        var width = mDisplayMetrics?.widthPixels ?: 0
        var height = mDisplayMetrics?.heightPixels ?: 0

        return mMediaProjection?.createVirtualDisplay(getSimpleName,
            width, height, screenDensity,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            mMediaRecorder?.getSurface(), null /*Callbacks*/, null /*Handler*/)
    }




/*

    private fun startScreenRecord(intent: Intent) {
        if (DEBUG) Log.v(TAG, "startScreenRecord:sMuxer=$sMuxer")
        synchronized(sSync) {
            if (sMuxer == null) {
                val resultCode = intent.getIntExtra(EXTRA_RESULT_CODE, 0)
                // get MediaProjection
                val projection: MediaProjection =
                    mMediaProjectionManager.getMediaProjection(resultCode, intent)
                if (projection != null) {
                    val metrics: DisplayMetrics = getResources().getDisplayMetrics()
                    val density: Int = metrics.densityDpi
                    if (DEBUG) Log.v(TAG, "startRecording:")
                    try {
                        sMuxer =
                            MediaMuxerWrapper(".mp4") // if you record audio only, ".m4a" is also OK.
                        if (true) {
                            // for screen capturing
                            MediaScreenEncoder(
                                sMuxer, mMediaEncoderListener,
                                projection, metrics.widthPixels, metrics.heightPixels, density
                            )
                        }
                        if (true) {
                            // for audio capturing
                            MediaAudioEncoder(sMuxer, mMediaEncoderListener)
                        }
                        sMuxer.prepare()
                        sMuxer.startRecording()
                    } catch (e: IOException) {
                        Log.e(TAG, "startScreenRecord:", e)
                    }
                }
            }
        }
    }


    private fun stopScreenRecord() {
        if (DEBUG) Log.v(TAG, "stopScreenRecord:sMuxer=$sMuxer")
        synchronized(sSync) {
            if (sMuxer != null) {
                sMuxer.stopRecording()
                sMuxer = null
                // you should not wait here
            }
        }
    }

    private fun pauseScreenRecord() {
        synchronized(sSync) {
            if (sMuxer != null) {
                sMuxer.pauseRecording()
            }
        }
    }

    private fun resumeScreenRecord() {
        synchronized(sSync) {
            if (sMuxer != null) {
                sMuxer.resumeRecording()
            }
        }
    }


    */


    /*

    // 간략화된 GET, POST
Document google1 = Jsoup.connect("http://www.google.com").get();
Document google2 = Jsoup.connect("http://www.google.com").post();

// Response로부터 Document 얻어오기
Connection.Response response = Jsoup.connect("http://www.google.com")
                                    .method(Connection.Method.GET)
                                    .execute();
Document google3 = response.parse();


Connection.Response response = Jsoup.connect("http://www.google.com")
                                    .method(Connection.Method.GET)
                                    .execute();
Document document = response.parse();

String html = document.html();
String text = document.text();


Connection.Response response = Jsoup.connect("http://www.google.com")
                                    .method(Connection.Method.GET)
                                    .execute();
Document googleDocument = response.parse();
Element btnK = googleDocument.select("input[name=btnK]").first();
String btnKValue = btnK.attr("value");

System.out.println(btnKValue); // Google 검색
     */


//    private class MainPageTask extends AsyncTask<Void,Void,Void> {
//    private Elements element;
//    @Override protected void onPostExecute(Void ®) {
// doInBackground 작업이 끝나고 난뒤의 작업 mainHello.setText(element.text()); }

// @Override protected Void doInBackground(Void... params) {
// 백그라운드 작업이 진행되는 곳. try {
// Document doc = Jsoup.connect("http://example.com").get();
// element = doc.select("#algoList > tbody");
// } catch (IOException e) {
// e.printStackTrace();
// }
// return null; } }


}