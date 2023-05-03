package com.raj.sample.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.raj.sample.R
import com.raj.sample.databinding.ActivityMainBinding
import com.raj.sample.databinding.BottomSheetUploadingProgressBinding
import com.raj.sample.online.ProgressListener
import com.raj.sample.util.FileUtil
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    //private val viewModel: MainViewModel by viewModels()
    private val viewModel: UploadViewModel by viewModels()
    private var isChoose = true

    private lateinit var getContent: ActivityResultLauncher<String>
    private var selectedFile: File? = null
    lateinit var bindingUploadDialog: BottomSheetUploadingProgressBinding
    private lateinit var uploadDialog: BottomSheetDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindingUploadDialog = BottomSheetUploadingProgressBinding.inflate(layoutInflater)
        uploadDialog = BottomSheetDialog(this@MainActivity, R.style.BottomSheetDialog)
        uploadDialog.setContentView(bindingUploadDialog.root)
        uploadDialog.setCancelable(false)

        initLaunchers()

        val progressListener = object : ProgressListener {
            override fun onProgress(progress: Int) {
                runOnUiThread {

                    if (!uploadDialog.isShowing) {
                        uploadDialog.show()
                    }

                    Glide
                        .with(this@MainActivity)
                        .load(selectedFile)
                        .centerCrop()
                        .placeholder(R.drawable.sample_image)
                        .into(bindingUploadDialog.uploadingImagePreview)

                    bindingUploadDialog.progressBar.progress = progress
                    bindingUploadDialog.percentage.text = "$progress%"
                    if (progress == 100) {

                        Handler(Looper.getMainLooper()).postDelayed({
                            Toast.makeText(
                                this@MainActivity,
                                "Image upload successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            if (uploadDialog.isShowing) {
                                uploadDialog.dismiss()
                            }
                        }, 1000)
                    }
                }
            }
        }

        binding.upload.setOnClickListener {
            if (isChoose) {
                getContent.launch("image/*")
            } else {
                selectedFile?.let {
                    viewModel.uploadFile(it.absolutePath, progressListener)
                    isChoose = true
                    binding.upload.text = "choose"
                }
            }
        }

    }

    /*    private fun subscribeUpload() = lifecycleScope.launchWhenStarted {
            viewModel.setupEvent.collect { event ->
                *//* when (event) {
                 "done" -> {
                     Toast.makeText(this@MainActivity, event, Toast.LENGTH_LONG).show()
                 }

                 "not_done" -> {



                     Toast.makeText(this@MainActivity, event, Toast.LENGTH_LONG).show()
                 }
             }*//*
        }
    }*/

    private fun initLaunchers() {
        getContent = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            if (uri != null) {
                binding.upload.text = "upload"
                isChoose = false
                selectedFile = FileUtil().getRealPathFromURI(this, uri)
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }

}