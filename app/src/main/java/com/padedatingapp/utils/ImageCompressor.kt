package com.padedatingapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.core.net.toUri
import com.padedatingapp.CustomCameraActivity
import com.padedatingapp.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.Throws
import kotlin.math.min


object ImageCompressor {
    /**
     * This doesn't compress the original image file.
     * It compresses the bitmap and updates it to the new file and returns from app cache
     */
    @Throws(Exception::class)
    fun compressBitmap(context: Context, originalImageFile: File, cb: ((File) -> Unit)? = null) {
        var bitmap = updateDecodeBounds(originalImageFile)
        val file  = File(
            getOutputDirectory(context),SimpleDateFormat(
            CustomCameraActivity.FILENAME_FORMAT, Locale.US
        ).format(System.currentTimeMillis()) + ".jpg")
        val fOut = FileOutputStream(file)
        bitmap = rotateImageIfRequired(bitmap, originalImageFile.toUri())!!
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, fOut)
        fOut.flush() // Not really required
        fOut.close() // do not forget to close the stream
        bitmap.recycle() //recycle the bitmap
        cb?.invoke(file)
    }

    @Throws(Exception::class)
    fun compressThumbBitmap(context: Context, originalImageFile: File, cb: ((File) -> Unit)? = null) {
        var bitmap = updateThumbDecodeBounds(originalImageFile)
        val file  = File(
            getOutputDirectory(context),SimpleDateFormat(
                CustomCameraActivity.FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg")
        val fOut = FileOutputStream(file)
        bitmap = rotateImageIfRequired(bitmap, originalImageFile.toUri())!!
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, fOut)
        fOut.flush() // Not really required
        fOut.close() // do not forget to close the stream
        bitmap.recycle() //recycle the bitmap
        cb?.invoke(file)
    }

    /**
     * This compress the original file.
     */
    @Throws(Exception::class)
    fun compressCurrentBitmapFile(originalImageFile: File) {
        val bitmap = updateDecodeBounds(originalImageFile)
        val fOut = FileOutputStream(originalImageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fOut)
        fOut.flush() // Not really required
        fOut.close() // do not forget to close the stream
        bitmap.recycle() //recycle the bitmap
    }

    /**
     * Measure decodeBounds of the bitmap from given File.
     */
    private fun updateDecodeBounds(imageFile: File): Bitmap {
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(imageFile.absolutePath, this)
            val sampleHeight = 1280
            val sampleWidth = 720
            /**
             * You can tweak the sizes 900, 1100.
             * The bigger the number is, the more details you can keep.
             * The lesser, the lesser quality of details.
             */
            inSampleSize = min(outHeight / sampleHeight, outWidth / sampleWidth)
            inJustDecodeBounds = false
            BitmapFactory.decodeFile(imageFile.absolutePath, this)
        }
    }

    private fun updateThumbDecodeBounds(imageFile: File): Bitmap {
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(imageFile.absolutePath, this)
            val sampleHeight = 256
            val sampleWidth = 144
            /**
             * You can tweak the sizes 900, 1100.
             * The bigger the number is, the more details you can keep.
             * The lesser, the lesser quality of details.
             */
            inSampleSize = min(outHeight / sampleHeight, outWidth / sampleWidth)
            inJustDecodeBounds = false
            BitmapFactory.decodeFile(imageFile.absolutePath, this)
        }
    }


    @Throws(IOException::class)
    private fun rotateImageIfRequired(
        img: Bitmap,
        selectedImage: Uri
    ): Bitmap? {
        val ei =
            ExifInterface(selectedImage.path!!)
        val orientation: Int = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(
                img,
                90f
            )
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(
                img,
                180f
            )
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(
                img,
                270f
            )
            else -> img
        }
    }
    fun getOutputDirectory(ctx:Context): File {
        val mediaDir = ctx.externalMediaDirs.firstOrNull()?.let {
            File(it, ctx.getString(R.string.app_name)).apply { mkdirs() } }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else ctx.filesDir
    }


    private fun rotateImage(
        img: Bitmap,
        degree: Float
    ): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degree)
        val rotatedImg = Bitmap.createBitmap(
            img,
            0,
            0,
            img.width,
            img.height,
            matrix,
            true
        )
        img.recycle()
        return rotatedImg
    }

    fun getThumbVideo(context: Context?, videoUri: Uri?): Bitmap? {
        var bitmap: Bitmap? = null
        var mediaMetadataRetriever: MediaMetadataRetriever? = null
        try {
            mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(context, videoUri)
            bitmap = mediaMetadataRetriever.getFrameAtTime(
                3000,
                MediaMetadataRetriever.OPTION_CLOSEST_SYNC
            )
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            mediaMetadataRetriever?.release()
        }
        return bitmap
    }

}