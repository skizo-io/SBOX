package io.sbox.sample


/*

    fun folderCheck() {
        val folder =
            File(Environment.getExternalStorageDirectory().toString() + "/cloze_screenshots")
        var success: Boolean = true
        // If the folder cloze not exist, create one
        if (!folder.exists()) {
            success = folder.mkdir()
        } else {
            ScreenShot()
        }
        // If mkdir successful
        if (success) {
            ScreenShot()
        } else {
            Log.e("mkdir_fail", "QQ")
        }
    }

    private fun ScreenShot() {
        val filePath: String =
            Environment.getExternalStorageDirectory().toString() + "/cloze_screenshots/temp.png"

        // create bitmap screen capture
        val bitmap: Bitmap

        /*
        val v1: View = getWindow().getDecorView().getRootView()
        v1.isDrawingCacheEnabled = true
        bitmap = Bitmap.createBitmap(v1.drawingCache)
        v1.isDrawingCacheEnabled = false
        var fout: OutputStream? = null
        val imageFile = File(filePath)
        try {
            fout = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout)
            fout.flush()
            fout.close()
            Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
        } catch (e: FileNotFoundException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        */
    }

    /*
Bitmap bitmap;
 ViewGroup v1 = findViewById(R.id.layout_id);
 v1.setDrawingCacheEnabled(true);
 bitmap = Bitmap.createBitmap(v1.getDrawingCache());
 v1.setDrawingCacheEnabled(false);
     */

    fun takeSnapshot(givenView: View, width: Int, height: Int): Bitmap? {
        val bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val snap = Canvas(bm)
        givenView.layout(
            0,
            0,
            givenView.layoutParams.width,
            givenView.layoutParams.height
        )
        givenView.draw(snap)
        return bm
    }

 */