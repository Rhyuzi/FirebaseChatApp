package com.azi.firebasechat.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.azi.firebasechat.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private var auth = FirebaseAuth.getInstance()
    private var firebaseUser = auth.currentUser!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)


        if (firebaseUser != null) {
            val intent = Intent(
                this@LoginActivity,
                UsersActivity::class.java
            )
            startActivity(intent)
            finish()
        }


        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        btnLogin.setOnClickListener{
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)){
                Toast.makeText(applicationContext,"Email dan Password harus diisi",Toast.LENGTH_SHORT).show()
            }else{
                auth!!.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this){
                        if (it.isSuccessful){
                            etEmail.setText("")
                            etPassword.setText("")

                            Log.d("LoginActivity","Login Suksesss")
                            val intent = Intent(
                                this@LoginActivity,
                                UsersActivity::class.java
                            )
                            startActivity(intent)
                            finish()
                        }else{
                            Toast.makeText(applicationContext,"Email atau Password salah",Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        btnSignUp.setOnClickListener{
            val intent = Intent(
                this@LoginActivity,
                SignUpActivity::class.java
            )
            startActivity(intent)
            finish()
        }
    }
}