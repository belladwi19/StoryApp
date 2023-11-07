package com.dicoding.mystoryapp.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dicoding.mystoryapp.activity.CameraStoryActivity
import com.dicoding.mystoryapp.activity.MainActivity
import com.dicoding.mystoryapp.databinding.FragmentStoryBinding
import com.dicoding.mystoryapp.util.reduceFileImage
import com.dicoding.mystoryapp.util.rotateBitmap
import com.dicoding.mystoryapp.util.uriToFile
import com.dicoding.mystoryapp.viewModel.AuthModel
import com.dicoding.mystoryapp.viewModel.FactoryModel
import com.dicoding.mystoryapp.viewModel.StoryModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class StoryFragment : Fragment() {
    private lateinit var binding: FragmentStoryBinding
    private lateinit var result: Bitmap
    private var getImage: File? = null
    private lateinit var factoryModel: FactoryModel
    private val storyModel: StoryModel by activityViewModels { factoryModel }
    private val authModel: AuthModel by activityViewModels {factoryModel}

    private fun add(description: String){
        if (getImage != null){
            val reqDescription = description.toRequestBody("text/plain".toMediaType())
            val file = reduceFileImage(getImage as File)
            val reqImage = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData("photo", file.name, reqImage)
            authModel.getToken().observe(viewLifecycleOwner) { token ->
                storyModel.add(imageMultipart, reqDescription, token)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factoryModel = FactoryModel.getInstance(requireActivity())

        binding.btnUpload.setOnClickListener {
            val description = binding.tvDescription.text.toString()
            if (description.isNotEmpty()){
                add(description)
            } else {
                Toast.makeText(activity, "Wajib Tambahkan Deskripsi", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnTookGalery.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            val pilih = Intent.createChooser(intent, "Choose a Picture")
            gallery.launch(pilih)
        }

        binding.btnTookCamera.setOnClickListener {
            val intent = Intent(activity, CameraStoryActivity::class.java)
            camera.launch(intent)
        }

        storyModel.error.observe(viewLifecycleOwner){e ->
            e.getContentIfNotHandled()?.let { error ->
                if (!error){
                    Toast.makeText(activity, "Sukses Upload", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(activity, MainActivity::class.java))
                    activity?.finish()
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentStoryBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    private val gallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK){
            val img: Uri = result.data?.data as Uri
            val myFile = uriToFile(img, requireActivity())
            getImage = myFile
            binding.btnUpload.isEnabled = true
            binding.previewImage.setImageURI(img)
        }
    }

    private val camera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == CAMERA){
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBack = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            getImage = myFile
            result = rotateBitmap(BitmapFactory.decodeFile(myFile.path), isBack)
            binding.btnUpload.isEnabled = true
            binding.previewImage.setImageBitmap(result)
        }
    }

    companion object{
        const val CAMERA = 200
    }

}