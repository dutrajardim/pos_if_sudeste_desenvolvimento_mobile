package com.example.ifbot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.amplifyframework.core.Amplify

class ConfirmSignupActivity : AppCompatActivity() {

    private lateinit var confirmCodeEditText: EditText
    private lateinit var confirmButton: Button
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_signup)

        username = intent.getStringExtra("username") as String

        confirmCodeEditText = findViewById(R.id.et_code)
        confirmButton = findViewById(R.id.btn_confirm)

        confirmButton.setOnClickListener { confirmCode() }
    }

    private fun confirmCode() {
        Amplify.Auth.confirmSignUp(
            username, confirmCodeEditText.text.toString(),
            {
                if (it.isSignUpComplete) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                else Log.i("AuthQuickstart","Confirm sign up not complete")
            },
            { Log.e("AuthQuickstart", "Failed to confirm sign up", it) }
        )
    }
}