package com.mustafa.googlesignin

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.google.android.gms.auth.api.identity.Identity
import com.mustafa.googlesignin.presentation.navigation.GoogleSignInApp
import com.mustafa.googlesignin.presentation.signin.google.GoogleAuthClient
import com.mustafa.googlesignin.ui.theme.GoogleSignInTheme


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {

    private val googleAuthClient by lazy {
        GoogleAuthClient(applicationContext, Identity.getSignInClient(applicationContext))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            GoogleSignInTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    GoogleSignInApp(googleAuthClient)

                }
            }
        }
    }
}
