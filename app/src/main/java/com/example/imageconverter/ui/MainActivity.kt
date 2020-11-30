package com.example.imageconverter.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.imageconverter.R
import com.example.imageconverter.mvp.presenter.MainPresenter
import com.example.imageconverter.mvp.view.MainView
import com.example.imageconverter.mvp.view.MainView.Companion.REQUEST_IMAGE_CODE
import com.example.imageconverter.mvp.view.MainView.Companion.WRITE_IMAGE_CODE
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter

class MainActivity : MvpAppCompatActivity(), MainView {

    private val presenter by moxyPresenter {
        MainPresenter(AndroidImageConverter(contentResolver), AndroidSchedulers.mainThread());
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_show.setOnClickListener {
            presenter.pickImage()
        }

        btn_convert.setOnClickListener {
            presenter.pickImageToConvert()
        }

        cancel.setOnClickListener{
            presenter.cancelConvertation()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_IMAGE_CODE -> presenter.showImage(data.dataString)
                WRITE_IMAGE_CODE -> presenter.convertImage(data.dataString)
            }
        }
    }

    override fun pickImage() {
        val intent = Intent()
            .setType("image/jpeg")
            .addCategory(Intent.CATEGORY_OPENABLE)
            .setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_image_intent_title)), REQUEST_IMAGE_CODE)
        imageView.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progress.visibility = View.GONE
        progressbar.visibility = View.GONE
        cancel.visibility = View.GONE
        imageView.visibility = View.GONE
        btn_show.isEnabled = true
        btn_convert.isEnabled = false
        imageView.setImageBitmap(null)
    }

    override fun convertImage() {
        val intent: Intent = Intent()
            .setType("image/png")
            .addCategory(Intent.CATEGORY_OPENABLE)
            .setAction(Intent.ACTION_CREATE_DOCUMENT)
            .putExtra(Intent.EXTRA_TITLE, getString(R.string.pattern_png));
        startActivityForResult(Intent.createChooser(intent, getString(R.string.covert_image_intent_title)), WRITE_IMAGE_CODE)
    }

    override fun setImage(bitmap: Bitmap) {
        imageView.setImageBitmap(bitmap)
        btn_convert.isEnabled = true
        btn_show.isEnabled = false
    }

    override fun showProgress(i: Int, percentProgress: String) {
        progressbar.visibility = View.VISIBLE
        progress.visibility = View.VISIBLE
        cancel.visibility = View.VISIBLE
        progressbar.setProgress(i);
        progress.text = percentProgress
    }

    override fun showImageConvertedTitle() {
        Toast.makeText(this, getString(R.string.image_converted_toast), Toast.LENGTH_SHORT).show()
    }

}