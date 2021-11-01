package com.padedatingapp.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraUtils
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.VideoResult
import com.otaliastudios.cameraview.controls.Mode
import com.otaliastudios.cameraview.controls.PictureFormat
import com.otaliastudios.cameraview.controls.Preview
import com.otaliastudios.transcoder.Transcoder
import com.otaliastudios.transcoder.TranscoderListener
import com.otaliastudios.transcoder.strategy.DefaultVideoStrategies
import com.otaliastudios.transcoder.strategy.DefaultVideoStrategy
import com.padedatingapp.R
import com.padedatingapp.utils.ImageCompressor
import com.padedatingapp.utils.LocaleHelper
import com.padedatingapp.utils.getFormattedCountDownTimer
import kotlinx.android.synthetic.main.activity_custom_camera_view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class CustomCameraView : AppCompatActivity() {

    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_camera_view)

        initCamera()
        setUpListeners()
    }

    private fun setUpListeners() {
        tvTimer.text = "00:00:00"

        intent?.extras?.let {
            if (intent?.extras?.containsKey("isProfilePicture")!!) {
                if (it.getBoolean("isProfilePicture", false)) {
                    ivVideoMode.isVisible = false
                }
            }
        }

        ivVideoMode.setOnClickListener {
            cameraView.mode = Mode.VIDEO
            rlVideoMode.isVisible = true
            rlPictureMode.isVisible = false
        }
        ivPictureMode.setOnClickListener {
            cameraView.mode = Mode.PICTURE
            rlPictureMode.isVisible = true
            rlVideoMode.isVisible = false
        }

        ivCapture.setOnClickListener {
            capturePicture()
        }

        ivRecordVideo.setOnClickListener {
            captureVideo()
        }

        ivRecordStopRec.setOnClickListener {
            cameraView.stopVideo()
            timer.cancel()
        }

        timer = object : CountDownTimer(31000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val hms = getFormattedCountDownTimer(millisUntilFinished)
                tvTimer.text = hms
            }

            override fun onFinish() {
                cameraView.stopVideo()
            }
        }

    }

    private fun initCamera() {

        cameraView.setLifecycleOwner(this)

        cameraView.addCameraListener(object : CameraListener() {

            override fun onPictureShutter() {
                // Picture capture started!
            }

            override fun onPictureTaken(@NonNull pictureResult: PictureResult) {
                // A Picture was taken!
                val extension = when (pictureResult!!.format) {
                    PictureFormat.JPEG -> "jpg"
                    PictureFormat.DNG -> "dng"
                    else -> throw RuntimeException("Unknown format.")
                }
                val file = File(filesDir, "${System.currentTimeMillis()}_picture.$extension")
                val thumbFile = File(filesDir, file.nameWithoutExtension+ "_thumb.$extension")
                GlobalScope.launch {
                    CameraUtils.writeToFile(pictureResult.data, file)
                }
                pictureResult.toFile(file) {
                    it?.let {
                        val savedUri = Uri.fromFile(it)
                        val thumbUri = Uri.fromFile(thumbFile)
                        //   val msg = "Photo capture succeeded: $savedUri"

                        val os: OutputStream = BufferedOutputStream(FileOutputStream(thumbFile))
                        val thumb = ThumbnailUtils.createImageThumbnail(
                            it.path,
                            MediaStore.Video.Thumbnails.MICRO_KIND
                        )
                        thumb?.compress(Bitmap.CompressFormat.JPEG, 50, os)
                        os.close()

                        val intent = Intent()
                        intent.putExtra("data", savedUri)
                        intent.putExtra("thumb", thumbUri)
                        intent.putExtra("type", "image")
                        intent.putExtra(Intent.EXTRA_STREAM, savedUri)
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                }
            }

            override fun onVideoTaken(@NonNull result: VideoResult) {
                rlPictureMode.isVisible = false
                rlVideoMode.isVisible = false
                pbSavingVideo.visibility = View.VISIBLE

                var strategy = DefaultVideoStrategy.fraction(0.5.toFloat()).build()

                // A Video was taken!
                result.file.let {
                    val savedUri = Uri.fromFile(it)
                    val destFile = File(filesDir, "${System.currentTimeMillis()}_video_compressed.mp4")

                    Transcoder.into(destFile.path)
                        .addDataSource(it.path)
                        .setVideoTrackStrategy(strategy)
                        .setListener(object :TranscoderListener {
                            override fun onTranscodeCanceled() {
                                Log.d("VideoCompressor","Compressing CANCELLED ")
                            }

                            override fun onTranscodeCompleted(successCode: Int) {

                                rlVideoMode.isVisible = true
                                pbSavingVideo.visibility = View.GONE

                                val thumbFile = File(filesDir, it.nameWithoutExtension+ "_thumb.jpg")
                                val thumbUri = Uri.fromFile(thumbFile)
                                val fOut = FileOutputStream(thumbFile)
                                val thumb =ImageCompressor.getThumbVideo(this@CustomCameraView,savedUri)
                                thumb?.compress(Bitmap.CompressFormat.JPEG,60,fOut)
                                fOut.flush() // Not really required
                                fOut.close() // do not forget to close the stream
                                thumb?.recycle()

                                val intent = Intent()
                                intent.putExtra("data", Uri.fromFile(destFile))
                                intent.putExtra("type", "video")
                                intent.putExtra("thumb", thumbUri)
                                intent.putExtra(Intent.EXTRA_STREAM,  Uri.fromFile(destFile))
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                setResult(Activity.RESULT_OK, intent)
                                finish()
                            }

                            override fun onTranscodeFailed(exception: Throwable) {
                                Log.d("VideoCompressor","Compressing failed because  " +exception.message)


                            }

                            override fun onTranscodeProgress(progress: Double) {

                            }
                        }).transcode()
                }
            }

            override fun onVideoRecordingStart() {
                // Notifies that the actual video recording has started.
                // Can be used to show some UI indicator for video recording or counting time.
                timer.start()
                ivRecordStopRec.isVisible = true
                ivRecordVideo.isVisible = false
                tvStart.isVisible = false
                ivPictureMode.isVisible = false

            }

            override fun onVideoRecordingEnd() {
                // Notifies that the actual video recording has ended.
                // Can be used to remove UI indicators added in onVideoRecordingStart.
                tvTimer.text = "00:00:00"
                ivRecordStopRec.isVisible = false
                ivRecordVideo.isVisible = true
                tvStart.isVisible = true
                ivPictureMode.isVisible = true
            }
        })
    }


    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    private fun capturePicture() {
        if (cameraView.mode == Mode.VIDEO) return run {
            //  message("Can't take HQ pictures while in VIDEO mode.", false)
        }
        if (cameraView.isTakingPicture) return
        /*captureTime = System.currentTimeMillis()
        message("Capturing picture...", false)*/
        cameraView.takePicture()
    }

    private fun captureVideo() {
        if (cameraView.isTakingVideo) return run {
            //message("Already taking video.", false)
        }
        if (cameraView.preview != Preview.GL_SURFACE) return run {
            //  message("Video snapshots are only allowed with the GL_SURFACE preview.", true)
        }
        // message("Recording snapshot for 5 seconds...", true)
        cameraView.takeVideo(
            File(filesDir, "pade_app_${System.currentTimeMillis()}_video.mp4"),
            30000
        )
    }


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
}