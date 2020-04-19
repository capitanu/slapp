package com.darthvader11.slapp.ui.login

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders


import com.darthvader11.slapp.R
import com.darthvader11.slapp.Objects.User
import com.darthvader11.slapp.Objects.UserLocalStore
import com.darthvader11.slapp.Objects.user_id
import com.darthvader11.slapp.server.GetUserCallback
import com.darthvader11.slapp.server.ServerRequest
import com.darthvader11.slapp.ui.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var loginViewModel: LoginViewModel
    lateinit var userLocalStore: UserLocalStore
    var myPreferences = "myPrefs"
    lateinit var sharedPreferences: SharedPreferences
    private var EMPTY = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)

        sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE)
        if(sharedPreferences.getString("email", EMPTY) != EMPTY){
            val intent = Intent(this , com.darthvader11.slapp.MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            ActivityCompat.finishAffinity(this)
        }
        setContentView(R.layout.activity_login)






        Log.d("myTag", "Test")

        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val login = findViewById<Button>(R.id.login)
        val loading = findViewById<ProgressBar>(R.id.loading)

        val register : TextView = findViewById(R.id.register_Signup)
        register.setOnClickListener(this)

        userLocalStore = UserLocalStore(this)


        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                email.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })



        email.afterTextChanged {
            loginViewModel.loginDataChanged(
                email.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    email.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            email.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {

                var emailtxt = email.text.toString()
                var passwordtxt = password.text.toString()
                var user: User = User(emailtxt, passwordtxt)
                loading.visibility = View.VISIBLE
                authenticate(user)
                loading.visibility = View.INVISIBLE


                loginViewModel.login(email.text.toString(), password.text.toString())
            }
        }


    }

    private fun authenticate(user: User){

        var serverRequest: ServerRequest = ServerRequest(this, R.layout.activity_login)
        serverRequest.fetchUserDataInBackground(user, object : GetUserCallback {
            override fun done(returnedUser: User?) {
                if(returnedUser?.username == "NOT_FOUND"){
                    showErrorMessage()
                }
                else{
                    logUserIn(returnedUser!!)
                }


            }
        })

    }

    private fun logUserIn(user: User){

        userLocalStore.storeUserData(user)
        userLocalStore.setUserLoggedIn(true)

        val editor = sharedPreferences.edit()

        editor.putString("email", email.text.toString())
        editor.putString("password", password.text.toString())
        editor.putInt("user_id", user.user_id)
        editor.apply()

        user_id.user_id = user.user_id


        val intent = Intent(this , com.darthvader11.slapp.MainActivity::class.java)
        startActivity(intent)



    }

    private fun showErrorMessage(){

        var dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Incorrect user detail")
        dialogBuilder.setPositiveButton("Ok", null)
        dialogBuilder.show()

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.register_Signup -> {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)

            }



        }
    }


}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })


}
