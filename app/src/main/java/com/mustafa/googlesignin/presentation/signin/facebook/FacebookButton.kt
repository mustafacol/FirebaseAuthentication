package com.mustafa.googlesignin.presentation.signin.facebook

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier

@Composable
fun FacebookButton(
    facebookAuthClient: FacebookAuthClient,
    onSignInClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    DisposableEffect(Unit) {
        facebookAuthClient.signIn()

        onDispose {
            facebookAuthClient.dispose()
        }
    }
    Button(
        modifier = modifier,
        onClick = {
            // start the sign-in flow
            onSignInClick()
        }) {
        Text("Continue with Facebook")
    }

}