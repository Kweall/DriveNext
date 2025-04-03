package com.example.kweallapp
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.example.kweallapp.databinding.SignUp3Binding
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.TextUtils
import java.util.Calendar
import android.Manifest
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.kweallapp.viewmodel.SignUpViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val REQUEST_CODE_GALLERY = 100
private const val REQUEST_CODE_CAMERA = 200
private const val REQUEST_CODE_PHOTO_1 = 300
private const val REQUEST_CODE_PHOTO_2 = 400

class SignUp3Activity : BaseActivity() {

    private lateinit var binding: SignUp3Binding
    private lateinit var viewModel: SignUpViewModel
    private var isFormSubmitted = false
    private var isPhoto1Loaded = false
    private var isPhoto2Loaded = false
    private var avatarBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignUp3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Получаем доступ к базе данных через MyApp
        val myApp = application as MyApp
        val userDao = myApp.database.userDao()

//        viewModel = ViewModelProvider(
//            this,
//            SignUpViewModel.SignUpViewModelFactory(userDao)
//        )[SignUpViewModel::class.java]
        viewModel = SignUpViewModel.getInstance(userDao)

        binding.buttonContinue.isEnabled = false

        binding.imageView.setOnClickListener {
            startActivity(Intent(this, SignUp2Activity::class.java))
        }

        binding.buttonContinue.setOnClickListener {
            lifecycleScope.launch {
                isFormSubmitted = true
                if (validateForm()) {
                    saveDataToViewModel()
                    saveUserToDatabase()
                    startActivity(Intent(this@SignUp3Activity, CongratulationsActivity::class.java))
                }
            }
        }

        binding.imageView5.setOnClickListener {
            showDatePicker()
        }


        checkPermissions()

        setupAvatarSelection()

        setupAdditionalPhotoSelection()

