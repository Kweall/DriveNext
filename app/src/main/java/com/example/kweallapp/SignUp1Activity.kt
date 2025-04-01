package com.example.kweallapp

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.kweallapp.databinding.SignUp1Binding
import com.example.kweallapp.utils.PasswordHashingUtils
import com.example.kweallapp.viewmodel.SignUpViewModel
import kotlinx.coroutines.launch
import java.util.Date

class SignUp1Activity : BaseActivity() {

    private lateinit var binding: SignUp1Binding
    private lateinit var viewModel: SignUpViewModel
    private var isFormSubmitted = false
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignUp1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Получаем доступ к базе данных через MyApp
        val myApp = application as MyApp
        val userDao = myApp.database.userDao()

        // Создаем ViewModel с помощью фабрики
//        viewModel = ViewModelProvider(
//            this,
//            SignUpViewModel.SignUpViewModelFactory(userDao)
//        )[SignUpViewModel::class.java]
        viewModel = SignUpViewModel.getInstance(userDao)

        updatePasswordField(binding.editText1, isPasswordVisible)
        updatePasswordField(binding.editText2, isPasswordVisible)

        binding.imageView.setOnClickListener {
            startActivity(Intent(this, GettingStartedActivity::class.java))
        }

        setupPasswordToggle()
        setupButton()
    }

    private fun setupPasswordToggle() {
        binding.imageView4.setOnClickListener {
            togglePasswordVisibility()
        }
        binding.imageView5.setOnClickListener {
            togglePasswordVisibility()
        }
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible

        val iconRes = if (isPasswordVisible) {
            R.drawable.ic_opened_eye
        } else {
            R.drawable.ic_closed_eye
        }
        binding.imageView4.setImageResource(iconRes)
        binding.imageView5.setImageResource(iconRes)

        updatePasswordField(binding.editText1, isPasswordVisible)
        updatePasswordField(binding.editText2, isPasswordVisible)
    }

    private fun updatePasswordField(editText: EditText, isVisible: Boolean) {
        val selection = editText.selectionEnd

        editText.inputType = if (isVisible) {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        editText.typeface = Typeface.DEFAULT
        editText.setSelection(selection)
    }

    private suspend fun validateForm(): Boolean {
        isFormSubmitted = true

        if (!validateEmail()) {
            return false
        }

        if (!validatePasswords()) {
            return false
        }

        if (!binding.checkBox.isChecked) {
            Toast.makeText(
                this,
                getString(R.string.error_agree_terms),
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        return true
    }

    private suspend fun validateEmail(): Boolean {
        val email = binding.editText.text.toString()
        val pattern = Patterns.EMAIL_ADDRESS
        val isValid = pattern.matcher(email).matches()

        if (email.isEmpty()) {
            Toast.makeText(
                this,
                getString(R.string.error_enter_email),
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        if (!isValid) {
            Toast.makeText(
                this,
                getString(R.string.error_invalid_email),
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        // Проверяем, существует ли пользователь с таким email
        val userExists = viewModel.checkIfUserExists(email)
        if (userExists) {
            Toast.makeText(
                this,
                getString(R.string.error_email_already_used),
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        binding.editText.error = null
        return true
    }

    private fun validatePasswords(): Boolean {
        val pass1 = binding.editText1.text.toString()
        val pass2 = binding.editText2.text.toString()

        if (pass1.isEmpty()) {
            if (isFormSubmitted) {
                Toast.makeText(
                    this,
                    getString(R.string.error_enter_password),
                    Toast.LENGTH_LONG
                ).show()
            }
            return false
        }

        if (pass1.length < 6) {
            Toast.makeText(
                this,
                getString(R.string.error_password_length),
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        if (pass2.isEmpty()) {
            if (isFormSubmitted) {
                Toast.makeText(
                    this,
                    getString(R.string.error_confirm_password),
                    Toast.LENGTH_LONG
                ).show()
            }
            return false
        }

        if (pass1 != pass2) {
            if (isFormSubmitted) {
                Toast.makeText(
                    this,
                    getString(R.string.error_passwords_mismatch),
                    Toast.LENGTH_LONG
                ).show()
            }
            return false
        }

        binding.editText1.error = null
        binding.editText2.error = null
        return true
    }

    private fun setupButton() {
        binding.buttonContinue.setOnClickListener {
            lifecycleScope.launch {
                if (validateForm()) {
                    if (binding.checkBox.isChecked) {
                        saveDataToViewModel()
                        startActivity(Intent(this@SignUp1Activity, SignUp2Activity::class.java))
                    } else {
                        Toast.makeText(
                            this@SignUp1Activity,
                            getString(R.string.error_agree_terms),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun saveDataToViewModel() {
        Log.d("SignUp1Activity", "ВЫЗВАЛИ СОХРАНЕНИЕ")
        val email = binding.editText.text.toString().trim()
        val password = binding.editText1.text.toString().trim()

        viewModel.email = email
        viewModel.password = PasswordHashingUtils.hashPassword(password)

        Log.d("SignUp1Activity", "Email saved to ViewModel: $email")
        Log.d("SignUp1Activity", "Password saved to ViewModel: $password")
    }

    private fun showToast(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_LONG).show()
    }
}