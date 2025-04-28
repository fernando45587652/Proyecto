package com.example.proyecto_de_catedra

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {
    private lateinit var tvRedirectSignUp: TextView
    lateinit var etEmail: EditText
    private lateinit var etPass: EditText
    lateinit var btnLogin: Button
    private lateinit var tvLogingoogle:TextView
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var auth_google: FirebaseAuth




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
// View Binding
        tvRedirectSignUp =
            findViewById(R.id.tvRedirectSignUp)
        btnLogin = findViewById(R.id.btnLogin)
        etEmail = findViewById(R.id.etEmailAddress)
        etPass = findViewById(R.id.etPassword)
        tvLogingoogle = findViewById(R.id.textView_login_google)
// initialising Firebase auth object
        auth = FirebaseAuth.getInstance()
        auth_google= Firebase.auth

        val google_signin_options= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this,google_signin_options)

        btnLogin.setOnClickListener {
            login()
        }
        tvRedirectSignUp.setOnClickListener {
            val intent = Intent(this,
                RegisterActivity::class.java)
            startActivity(intent)

// using finish() to end the activity
            finish()
        }
        tvLogingoogle.setOnClickListener {
            login_with_google_provider()
        }

    }
    private fun login() {
        val email = etEmail.text.toString()
        val pass = etPass.text.toString()
// calling signInWithEmailAndPassword(email, pass)
// function using Firebase auth object
// On successful response Display a Toast
        auth.signInWithEmailAndPassword(email,
            pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Toast.makeText(this, "Successfully LoggedIn", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java);
                startActivity(intent);
            } else
                Toast.makeText(this, "Log In failed", Toast.LENGTH_SHORT).show()
        }

    }
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {
                result->
            if(result.resultCode== Activity.RESULT_OK)
            {
                val
                        task=GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResult(task)
            }
        }
    private fun handleResult(task: Task<GoogleSignInAccount>) {
        if(task.isSuccessful){
            val account:GoogleSignInAccount?=task.result
            if(account!=null){
                val
                        credential= GoogleAuthProvider.getCredential(account.idToken,null)

                auth_google.signInWithCredential(credential).addOnCompleteListener{
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Successfully LoggedIn", Toast.LENGTH_SHORT).show()
                            val
                        intent=Intent(this,MainActivity::class.java)
                        startActivity(intent)
                    } else
                        Toast.makeText(this, "Log In failed ",
                            Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this, "Log In failed,Try again ",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun login_with_google_provider(){
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }//fin login google

}