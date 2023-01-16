package com.example.ifbot

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.amplifyframework.auth.AuthUserAttribute
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.core.Amplify

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {

    private lateinit var fullNameEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        fullNameEditText = view.findViewById(R.id.et_name)
        usernameEditText = view.findViewById(R.id.et_username)
        emailEditText = view.findViewById(R.id.et_email)
        passwordEditText = view.findViewById(R.id.et_password)
        confirmPasswordEditText = view.findViewById(R.id.et_repassword)
        registerButton = view.findViewById(R.id.btn_register)

        registerButton.setOnClickListener { register() }

        return view
    }

    private fun register() {

        if (confirmPasswordEditText.text.toString() != passwordEditText.text.toString()) {
            Toast.makeText(view?.context, "Senha e confirmação não confere.", Toast.LENGTH_LONG).show()
            return
        }

        val options = AuthSignUpOptions.builder()
            .userAttributes(mutableListOf(
                AuthUserAttribute(AuthUserAttributeKey.email(), emailEditText.text.toString()),
                AuthUserAttribute(AuthUserAttributeKey.name(), fullNameEditText.text.toString())
            ))
            .build()

        Amplify.Auth.signUp(
            usernameEditText.text.toString(),
            passwordEditText.text.toString(),
            options,
            {
                val intent = Intent(view?.context, ConfirmSignupActivity::class.java)
                intent.putExtra("username", usernameEditText.text.toString())
                startActivity(intent)
            },
            {
                activity?.runOnUiThread { Toast.makeText(view?.context, "Erro", Toast.LENGTH_LONG).show() }
                Log.e("SignupError", it.toString())
            }
        )

    }

}