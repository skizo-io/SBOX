package io.sbox.sample

class Recording02 {

    /*
    private var mVirtualDisplay: VirtualDisplay? = null
    private var mMediaRecorder: MediaRecorder? = null
    private var mMediaProjection: MediaProjection? = null
    private val callback: MediaProjectionCallback? = null
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val projectionManager =
            context.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        mMediaProjection!!.registerCallback(callback, null)
        initRecorder()
        mMediaRecorder!!.prepare()
        mVirtualDisplay = createVirtualDisplay()
        mMediaRecorder!!.start()
    }

    fun initRecorder() {
        path = "/sdcard/Record/video" + ".mp4"
        recId = "capture-" + System.currentTimeMillis() + ".mp4"
        val myDirectory =
            File(Environment.getExternalStorageDirectory(), "Record")
        mMediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mMediaRecorder!!.setVideoSource(MediaRecorder.VideoSource.SURFACE)
        mMediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mMediaRecorder!!.setVideoEncoder(MediaRecorder.VideoEncoder.H264)
        mMediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mMediaRecorder!!.setVideoEncodingBitRate(MainFragment.bitRate)
        mMediaRecorder!!.setVideoFrameRate(30)
        mMediaRecorder!!.setVideoSize(
            MainFragment.DISPLAY_WIDTH,
            MainFragment.DISPLAY_HEIGHT
        )
        mMediaRecorder.setOutputFile(path)
    }

    private fun createVirtualDisplay(): VirtualDisplay {
        return mMediaProjection!!.createVirtualDisplay(
            "MainActivity",
            MainFragment.DISPLAY_WIDTH, MainFragment.DISPLAY_HEIGHT, MainFragment.screenDensity,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            mMediaRecorder!!.surface, null *//*Callbacks*//*, null *//*Handler*//*
        )
    }

    inner class MediaProjectionCallback :
        MediaProjection.Callback() {
        override fun onStop() {
            mMediaRecorder!!.stop()
            // mMediaRecorder.reset();
            mMediaRecorder!!.release()
            mMediaProjection!!.unregisterCallback(callback)
            mMediaProjection = null
            mMediaRecorder = null
        }
    }
    */
}