        setupButton()
    }

    private fun setupAvatarSelection() {
        binding.imageView1.setOnClickListener {
            val options = arrayOf(getString(R.string.option_select_from_gallery), getString(R.string.option_take_photo))
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_select_image_source))
                .setItems(options) { _, which ->
                    when (which) {
                        0 -> openGalleryForPhoto(REQUEST_CODE_GALLERY)
                        1 -> openCamera(REQUEST_CODE_CAMERA)
                    }
                }
                .show()
        }
    }

    private fun setupAdditionalPhotoSelection() {
        binding.imageView6.setOnClickListener {
            openGalleryForPhoto(REQUEST_CODE_PHOTO_1)
        }

        binding.imageView7.setOnClickListener {
            openGalleryForPhoto(REQUEST_CODE_PHOTO_2)
        }
    }

    private fun openCamera(requestCode: Int) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, requestCode)
    }

    private fun openGalleryForPhoto(requestCode: Int) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_GALLERY -> {
                    val imageUri = data?.data
                    imageUri?.let { uri ->
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    loadBitmapIntoCircle(bitmap)
                    }
                }
                REQUEST_CODE_CAMERA -> {
                    val bitmap = data?.extras?.get("data") as? Bitmap
                    bitmap?.let {
                        loadBitmapIntoCircle(it)
                    }
                }
                REQUEST_CODE_PHOTO_1 -> {
                    val imageUri = data?.data
                    imageUri?.let {
                        updateFileNameText(it, binding.textView9)
                        isPhoto1Loaded = true
                        checkButtonState()
                    }
                }
                REQUEST_CODE_PHOTO_2 -> {
                    val imageUri = data?.data
                    imageUri?.let {
                        updateFileNameText(it, binding.textView10)
                        isPhoto2Loaded = true
                        checkButtonState()
                    }
                }
            }
        }
    }

    private fun checkButtonState() {
        lifecycleScope.launch {
            val isFormValid = validateForm()
            val arePhotosLoaded = isPhoto1Loaded && isPhoto2Loaded

            if (isFormValid && arePhotosLoaded) {
                binding.buttonContinue.isEnabled = true
            } else {
                binding.buttonContinue.isEnabled = false
            }
        }
    }

    private fun updateFileNameText(imageUri: Uri, textView: TextView) {
        val fileName = getFileNameFromUri(imageUri)
        fileName?.let {
            textView.text = it
        }
    }

    private fun getFileNameFromUri(uri: Uri): String? {
        return try {
            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (cursor.moveToFirst()) {
                    cursor.getString(nameIndex)
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getRoundedBitmap(bitmap: Bitmap): Bitmap {
        val size = bitmap.width.coerceAtMost(bitmap.height)

        val squareBitmap = Bitmap.createBitmap(
            bitmap,
            (bitmap.width - size) / 2,
            (bitmap.height - size) / 2,
            size,
            size
        )

        val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val paint = Paint()
        paint.isAntiAlias = true

        canvas.drawARGB(0, 0, 0, 0)

        val rect = RectF(0f, 0f, size.toFloat(), size.toFloat())
        canvas.drawOval(rect, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

        canvas.drawBitmap(squareBitmap, null, rect, paint)

        val borderPaint = Paint()
        borderPaint.color = android.graphics.Color.GRAY
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = 5f
        borderPaint.isAntiAlias = true

        val circleRadius = (size / 2).toFloat()
        val centerX = size / 2
        val centerY = size / 2
        canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), circleRadius - 5f / 2, borderPaint)

        return output
    }

    private fun loadBitmapIntoCircle(bitmap: Bitmap) {
        val roundedBitmap = getRoundedBitmap(bitmap)
        binding.imageView1.setImageBitmap(roundedBitmap)
    }

    private fun checkPermissions() {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    300
                )
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 300) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.permissions_for_camera),
                    Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private suspend fun validateForm(): Boolean {
        isFormSubmitted = true

        if (!validateDriverLicense()) {
            return false
        }


        val dateOfGetting = binding.editText3.text.toString()
        if (TextUtils.isEmpty(dateOfGetting) || !isValidDate(dateOfGetting)) {
            if (TextUtils.isEmpty(dateOfGetting)) {
                Toast.makeText(
                    this,
                    getString(R.string.error_enter_date_of_getting),
                    Toast.LENGTH_LONG
                ).show()
                return false
            } else{
                Toast.makeText(
                    this,
                    getString(R.string.error_enter_invalid_date),
                    Toast.LENGTH_LONG
                ).show()
                return false
            }
        }

        binding.buttonContinue.isEnabled = true

        if (!isPhoto1Loaded || !isPhoto2Loaded) {
            Toast.makeText(
                this,
                getString(R.string.error_upload_photo),
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        binding.buttonContinue.isEnabled = true

        return true
    }


    private suspend fun validateDriverLicense(): Boolean {
        val driverLicense = binding.editText.text.toString()

        if (driverLicense.isEmpty()) {
            Toast.makeText(
                this,
                getString(R.string.error_enter_driver_license),
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        val regex = Regex("^\\d{10}$")
        if (!regex.matches(driverLicense)) {
            Toast.makeText(
                this,
                getString(R.string.error_driver_license_length),
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        // Проверка на уникальность номера водительского удостоверения
        val isDriverLicenseExists = viewModel.checkIfDriverLicenseExists(driverLicense)
        if (isDriverLicenseExists) {
            Toast.makeText(
                this,
                getString(R.string.error_driver_license_already_used),
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        binding.editText.error = null
        return true
    }

    private fun saveUserToDatabase() {
        lifecycleScope.launchWhenStarted {
            try {
                Log.d("SignUp3Activity", "Starting to save user data to database...")
                viewModel.saveUserToDatabase()
                Log.d("SignUp3Activity", "User data saved successfully!")
                Toast.makeText(this@SignUp3Activity, "Регистрация завершена!", Toast.LENGTH_LONG).show()
                startActivity(Intent(this@SignUp3Activity, CongratulationsActivity::class.java))
                finish()
            } catch (e: Exception) {
                Log.e("SignUp3Activity", "Error saving user data: ${e.message}")
                Toast.makeText(this@SignUp3Activity, "Ошибка при сохранении данных", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun saveDataToViewModel() {
        viewModel.driverLicenseNumber = binding.editText.text.toString().trim()
        viewModel.driverLicenseIssueDate = binding.editText3.text.toString().trim()
        viewModel.avatar = avatarBitmap?.toString() ?: ""
        viewModel.driverLicensePhoto = binding.textView9.text.toString().trim()
        viewModel.passportPhoto = binding.textView10.text.toString().trim()
        viewModel.registrationDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())

        Log.d("SignUp3Activity", "Email saved to ViewModel: ${viewModel.email}")
        Log.d("SignUp3Activity", "Password saved to ViewModel: ${viewModel.password}")
        Log.d("SignUp3Activity", "firstName saved to ViewModel: ${viewModel.firstName}")
        Log.d("SignUp3Activity", "LastName saved to ViewModel: ${viewModel.lastName}")
        Log.d("SignUp3Activity", "birthDate saved to ViewModel: ${viewModel.birthDate}")
        Log.d("SignUp3Activity", "driverLicenseNumber saved to ViewModel: ${viewModel.driverLicenseNumber}")
        Log.d("SignUp3Activity", "driverLicenseIssueDate saved to ViewModel: ${viewModel.driverLicenseIssueDate}")
        Log.d("SignUp3Activity", "avatar saved to ViewModel: ${viewModel.avatar}")
    }

    private fun setupButton() {
        binding.buttonContinue.setOnClickListener {
            lifecycleScope.launch {
                isFormSubmitted = true
                if (validateForm()) {
                    saveDataToViewModel()
                    saveUserToDatabase()
                    startActivity(Intent(this@SignUp3Activity, CongratulationsActivity::class.java))
                }
            }
        }
    }

    private fun isValidDate(date: String): Boolean {
        val parts = date.split("/")
        if (parts.size != 3) return false

        return try {
            val day = parts[0].toInt()
            val month = parts[1].toInt()
            val year = parts[2].toInt()

            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month - 1, day)

            if (selectedDate.after(Calendar.getInstance())) {
                return false
            }

            true
        } catch (e: Exception) {
            false
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)

                binding.editText3.setText(formattedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }
}