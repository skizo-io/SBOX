package io.comico.library.debug

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


@Suppress("DEPRECATION")
class Debugger {

    companion object {

        @SuppressLint("WrongConstant")
        fun showNotification(context: Context, emails:List<String>? = null) {
            val mNotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val builder: NotificationCompat.Builder =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val mChannel =
                        NotificationChannel(
                            "debugger",
                            "debugger",
                            NotificationManager.IMPORTANCE_DEFAULT
                        )
                    mNotificationManager.createNotificationChannel(mChannel)
                    NotificationCompat.Builder(context, mChannel.id)
                } else {
                    NotificationCompat.Builder(context)
                }


            val action = "${context.packageName}.log.share"

            val pendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    0,
                    Intent(action),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

            builder.setContentTitle("Click to share log (${context.packageName})")
                .setSmallIcon(android.R.drawable.ic_menu_save)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setCategory(Notification.CATEGORY_MESSAGE)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
            }

            var intentFilter = IntentFilter()
            intentFilter.addAction(action)
            context.registerReceiver(btnReceiverLogCapture, intentFilter)

            val NOTIFICATION_ID = 101055
            mNotificationManager.notify(NOTIFICATION_ID, builder.build())


        }

        var btnReceiverLogCapture: BroadcastReceiver = object : BroadcastReceiver() {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onReceive(context: Context, intent: Intent) {

//                val path = createLog(context)
                sendLog(context, createLog(context))
            }
        }


        fun createLog(context: Context): String {
            val log = StringBuilder()
            val divide = "---------------------- End"

            try {
                log.append("----------- Phone Infomation -----------\n")
                context?.apply {
                    log.append(
                        """
                    OS version = ${System.getProperty("os.version")}
                    API Level = ${Build.VERSION.SDK_INT}
                    Device = ${Build.DEVICE}
                    Model = ${Build.MODEL}
                    Product = ${Build.PRODUCT}
                    DIP = ${resources.configuration.densityDpi}
                    width = ${resources.displayMetrics.widthPixels}
                    height = ${resources.displayMetrics.heightPixels}

                    """.trimIndent()
                    )
                }

                // prefere...
                /*
                val prefsdir = File(context.applicationInfo.dataDir, "shared_prefs")
                if (prefsdir.exists() && prefsdir.isDirectory) {
                    val list: Array<String> = prefsdir.list()

                }
                */

                log.append("\n$divide\n\n")
            } catch (e: Exception) {
            }

            val path = context.cacheDir.toString()
            val filePath = "$path/log.txt"

            try {
                var file = File(path)
                if (!file.exists()) file.mkdirs()

                var line: String? = null

                file = File(filePath)
                if (file.exists()) {
                    var fis = FileInputStream(file)
                    var reader = BufferedReader(InputStreamReader(fis))
                    var sb = StringBuilder()

                    while (reader.readLine()?.also { line = it } != null) {

                        sb.append(
                            """
                            $line

                            """.trimIndent()
                        )
                    }
                    reader.close()

                    var temp = StringBuilder()
                    sb.toString().split(divide).forEachIndexed { index, s ->
                        if(index > 0) {
                            temp.append(s)
                        }
                    }

                    var list = temp.split("\n")
                    val max = 300
                    val size = list.size - 1
                    if(size > max) {
                        list = list.subList(size - max, size)
                        temp.clear()
                        temp.append(list.joinToString("\n"))
                    }

                    log.append(temp.toString())
                    log.append("\n---------------------- Temp \n\n")
                }

                val process = Runtime.getRuntime().exec("logcat -d")
                val bufferedReader = BufferedReader(
                    InputStreamReader(process.inputStream)
                )

                line = ""
                while (bufferedReader.readLine()?.also { line = it } != null) {
                    log.append(
                        """
                            $line

                            """.trimIndent()
                    )
                }
                bufferedReader.close()

                val bfw = BufferedWriter(FileWriter(filePath, false))
                bfw.write(log.toString())
                bfw.flush()
                bfw.close()
            } catch (e: Exception) {}

            return filePath
        }

        fun sendLog(context: Context, fullpath: String) {

            val sdf = SimpleDateFormat("MM.dd HH:mm")
            val subject = "${context.packageName} (${sdf.format(Calendar.getInstance().time)})"

            val intent = Intent(Intent.ACTION_SEND_MULTIPLE)
//            intent.setType("text/plain")
            intent.type = "plain/text"
//            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("skizo80@gmail.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            intent.putExtra(Intent.EXTRA_TEXT, "${context.packageName}")

            val uris = ArrayList<Uri>()
            val fileIn = File(fullpath)
            val u = FileProvider.getUriForFile(context, "${context.packageName}.provider", fileIn)
            uris.add(u)
            intent.putExtra(Intent.EXTRA_STREAM, uris);
            var mIntent = Intent.createChooser(intent, "Send email...")
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(mIntent)
        }
    }





}