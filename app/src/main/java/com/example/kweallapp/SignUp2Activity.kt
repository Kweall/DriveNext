package com.example.kweallapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.kweallapp.databinding.SignUp1Binding
import com.example.kweallapp.databinding.SignUp2Binding
import com.example.kweallapp.viewmodel.SignUpViewModel
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.TextUtils
import java.util.Calendar

class SignUp2Activity : BaseActivity() {

    private lateinit var binding: SignUp2Binding
    private lateinit var viewModel: SignUpViewModel
    private var isFormSubmitted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignUp2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val myApp = application as MyApp
        val userDao = myApp.database.userDao()

//        viewModel = ViewModelProvider(
//            this,
//            SignUpViewModel.SignUpViewModelFactory(userDao)
//        )[SignUpViewModel::class.java]
        viewModel = SignUpViewModel.getInstance(userDao)

        binding.buttonContinue.setOnClickListener {
            startActivity(Intent(this, SignUp3Activity::class.java))
        }

        binding.imageView.setOnClickListener {
            startActivity(Intent(this, SignUp1Activity::class.java))
        }

        binding.imageView5.setOnClickListener {
            showDatePicker()
        }

        setupButton()
    }

    private fun validateForm(): Boolean {
        isFormSubmitted = true

        if (!validateSurname()) {
            return false
        }

        if (!validateName()) {
            return false
        }

        if (!validatePatronymic()) {
            return false
        }

        val dateOfBirth = binding.editText3.text.toString()
        if (TextUtils.isEmpty(dateOfBirth) || !isValidDate(dateOfBirth)) {
            if (TextUtils.isEmpty(dateOfBirth)) {
                Toast.makeText(
                    this,
                    getString(R.string.error_enter_date_of_birth),
                    Toast.LENGTH_LONG
                ).show()
                return false
            } else{
                Toast.makeText(
                    this,
                    getString(R.string.error_age_less_than_16),
                    Toast.LENGTH_LONG
                ).show()
                return false
            }
        }

        if (binding.radioGroupGender.checkedRadioButtonId == -1) {
            Toast.makeText(
                this,
                getString(R.string.error_select_gender),
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        return true
    }


    private fun validateSurname(): Boolean {
        val surname = binding.editText.text.toString()

        if (surname.isEmpty()) {
            Toast.makeText(
                this,
                getString(R.string.error_enter_surname),
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        binding.editText.error = null
        return true
    }

    private fun validateName(): Boolean {
        val name = binding.editText1.text.toString()

        if (name.isEmpty()) {
            Toast.makeText(
                this,
                getString(R.string.error_enter_name),
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        binding.editText1.error = null
        return true
    }

    private fun validatePatronymic(): Boolean {
        val patronymic = binding.editText2.text.toString()

        if (patronymic.isEmpty()) {
            Toast.makeText(
                this,
                getString(R.string.error_enter_patronymic),
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        binding.editText2.error = null
        return true
    }

    private fun setupButton() {
        binding.buttonContinue.setOnClickListener {
            isFormSubmitted = true
            if (validateForm()) {
                saveDataToViewModel()
                startActivity(Intent(this, SignUp3Activity::class.java))
            }
        }
    }

    private fun saveDataToViewModel() {
        viewModel.firstName = binding.editText1.text.toString().trim()
        viewModel.lastName = binding.editText.text.toString().trim()
        viewModel.birthDate = binding.editText3.text.toString().trim()

        Log.d("SignUp1Activity", "Email saved to ViewModel: ${viewModel.email}")
        Log.d("SignUp1Activity", "Password saved to ViewModel: ${viewModel.password}")
        Log.d("SignUp1Activity", "firstName saved to ViewModel: ${viewModel.firstName}")
        Log.d("SignUp1Activity", "LastName saved to ViewModel: ${viewModel.lastName}")
        Log.d("SignUp1Activity", "birthDate saved to ViewModel: ${viewModel.birthDate}")
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

            val minDateOfBirth = Calendar.getInstance()
            minDateOfBirth.add(Calendar.YEAR, -16)

            if (selectedDate.after(minDateOfBirth)) {
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

