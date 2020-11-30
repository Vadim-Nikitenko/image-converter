package com.example.imageconverter.mvp.model

import android.graphics.Bitmap
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface IImageConverter {
    fun convertImage(uri: String?): Flowable<Int>
    fun getBitmapFromUri(uri: String?): Single<Bitmap>
    fun simulateProgress(): Flowable<Int>
}