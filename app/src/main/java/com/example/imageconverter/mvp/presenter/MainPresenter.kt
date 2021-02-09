package com.example.imageconverter.mvp.presenter

import com.example.imageconverter.mvp.model.IImageConverter
import com.example.imageconverter.mvp.view.MainView
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import moxy.MvpPresenter

class MainPresenter(private val imageConverter: IImageConverter, private val scheduler: Scheduler) :
    MvpPresenter<MainView>() {

    private val compositeDisposable = CompositeDisposable()

    fun pickImage() {
        viewState.pickImage()
    }

    fun pickImageToConvert() {
        viewState.convertImage()
    }

    // отобразить изображение из пикера во вьюхе
    fun showImage(uri: String?) {
        imageConverter.getBitmapFromUri(uri)
            .observeOn(scheduler)
            .subscribe({
                viewState.setImage(it)
            }, {
                it.printStackTrace()
            })
            .addTo(compositeDisposable)
    }

    // ковертировать изображение из jpg в png
    fun convertImage(uri: String?) {
        imageConverter.simulateProgress()
            .observeOn(scheduler, false, 1)
            .subscribe({
                viewState.showProgress(it / 100000, "${it / 100000}%")
            }, {
                it.printStackTrace()
            })
            .addTo(compositeDisposable)

        imageConverter.convertImage(uri)
            .observeOn(scheduler)
            .subscribe({
                viewState.showImageConvertedTitle()
                viewState.hideProgress()
            }, {
                it.printStackTrace()
            })
            .addTo(compositeDisposable)
    }

    fun cancelConvertation() {
        compositeDisposable.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

}