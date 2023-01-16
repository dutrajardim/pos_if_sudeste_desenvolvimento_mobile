package com.example.ifbot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amplifyframework.api.rest.RestOptions
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult
import com.amplifyframework.core.Amplify

class AppActivity : AppCompatActivity() {

    private lateinit var botListAdapter: BotListAdapter
    private val botList = mutableSetOf<String>()

    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var logoutButton: Button
    private lateinit var botListRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)

        nameTextView = findViewById(R.id.tv_username)
        emailTextView = findViewById(R.id.tv_email)
        logoutButton = findViewById(R.id.btn_logout)

        logoutButton.setOnClickListener { logout() }

        botListAdapter = BotListAdapter(botList)
        botListRecyclerView = findViewById(R.id.rv_bot_list)
        botListRecyclerView.adapter = botListAdapter
        botListRecyclerView.layoutManager = LinearLayoutManager(applicationContext)

    }

    private fun logout() {
        Amplify.Auth.signOut { signOutResult ->
            when (signOutResult) {

                is AWSCognitoAuthSignOutResult.CompleteSignOut ->
                    startActivity(Intent(this, MainActivity::class.java))

                is AWSCognitoAuthSignOutResult.FailedSignOut ->
                    this.runOnUiThread { Toast.makeText(this, "Falha ao tentar efetuar o logout!", Toast.LENGTH_LONG).show() }

            }
        }
    }

    override fun onStart() {
        super.onStart()
        updateBotList()
        updateUserProfile()
    }

    private fun updateBotList() {
        val options = RestOptions.builder().addPath("/bots").build()
        Amplify.API.get(options,
            {
                try {
                    val data = it.data.asJSONObject().getJSONArray("data")
                    for (i in 1..data.length()) {
                        val tag = data.getJSONObject(i - 1).get("Tag") as String
                        botList.add(tag)
                    }
                    this.runOnUiThread{ botListAdapter.notifyDataSetChanged() }
                } catch (e: Exception) { }
            },
            { Log.e("Api.bots", "GET failed", it) }
        )
    }

    private fun updateUserProfile() {
        Amplify.Auth.fetchUserAttributes(
            {
                val attributesMap = it.fold(mapOf<String, String>()) { acc, attr -> acc + mapOf(attr.key.keyString to attr.value) }
                this.runOnUiThread {
                    nameTextView.text = attributesMap["name"] ?: ""
                    emailTextView.text = attributesMap["email"] ?: ""
                }
            },
            {
                Log.e("API", "Failed to fetch user attribures", it)
                Amplify.Auth.signOut { startActivity(Intent(this, MainActivity::class.java)) }
            }
        )
    }
}