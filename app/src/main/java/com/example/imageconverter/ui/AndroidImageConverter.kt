package com.example.imageconverter.ui

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.imageconverter.mvp.model.IImageConverter
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.FileOutputStream


class AndroidImageConverter(private val contentResolver: ContentResolver) : IImageConverter {

    companion object {
        val READ_ONLY_MOE = "r"
        val WRITE_MODE = "w"
    }

    private var bitmap: Bitmap? = null

    // конвертирует jpg в png
    override fun convertImage(uri: String?): Flowable<Int> {
        return Flowable.fromCallable<Int>() {
            val parcelFileDescriptor =
                contentResolver.openFileDescriptor(Uri.parse(uri), WRITE_MODE)
            parcelFileDescriptor?.let {
                Thread.sleep(2000)
                val fos = FileOutputStream(it.fileDescriptor)
                this.bitmap?.compress(Bitmap.CompressFormat.PNG, 10, fos)
                fos.close()
                it.close()
            }
            100
        }.subscribeOn(Schedulers.io()).onBackpressureLatest().concatWith(simulateProgress())
    }

    // отображает изображение во вьюхе
    override fun getBitmapFromUri(uri: String?): Single<Bitmap> {
        return Single.create<Bitmap> { emitter ->
            try {
                val parcelFileDescriptor =
                    contentResolver.openFileDescriptor(Uri.parse(uri), READ_ONLY_MOE)
                val bitmap =
                    BitmapFactory.decodeFileDescriptor(parcelFileDescriptor?.fileDescriptor)
                this.bitmap = bitmap;
                emitter.onSuccess(bitmap)
                parcelFileDescriptor?.close()
            } catch (e: Error) {
                emitter.onError(e)
            }
        }.subscribeOn(Schedulers.io())
    }

    override fun simulateProgress(): Flowable<Int> {
        return Flowable.range(1, 1000_000_0).subscribeOn(Schedulers.computation())
            .onBackpressureLatest()
    }
}
