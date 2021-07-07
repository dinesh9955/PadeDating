package com.padedatingapp.ui.main.fragments.edit_profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.birimo.birimosports.utils.SharedPref
import com.bumptech.glide.Glide
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.padedatingapp.R
import com.padedatingapp.adapter.UploadImageAdapter
import com.padedatingapp.api.Resource
import com.padedatingapp.api.ResponseStatus
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.custom_views.CustomProgressDialog
import com.padedatingapp.databinding.FragmentEditProfileBinding
import com.padedatingapp.model.ImageModel
import com.padedatingapp.model.ImageUploadResponse
import com.padedatingapp.model.Result
import com.padedatingapp.model.UserModel
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
import org.koin.android.ext.android.inject
import java.io.File
import java.util.*

class EditProfileFragment : DataBindingFragment<FragmentEditProfileBinding>(),
    UploadImageAdapter.OnItemClickListener {
    private val sharedPref by inject<SharedPref>()
    private var progressDialog: CustomProgressDialog? = null
    private val editProfileVM by inject<EditProfileVM>()
    var isProfileClick = false
    override fun layoutId(): Int = R.layout.fragment_edit_profile

    companion object {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        viewBinding.vm = editProfileVM
        viewBinding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(requireContext())

        initComponents()
        initObservables()
        setUserData()
    }

    private fun initObservables() {

        editProfileVM._errorMessage.observe(viewLifecycleOwner) {
            if (it != "") toast(it)
        }

        editProfileVM.uploadFileResponse.observe(viewLifecycleOwner) {
            getLiveData(it, "uploadFileResponse")
        }

        editProfileVM.loginResponse.observe(viewLifecycleOwner) {
            getLiveData(it, "setupProfileResponse")
        }

    }

    private fun getLiveData(response: Resource<Result<*>>?, type: String) {
        when (response?.status) {
            Resource.Status.LOADING -> {
                progressDialog?.show()
            }
            Resource.Status.SUCCESS -> {
                progressDialog?.dismiss()

                when (type) {
                    "setupProfileResponse" -> {
                        val data = response.data as Result<UserModel>
                        onSetupProfileResponse(data)
                    }

                    "uploadFileResponse" -> {
                        val data = response.data as Result<ImageUploadResponse>
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

    private fun onUploadFileResponse(data: Result<ImageUploadResponse>) {
        data?.let {
            if (it.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && it.success) {
                if (isProfileClick) {
                    editProfileVM.profilePicUrl = it.data!!.source
                    Glide.with(this).load(editProfileVM.profilePicUrl)
                        .into(viewBinding.ivPorfilePic)

                } else {

                    when (editProfileVM.list.size == 10) {
                        true -> {
                            editProfileVM.list.removeAt(editProfileVM.list.size - 1)
                            editProfileVM.list.add(
                                editProfileVM.list.size - 1,
                                (ImageModel(it.data!!.source, it.data!!.type, it.data!!.thumb))
                            )
                        }
                        false -> {
                            editProfileVM.list.add(
                                editProfileVM.list.size - 1,
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

    private fun setAdapter() {
        val adapter = UploadImageAdapter(this)
        adapter.submitList(editProfileVM.list)
        adapter.notifyDataSetChanged()
        viewBinding.rvPhotos.adapter = adapter
        viewBinding.rvPhotos.layoutManager = GridLayoutManager(requireContext(), 4)
    }

    private fun onSetupProfileResponse(data: Result<UserModel>) {
        data?.let {
            if (data.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && data.success) {
                sharedPref.setString(AppConstants.USER_TOKEN, it.data?.accessToken!!)

                sharedPref.setString(AppConstants.USER_ID, it.data._id)
                sharedPref.setString(AppConstants.USER_OBJECT, Gson().toJson(it.data))
                findNavController().popBackStack()
            } else {
                toast(data.message)
            }
        }
    }


    private fun initComponents() {
        viewBinding.header.tvTitle.text = getString(R.string.edit_information)

        editProfileVM.token = sharedPref.getString(AppConstants.USER_TOKEN)
        viewBinding.header.ivBack.setOnClickListener {
            requireActivity().hideKeyboard()
            findNavController().popBackStack()
        }
        viewBinding.ivCamera.setOnClickListener {
            isProfileClick = true
            showOptions()
        }
        viewBinding.tvGender.setOnClickListener {
            requireActivity().hideKeyboard()
            showDropDownDialog()
        }

        viewBinding.tvAddress.setOnClickListener {
            requireActivity().hideKeyboard()
            showAddressOverlay()
        }

        /* viewBinding.btnSave.setOnClickListener {
             findNavController().popBackStack()
         }*/

        viewBinding.clTop.setOnClickListener {
            requireActivity().hideKeyboard()
        }
    }

    override fun onItemClick(model: ImageModel, actionType: String) {
        when (actionType) {
            "add" -> {
                Log.e("JDJFCV", "onItemClick: ")
                isProfileClick = false
                showOptions()
            }
            "remove" -> {
                editProfileVM.list.remove(model)
                setAdapter()
            }

            "show_in_video_player" -> {
                startActivity(Intent(requireActivity(),PlayerActivity::class.java).putExtra("video_url",model.source))
            }
        }
    }

    private fun showAddressOverlay() {
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        val intent = object : Autocomplete.IntentBuilder(
            AutocompleteActivityMode.FULLSCREEN, fields
        ) {}
            .setHint("Address")
            .build(requireContext())
        startActivityForResult(intent, SELECT_ADDRESS_REQUEST_CODE)
    }


    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()

    }

    private fun setUserData() {
        try {
            editProfileVM.list.clear()
            val userObject =
                Gson().fromJson(
                    sharedPref.getString(AppConstants.USER_OBJECT),
                    UserModel::class.java
                )
            Glide.with(requireContext()).load(userObject.image)
                .placeholder(R.drawable.user_place_holder).into(viewBinding.ivPorfilePic)

            viewBinding.tvName.text = userObject.firstName + " " + userObject.lastName
            viewBinding.etFirstName.setText(userObject.firstName)
            viewBinding.etLastName.setText(userObject.lastName)
            viewBinding.etPhone.setText(userObject.phoneNo)
            viewBinding.etEmail.setText(userObject.email)
            editProfileVM.gender.value = userObject.gender.substring(0, 1).toUpperCase() + userObject.gender.substring(1).toLowerCase()
            viewBinding.tvAddress.setText(userObject.address)

            editProfileVM.firstName.value = userObject.firstName
            editProfileVM.lastName.value = userObject.lastName
            editProfileVM.address.value = userObject.address
            editProfileVM.profilePicUrl = userObject.image
            editProfileVM.city.value = userObject.city
            editProfileVM.state.value = userObject.state
            editProfileVM.country.value = userObject.country
            editProfileVM.latitude.value = userObject.latitude.toDouble()
            editProfileVM.longitude.value = userObject.longitude.toDouble()
            viewBinding.ccp.setCountryForPhoneCode(userObject.countryCode.replace("+", "").toInt())
            for (doc in userObject.docImage) {
                editProfileVM.list.add(ImageModel(doc.source, doc.type, doc.thumb))
            }
            if (editProfileVM.list.size < 10) {
                editProfileVM.list.add(ImageModel("add", "image", ""))
            }
            setAdapter()

        } catch (e: JsonParseException) {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAPTURE_IMAGE_REQUEST -> {
                    viewBinding.ivPorfilePic.scaleType = ImageView.ScaleType.CENTER_CROP
                    val type = data?.extras?.getString("type", "image") ?: "image"
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
                                        editProfileVM.callUploadFile(
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
                                        editProfileVM.callUploadFile(
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
                                editProfileVM.callUploadFile(
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
                SELECT_ADDRESS_REQUEST_CODE -> {
                    try {
                        val place = Autocomplete.getPlaceFromIntent(data!!)
                        try {
                            val addresses: List<Address>
                            val geocoder = Geocoder(requireContext(), Locale.getDefault())
                            addresses = geocoder.getFromLocation(
                                place.latLng?.latitude!!,
                                place.latLng?.longitude!!,
                                1
                            )
                            // val address: String = addresses[0].getAddressLine(0)
                            editProfileVM.latitude.value = addresses[0].latitude
                            editProfileVM.longitude.value = addresses[0].longitude
                            if (addresses[0].locality != null) {
                                editProfileVM.city.value = addresses[0].locality ?: ""
                            } else {
                                editProfileVM.city.value = ""
                            }
                            editProfileVM.state.value = addresses[0].adminArea ?: ""
                            editProfileVM.country.value = addresses[0].countryName ?: ""
                            var str = addresses[0].locality + ", " + addresses[0].countryName
                            //editProfileVM.cityAndCountry.value = str
                            editProfileVM.address.value = addresses[0].getAddressLine(0)
                            viewBinding.tvAddress.text = addresses[0].getAddressLine(0)
                            requireActivity().hideKeyboard()
                        } catch (e: Exception) {
                            Log.e("CreateAccountFragment", "GeoCoder Exception: $e")
                        }
                    } catch (e: Exception) {
                    }
                }
            }
        }
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

    private fun showDropDownDialog() {
        val list = resources.getStringArray(R.array.gender_array)
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.gender))
            .setItems(list) { _, which ->
                viewBinding.tvGender.text = list[which]
                editProfileVM.gender.value = list[which]
            }.show()
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