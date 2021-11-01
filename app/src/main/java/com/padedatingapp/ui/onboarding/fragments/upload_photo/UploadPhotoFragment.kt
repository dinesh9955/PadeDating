package com.padedatingapp.ui.onboarding.fragments.upload_photo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.birimo.birimosports.utils.SharedPref
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.padedatingapp.R
import com.padedatingapp.adapter.UploadImageAdapter
import com.padedatingapp.api.Resource
import com.padedatingapp.api.ResponseStatus
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.custom_views.CustomProgressDialog
import com.padedatingapp.databinding.FragmentUploadPhotoBinding
import com.padedatingapp.model.*
import com.padedatingapp.ui.CustomCameraView
import com.padedatingapp.ui.PlayerActivity
import com.padedatingapp.utils.AppConstants
import com.padedatingapp.utils.ImageCompressor
import com.padedatingapp.utils.RealPathUtil
import com.padedatingapp.utils.hideKeyboard
import id.zelory.compressor.Compressor
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import org.koin.android.ext.android.inject
import java.io.File


class UploadPhotoFragment : DataBindingFragment<FragmentUploadPhotoBinding>(),
    UploadImageAdapter.OnItemClickListener {

    private val sharedPref by inject<SharedPref>()
    private var from = "sign_up"
    private var progressDialog: CustomProgressDialog? = null
    private val uploadPhotoVM by inject<UploadPhotoVM>()
    val list = ArrayList<ImageModel>()
    var isProfileClick = false

    companion object {
        var TAG = "UploadPhotoFragment"
        private val REQUIRED_GALLERY_PERMISSIONS =
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            )
        private const val CAPTURE_IMAGE_REQUEST = 1001
        private const val GALLERY_IMAGE_REQUEST = 1002
        private const val GALLERY_PERMISSION_REQUEST = 1004
        private const val SELECT_ADDRESS_REQUEST_CODE = 1003
    }

    override fun layoutId(): Int = R.layout.fragment_upload_photo

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.vm = uploadPhotoVM
        viewBinding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(requireContext())
        initComponents()
    }

    private fun initComponents() {
        from = arguments?.getString("from", "sign_up").toString()



        uploadPhotoVM.token = sharedPref.getString(AppConstants.USER_TOKEN, "en")

        Log.e(TAG, "USER_TOKEN11 "+sharedPref.getString(AppConstants.USER_TOKEN, "en"))

        viewBinding.header.tvTitle.text = getString(R.string.upload_photo)

        repeat(1) {
            list.add(ImageModel("add"))
        }
        initObservables()
        setAdapter()
        setupListeners()
    }

    private fun initObservables() {
        uploadPhotoVM.uploadFileResponse.observe(viewLifecycleOwner) {
            getLiveData(it, "uploadFileResponse")
        }

        uploadPhotoVM.loginResponse.observe(viewLifecycleOwner) {
            getLiveData(it, "setupProfileResponse")
        }
    }

    private fun setAdapter() {
        val adapter = UploadImageAdapter(this)
        adapter.submitList(list)
        adapter.notifyDataSetChanged()
        viewBinding.rvPhotos.adapter = adapter
        viewBinding.rvPhotos.layoutManager = GridLayoutManager(requireContext(), 4)
    }

    private fun setupListeners() {
        viewBinding.btnGetStarted.setOnClickListener {
            val directions =
                UploadPhotoFragmentDirections.actionToSignUpAboutFragment(
                    getString(R.string.sign_up)
                )
            findNavController().navigate(directions)
        }

        viewBinding.header.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        viewBinding.ivPorfilePic.setOnClickListener {
            isProfileClick = true
            showOptions()
        }

        viewBinding.ivChooseImage.setOnClickListener {
            isProfileClick = true
            showOptions()
        }

        viewBinding.tvAddPhoto.setOnClickListener {
            isProfileClick = true
            showOptions()
        }

        viewBinding.btnGetStarted.setOnClickListener {
            val jsonObj = JSONObject()
            val jsonArray = JSONArray()

            for (url in list) {
                if (url.source != "add") {
                    val obj = JSONObject()
                    obj.put("source", url.source)
                    obj.put("type", url.type)
                    obj.put("thumb", url.thumb)
                    jsonArray.put(obj)
                }
            }
            jsonObj.put("profileStatus", 2)
            jsonObj.put("image", uploadPhotoVM.profilePicUrl)
            jsonObj.put("docImage", jsonArray)

            Log.d("REGISTER_RQST_BODY_DATA", "Submit case for upload profile: $jsonObj")
            uploadPhotoVM.callUpdatePicApi(
                jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
            )
        }
    }

    private fun getLiveData(response: Resource<ResultModel<*>>?, type: String) {
        when (response?.status) {
            Resource.Status.LOADING -> {
                progressDialog?.show()
            }
            Resource.Status.SUCCESS -> {
                progressDialog?.dismiss()

                when (type) {
                    "setupProfileResponse" -> {
                        var data = response.data as ResultModel<UserModel>
                        onSetupProfileResponse(data)
                    }

                    "uploadFileResponse" -> {
                        var data = response.data as ResultModel<ImageUploadResponse>
                        onUploadFileResponse(data)
                    }
                }
            }
            Resource.Status.ERROR -> {
                progressDialog?.dismiss()
                toast(response.getErrorMessage().toString())
            }
            Resource.Status.CANCEL -> {
                progressDialog?.dismiss()
            }
        }
    }

    private fun onUploadFileResponse(data: ResultModel<ImageUploadResponse>) {
        data?.let {
            if (it.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && it.success) {
                if (isProfileClick) {
                    uploadPhotoVM.profilePicUrl = it.data!!.source
                    Glide.with(this).load(uploadPhotoVM.profilePicUrl)
                        .into(viewBinding.ivPorfilePic)

                } else {

                    when (list.size == 10) {
                        true -> {
                            list.removeAt(list.size - 1)
                            list.add(
                                list.size - 1,
                                (ImageModel(it.data!!.source, it.data!!.type, it.data!!.thumb))
                            )
                        }
                        false -> {
                            list.add(
                                list.size - 1,
                                (ImageModel(it.data!!.source, it.data!!.type, it.data!!.thumb))
                            )
                        }
                    }
                    setAdapter()
                }
            } else {
                toast(it.message)
            }
        }
    }

    private fun onSetupProfileResponse(data: ResultModel<UserModel>) {
        data?.let {
            if (data.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && data.success) {
                sharedPref.setString(AppConstants.USER_TOKEN, it.data?.accessToken!!)
                val directions =
                    UploadPhotoFragmentDirections.actionToSignUpAboutFragment(
                        getString(R.string.sign_up)
                    )
                findNavController().navigate(directions)
            } else {
                toast(data.message)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }

    private fun showOptions() = try {
        val dialogView = layoutInflater.inflate(R.layout.dialog_choose_image_option, null)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        val tvPhotos = dialogView.findViewById<TextView>(R.id.tvPhotos)
        val tvPhotosDesc = dialogView.findViewById<TextView>(R.id.tvPhotosDesc)
        val tvCamera = dialogView.findViewById<TextView>(R.id.tvCamera)
        val tvCameraDesc = dialogView.findViewById<TextView>(R.id.tvCameraDesc)
        val dialog = BottomSheetDialog(requireContext(), R.style.TransparentDialog)
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        tvCamera.setOnClickListener {
            dialog.dismiss()
            if (allPermissionsGranted())
                showCamera()
            else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    REQUIRED_GALLERY_PERMISSIONS,
                    GALLERY_PERMISSION_REQUEST
                )
            }
        }
        tvCameraDesc.setOnClickListener {
            dialog.dismiss()
            if (allPermissionsGranted())
                showCamera()
            else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    REQUIRED_GALLERY_PERMISSIONS,
                    GALLERY_PERMISSION_REQUEST
                )
            }
        }
        tvPhotos.setOnClickListener {
            dialog.dismiss()
            checkGalleryPermissions()
        }
        tvPhotosDesc.setOnClickListener {
            dialog.dismiss()
            checkGalleryPermissions()

        }
        dialog.setContentView(dialogView)
        dialog.show()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    override fun onItemClick(model: ImageModel, action: String) {

        when (action) {
            "add" -> {
                Log.e("JDJFCV", "onItemClick: ")
                isProfileClick = false
                showOptions()
            }
            "remove"->{
                list.remove(model)
                setAdapter()
            }
            "show_in_video_player" -> {
                startActivity(Intent(requireActivity(), PlayerActivity::class.java).putExtra("video_url",model.source))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAPTURE_IMAGE_REQUEST -> {
                    viewBinding.ivPorfilePic.scaleType = ImageView.ScaleType.CENTER_CROP
                    var type = data?.extras?.getString("type", "image") ?: "image"
                    if (type == "image") {
                        data?.getParcelableExtra<Uri>("data")?.let {
                            ImageCompressor.compressBitmap(requireContext(), it.toFile()) { file ->
                                file.let { f ->
                                    val requestBody: RequestBody =
                                        f.asRequestBody("image/*".toMediaTypeOrNull())
                                    val imageToUpload = MultipartBody.Part.createFormData(
                                        "source",
                                        f.name,
                                        requestBody
                                    )
                                    data?.getParcelableExtra<Uri>("thumb")?.let { thumbUri ->
                                        val thumbToUpload = MultipartBody.Part.createFormData(
                                            "thumb",
                                            thumbUri.toFile().name,
                                            requestBody
                                        )
                                        uploadPhotoVM.callUploadFile(
                                            imageToUpload, thumbToUpload,
                                            type.toRequestBody("text/plain".toMediaTypeOrNull())
                                        )

                                    }
                                }
                            }
                        }
                    } else {
                        data?.getParcelableExtra<Uri>("data")?.let {
                            it.toFile().let { f ->
                                val requestBody: RequestBody =
                                    f.asRequestBody("image/*".toMediaTypeOrNull())
                                val imageToUpload = MultipartBody.Part.createFormData(
                                    "source",
                                    f.name,
                                    requestBody
                                )
                                data?.getParcelableExtra<Uri>("thumb")?.let { thumbUri ->
                                    ImageCompressor.compressThumbBitmap(
                                        requireContext(),
                                        thumbUri.toFile()
                                    ) { thumbFile ->
                                        val thumbRqstBody: RequestBody =
                                            thumbFile.asRequestBody("image/*".toMediaTypeOrNull())
                                        val thumbToUpload = MultipartBody.Part.createFormData(
                                            "thumb",
                                            thumbFile.name,
                                            thumbRqstBody
                                        )
                                        uploadPhotoVM.callUploadFile(
                                            imageToUpload, thumbToUpload,
                                            type.toRequestBody("text/plain".toMediaTypeOrNull())
                                        )
                                    }


                                }

                            }
                        }
                    }
                }

                GALLERY_IMAGE_REQUEST -> {
                    viewBinding.ivPorfilePic.scaleType = ImageView.ScaleType.CENTER_CROP
                    val path = RealPathUtil.getRealPath(requireContext(), data?.data!!)
                    val imageFile = File(path)
                    data.data?.let {
                        lifecycleScope.launch {
                            val compressedImageFile =
                                Compressor.compress(requireContext(), imageFile)
                            /* Glide.with(requireContext()).apply {
                                 this.load(compressedImageFile).into(viewBinding.ivPorfilePic)
                             }*/
                            compressedImageFile.let { f ->
                                val requestBody: RequestBody =
                                    f.asRequestBody("image/*".toMediaTypeOrNull())
                                val imageToUpload =
                                    MultipartBody.Part.createFormData("source", f.name, requestBody)
                                val thumbToUpload =
                                    MultipartBody.Part.createFormData("thumb", f.name, requestBody)
                                uploadPhotoVM.callUploadFile(
                                    imageToUpload, thumbToUpload,
                                    "image".toRequestBody("text/plain".toMediaTypeOrNull())
                                )
                            }
                        }
                    }
                    /*
                        ImageCompressor.compressBitmap(requireActivity(), imageFile) { file ->
                           // setupProfileVM.imageFile = file
                            Glide.with(this).apply {
                                this.load(file).into(viewBinding.ivPorfilePic)
                            }
                        }
                    }*/
                }

            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == GALLERY_PERMISSION_REQUEST) {
            if (allPermissionsGranted()) {
                showGallery()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please grant Gallery Permissions to choose image from gallery",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_GALLERY_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }


    private fun checkGalleryPermissions() {
        if (allPermissionsGranted())
            showGallery()
        else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_GALLERY_PERMISSIONS,
                GALLERY_PERMISSION_REQUEST
            )
        }
    }

    private fun showCamera() {
        startActivityForResult(
            Intent(requireContext(), CustomCameraView::class.java).putExtra(
                "isProfilePicture",
                isProfileClick
            ),
            CAPTURE_IMAGE_REQUEST
        )
    }

    private fun showGallery() {
        val pickPhoto = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(
            pickPhoto,
            GALLERY_IMAGE_REQUEST
        )
    }
}