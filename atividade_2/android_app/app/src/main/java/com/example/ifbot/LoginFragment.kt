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
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.cognito.options.AWSCognitoAuthSignOutOptions
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult
import com.amplifyframework.auth.options.AuthFetchSessionOptions
import com.amplifyframework.auth.result.AuthSessionResult
import com.amplifyframework.auth.result.step.AuthSignInStep
import com.amplifyframework.core.Amplify

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    lateinit var emailEdit: EditText
    lateinit var passwordEdit: EditText
    lateinit var loginButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        loginButton = view.findViewById(R.id.btn_login)
        emailEdit = view.findViewById(R.id.et_email)
        passwordEdit = view.findViewById(R.id.et_password)

        loginButton.setOnClickListener { login() }
        return view
    }

    private fun login() {
        if (emailEdit.text.isEmpty())
            Toast.makeText(view?.context, "Campo email é necessário.", Toast.LENGTH_LONG).show()
        else if (passwordEdit.text.isEmpty())
            Toast.makeText(view?.context, "Campo senha é necessário.", Toast.LENGTH_LONG).show()
        else Amplify.Auth.signIn(
            emailEdit.text.toString(),
            passwordEdit.text.toString(),
            {
                if (it.isSignedIn) startActivity(Intent(view?.context, AppActivity::class.java))
            },
            {
                Log.e("Auth", "Error trying to login", it)
                activity?.runOnUiThread { Toast.makeText(view?.context, "Usuário ou senha inválido!", Toast.LENGTH_LONG).show() }
            }
        )
    }


}