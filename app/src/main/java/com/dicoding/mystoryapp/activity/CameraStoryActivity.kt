package com.dicoding.mystoryapp.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.dicoding.mystoryapp.R
import com.dicoding.mystoryapp.databinding.ActivityCameraStoryBinding
import com.dicoding.mystoryapp.fragment.StoryFragment.Companion.CAMERA
import com.dicoding.mystoryapp.util.createFile
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraStoryBinding
    private var capture: ImageCapture? = null
    private lateinit var camera : ExecutorService
    private var selector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tookImg.setOnClickListener {
            take()
        }
        camera = Executors.newSingleThreadExecutor()
        binding.switchCam.setOnClickListener {
            selector =
                if (selector == CameraSelector.DEFAULT_BACK_CAMERA)
                    CameraSelector.DEFAULT_FRONT_CAMERA
                else
                    CameraSelector.DEFAULT_BACK_CAMERA
            val cameraProv = ProcessCameraProvider.getInstance(this)
            cameraProv.addListener({
                val camProvider: ProcessCameraProvider = cameraProv.get()
                val previewPhoto = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(binding.vCamera.surfaceProvider)
                    }
                capture = ImageCapture.Builder().build()
                try {
                    camProvider.unbindAll()
                    camProvider.bindToLifecycle(
                        this, selector, previewPhoto, capture
                    )
                } catch (e: Exception){
                    Toast.makeText(this, R.string.gagal_ambil_gambar, Toast.LENGTH_SHORT).show()
                }
            }, ContextCompat.getMainExecutor(this))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        camera.shutdown()
    }

    private fun take(){
        val capture = capture ?: return
        val photoFile = createFile(application)
        val imgOutput = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        capture.takePicture(
            imgOutput,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val intent = Intent()
                    intent.putExtra("picture", photoFile)
                    intent.putExtra(
                        "isBackCamera",
                        selector == CameraSelector.DEFAULT_BACK_CAMERA
                    )
                    setResult(CAMERA, intent)
                    finish()
                }
                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(this@CameraStoryActivity, R.string.error, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
        val cameraProv = ProcessCameraProvider.getInstance(this)
        cameraProv.addListener({
            val camProvider: ProcessCameraProvider = cameraProv.get()
            val previewPhoto = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.vCamera.surfaceProvider)
                }
            capture = ImageCapture.Builder().build()
            try {
                camProvider.unbindAll()
                camProvider.bindToLifecycle(
                    this, selector, previewPhoto, capture
                )
            } catch (e: Exception){
                Toast.makeText(this, R.string.gagal_ambil_gambar, Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }
}