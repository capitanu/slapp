package com.darthvader11.slapp.ui.register

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.darthvader11.slapp.R

class RegisterViewModel : ViewModel() {


    private val _registerForm = MutableLiveData<RegisterFormState>()
    val registerFormState: LiveData<RegisterFormState> = _registerForm

    fun registerDataChanged(username: String, email: String, password: String, verifyPassword: String) {
        if (!isUserNameValid(username)){
            _registerForm.value = RegisterFormState(usernameError = R.string.register_usernameTooShort)
        } else if (!isEmailValid(email)){
            _registerForm.value = RegisterFormState(mailError = R.string.register_mailError)
        }else if(!isPasswordValid(password)){
            _registerForm.value = RegisterFormState(passwordError = R.string.invalid_password)
        } else if(!verifyPasswordValid(password, verifyPassword)){
            _registerForm.value = RegisterFormState(verifyPasswordError = R.string.invalid_verification)
        } else {
            _registerForm.value = RegisterFormState(isDataValid = true)
        }

    }

    private fun isUserNameValid(username: String) : Boolean{
        if(username.length < 5)
            return false
         return true
    }

    private fun isEmailValid(mail: String) : Boolean{
        return if(mail.contains('@')){
            Patterns.EMAIL_ADDRESS.matcher(mail).matches()
        } else {
            false
        }
    }
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    private fun verifyPasswordValid(password: String, verifyPassword: String): Boolean {
        return password.equals(verifyPassword)
    }
}