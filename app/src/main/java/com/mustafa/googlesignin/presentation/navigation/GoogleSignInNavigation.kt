package com.mustafa.googlesignin.presentation.navigation

import android.app.Activity.RESULT_OK
import android.content.IntentSender
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mustafa.googlesignin.presentation.profile.ProfileScreen
import com.mustafa.googlesignin.presentation.signin.GoogleAuthClient
import com.mustafa.googlesignin.presentation.signin.SignInScreen
import com.mustafa.googlesignin.presentation.signin.SignInViewModel
import kotlinx.coroutines.launch

@Composable
fun GoogleSignInNavigation(googleAuthClient: GoogleAuthClient) {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    NavHost(navController = navController, startDestination = "signIn") {
        composable("signIn") {
            val viewModel = viewModel<SignInViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            LaunchedEffect(key1 = Unit) {
                if (googleAuthClient.getSignedUser() != null) {
                    navController.navigate("profile")
                }
            }

            val launcher =
                rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        if (result.resultCode == RESULT_OK) {
                            coroutineScope.launch {
                                val signInResult = googleAuthClient.signInWithIntent(
                                    intent = result.data ?: return@launch
                                )
                                viewModel.onSignInResult(signInResult)
                            }
                        }
                    })

            LaunchedEffect(key1 = state.isSignInSuccessful) {
                if (state.isSignInSuccessful) {
                    Toast.makeText(context, "Login Successfully", Toast.LENGTH_SHORT).show()
                    navController.navigate("profile")
                    viewModel.resetState()
                }
            }

            SignInScreen(signInState = state, onSignInClick = {
                coroutineScope.launch {
                    val signInSender = googleAuthClient.signIn()
                    launcher.launch(
                        IntentSenderRequest.Builder(
                            signInSender ?: return@launch
                        ).build()
                    )
                }
            })


        }

        composable("profile") {
            ProfileScreen(userData = googleAuthClient.getSignedUser(), onSignOutClick = {
                coroutineScope.launch {
                    googleAuthClient.signOut()
                    Toast.makeText(context, "Log out successfully", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            })
        }
    }
}