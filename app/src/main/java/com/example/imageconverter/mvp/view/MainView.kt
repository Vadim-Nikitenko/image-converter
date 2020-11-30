package com.example.imageconverter.mvp.view

import android.graphics.Bitmap
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface MainView: MvpView {
    companion object {
        val REQUEST_IMAGE_CODE = 123
        val WRITE_IMAGE_CODE = 321
    }
    fun pickImage()
    fun setImage(it: Bitmap)
    fun showProgress(i: Int, progress: String)
    fun convertImage()
    fun showImageConvertedTitle()
    fun hideProgress()
}