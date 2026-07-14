package br.com.fiap.biomonitor.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val imagesDir: File by lazy {
        File(context.filesDir, "sighting_images").apply {
            if (!exists()) mkdirs()
        }
    }


    suspend fun saveImage(
        uri: Uri,
        quality: Int = Constants.IMAGE_COMPRESSION_QUALITY
    ): String? = withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
                ?: return@withContext null

            val bitmap = decodeSampledBitmap(inputStream, uri)
                ?: return@withContext null

            val rotatedBitmap = correctImageOrientation(uri, bitmap)
            val fileName = "${UUID.randomUUID()}.jpg"
            val file = File(imagesDir, fileName)

            FileOutputStream(file).use { outputStream ->
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            }

            if (rotatedBitmap != bitmap) {
                rotatedBitmap.recycle()
            }
            bitmap.recycle()

            file.absolutePath
        } catch (e: Exception) {
            null
        }
    }


    suspend fun loadImage(path: String): Bitmap? = withContext(Dispatchers.IO) {
        try {
            val file = File(path)
            if (!file.exists()) return@withContext null

            BitmapFactory.decodeFile(path)
        } catch (e: Exception) {
            null
        }
    }


    suspend fun loadThumbnail(
        path: String,
        targetWidth: Int = 200,
        targetHeight: Int = 200
    ): Bitmap? = withContext(Dispatchers.IO) {
        try {
            val file = File(path)
            if (!file.exists()) return@withContext null

            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeFile(path, options)

            options.inSampleSize = calculateInSampleSize(options, targetWidth, targetHeight)
            options.inJustDecodeBounds = false

            BitmapFactory.decodeFile(path, options)
        } catch (e: Exception) {
            null
        }
    }


    suspend fun deleteImage(path: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val file = File(path)
            file.exists() && file.delete()
        } catch (e: Exception) {
            false
        }
    }


    suspend fun compressImage(
        path: String,
        quality: Int = Constants.IMAGE_COMPRESSION_QUALITY
    ): String? = withContext(Dispatchers.IO) {
        try {
            val bitmap = BitmapFactory.decodeFile(path) ?: return@withContext null

            val scaledBitmap = if (bitmap.width > Constants.MAX_IMAGE_DIMENSION ||
                                   bitmap.height > Constants.MAX_IMAGE_DIMENSION) {
                scaleBitmap(bitmap, Constants.MAX_IMAGE_DIMENSION)
            } else {
                bitmap
            }

            val fileName = "${UUID.randomUUID()}.jpg"
            val file = File(imagesDir, fileName)

            FileOutputStream(file).use { outputStream ->
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            }

            if (scaledBitmap != bitmap) {
                scaledBitmap.recycle()
            }
            bitmap.recycle()

            file.absolutePath
        } catch (e: Exception) {
            null
        }
    }


    fun getImageSize(path: String): Long {
        val file = File(path)
        return if (file.exists()) file.length() else -1
    }


    fun imageExists(path: String): Boolean {
        return File(path).exists()
    }


    private fun decodeSampledBitmap(inputStream: InputStream, uri: Uri): Bitmap? {
        return try {

            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }


            val newStream = context.contentResolver.openInputStream(uri)
                ?: return null

            BitmapFactory.decodeStream(newStream, null, options)
            newStream.close()


            options.inSampleSize = calculateInSampleSize(
                options,
                Constants.MAX_IMAGE_DIMENSION,
                Constants.MAX_IMAGE_DIMENSION
            )
            options.inJustDecodeBounds = false


            val finalStream = context.contentResolver.openInputStream(uri)
                ?: return null
            val bitmap = BitmapFactory.decodeStream(finalStream, null, options)
            finalStream.close()

            bitmap
        } catch (e: Exception) {
            null
        }
    }


    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight &&
                   halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }


    private fun correctImageOrientation(uri: Uri, bitmap: Bitmap): Bitmap {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
                ?: return bitmap

            val exif = ExifInterface(inputStream)
            inputStream.close()

            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )

            val matrix = Matrix()
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.preScale(-1f, 1f)
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.preScale(1f, -1f)
                else -> return bitmap
            }

            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } catch (e: IOException) {
            bitmap
        }
    }


    private fun scaleBitmap(bitmap: Bitmap, maxDimension: Int): Bitmap {
        val ratio = minOf(
            maxDimension.toFloat() / bitmap.width,
            maxDimension.toFloat() / bitmap.height
        )

        if (ratio >= 1f) return bitmap

        val newWidth = (bitmap.width * ratio).toInt()
        val newHeight = (bitmap.height * ratio).toInt()

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }
}
