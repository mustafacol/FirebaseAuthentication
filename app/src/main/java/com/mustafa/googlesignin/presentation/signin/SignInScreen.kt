package com.mustafa.googlesignin.presentation.signin

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.mustafa.googlesignin.presentation.signin.facebook.FacebookAuthClient
import com.mustafa.googlesignin.presentation.signin.facebook.FacebookButton

@Composable
fun SignInScreen(
    signInState: SignInState,
    facebookAuthClient: FacebookAuthClient,
    onSignInClick: () -> Unit,
    onFacebookSignIn:()->Unit
) {

    val context = LocalContext.current
    LaunchedEffect(key1 = signInState.signInError) {
        signInState.signInError?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = onSignInClick) {
            Text(text = "SignIn With Google")
        }

        FacebookButton(facebookAuthClient,onSignInClick = onFacebookSignIn)

    }

}