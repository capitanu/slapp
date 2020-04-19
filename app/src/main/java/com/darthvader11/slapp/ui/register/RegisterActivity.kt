package com.darthvader11.slapp.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.darthvader11.slapp.R
import com.darthvader11.slapp.Objects.User
import com.darthvader11.slapp.server.GetUserCallback
import com.darthvader11.slapp.server.ServerRequest
import com.darthvader11.slapp.ui.login.LoginActivity
import com.darthvader11.slapp.ui.login.afterTextChanged

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var registerViewModel: RegisterViewModel
    lateinit var username: EditText
    lateinit var email: EditText
    lateinit var password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)
        this.window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        username = findViewById<EditText>(R.id.username_register)
        email = findViewById<EditText>(R.id.email_register)
        password = findViewById<EditText>(R.id.password_register)
        val verifyPassword = findViewById<EditText>(R.id.verify_password)
        val registerBtn = findViewById<Button>(R.id.registerBtn)
        registerBtn.setOnClickListener(this)

        registerViewModel = ViewModelProviders.of(this, RegisterViewModelFactory()).get(RegisterViewModel::class.java)

        registerViewModel.registerFormState.observe(this@RegisterActivity, androidx.lifecycle.Observer {
            val registerState: RegisterFormState = it ?: return@Observer

            registerBtn.isEnabled = registerState.isDataValid

            if(registerState.usernameError != null){
                username.error = getString(registerState.usernameError)
            }
            if(registerState.mailError != null){
                email.error = getString(registerState.mailError)
            }
            if(registerState.passwordError != null){
                password.error = getString(registerState.passwordError)
            }
            if(registerState.verifyPasswordError != null){
                verifyPassword.error = getString(registerState.verifyPasswordError)
            }

        })

        username.afterTextChanged {
            registerViewModel.registerDataChanged(
                username.text.toString(),
                email.text.toString(),
                password.text.toString(),
                verifyPassword.text.toString()
            )
        }

        email.afterTextChanged {
            registerViewModel.registerDataChanged(
                username.text.toString(),
                email.text.toString(),
                password.text.toString(),
                verifyPassword.text.toString()
            )
        }

        password.afterTextChanged {
            registerViewModel.registerDataChanged(
                username.text.toString(),
                email.text.toString(),
                password.text.toString(),
                verifyPassword.text.toString()
            )
        }

        verifyPassword.afterTextChanged {
            registerViewModel.registerDataChanged(
                username.text.toString(),
                email.text.toString(),
                password.text.toString(),
                verifyPassword.text.toString()
            )
        }

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.registerBtn -> {
                /*var serverRequest: ServerRequest = ServerRequest(this, R.layout.activity_register)
                serverRequest.storeUserDataInBackground(user, object: GetUserCallback {
                    override fun done(returnedUser: User?) {

                    }
                }

                */

                var user: User =
                    User(
                        username.text.toString(),
                        email.text.toString(),
                        password.text.toString(),
                        0
                    )
                registerUser(user)

            }
        }
    }

    private fun registerUser(user: User){

        var serverRequest: ServerRequest = ServerRequest(this, R.layout.activity_register)
        Log.d("ServerDebug","got to registerUser")
        serverRequest.storeUserDataInBackground(user, object: GetUserCallback{
            override fun done(returnedUser: User?) {
                startActivity(Intent(baseContext, LoginActivity::class.java))

            }
        })

    }

}