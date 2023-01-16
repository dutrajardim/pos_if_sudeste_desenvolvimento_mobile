package com.example.ifbot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.amplifyframework.AmplifyException
import com.amplifyframework.api.aws.AWSApiPlugin
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.exceptions.NotAuthorizedException
import com.amplifyframework.auth.options.AuthFetchSessionOptions
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.Amplify.AlreadyConfiguredException
import java.util.concurrent.CompletableFuture

private const val NUM_PAGES = 2

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureAmplify()

        viewPager = findViewById(R.id.viewPager)
        val pagerAdapter = AuthenticationPagerAdapter(this)
        viewPager.adapter = pagerAdapter

    }

    override fun onStart() {
        super.onStart()
        val options = AuthFetchSessionOptions.builder().forceRefresh(true).build()
        Amplify.Auth.fetchAuthSession(
            options,
            {
                val session = it as AWSCognitoAuthSession
                if (session.isSignedIn) {
                    val intent = Intent(this, AppActivity::class.java)
                    startActivity(intent)
                }
            },
            {
                if (it is NotAuthorizedException) Amplify.Auth.signOut { }
                else Log.e("Authentication", "Failed to fetch auth session", it)
            }
        )
    }

    private inner class AuthenticationPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES
        override fun createFragment(position: Int): Fragment = if(position == 0) LoginFragment() else RegisterFragment()
    }

    private fun configureAmplify() {
        try {
            val plugin = AWSApiPlugin.builder()
                .configureClient("bot-api") { okHttpClient ->
                    okHttpClient.addInterceptor { chain ->
                        val future = CompletableFuture<String>()
                        Amplify.Auth.fetchAuthSession(
                            { future.complete((it as AWSCognitoAuthSession).userPoolTokensResult.value?.idToken) },
                            { future.completeExceptionally(it) }
                        )
                        val token = future.get()
                        val originalRequest = chain.request()
                        val updatedRequest = originalRequest.newBuilder()
                            .addHeader("Authorization", token)
                            .build()
                        chain.proceed(updatedRequest)
                    }
                }.build()

            Amplify.addPlugin(plugin)
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.configure(this)
        } catch (error: AmplifyException) {
            Log.e("MainActivity", "Could not initialize Amplify", error)
        }
    }
}
