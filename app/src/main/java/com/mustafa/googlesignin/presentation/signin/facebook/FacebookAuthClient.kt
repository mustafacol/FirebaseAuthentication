package com.mustafa.googlesignin.presentation.signin.facebook

import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mustafa.googlesignin.presentation.signin.SignInResult
import com.mustafa.googlesignin.presentation.signin.UserData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FacebookAuthClient(
    private val coroutineScope: CoroutineScope,
    private val callbackManager: CallbackManager,
    private val onSuccess: (SignInResult) -> Unit,
    private val onError: (String) -> Unit
) : FacebookCallback<LoginResult> {

    private val loginManager = LoginManager.getInstance()
    private var signInResult: SignInResult = SignInResult(null, null)
    fun signIn() {
        loginManager.registerCallback(callbackManager, this)
    }

    fun dispose() {
        loginManager.unregisterCallback(callbackManager)
    }

    override fun onCancel() = Unit

    override fun onError(error: FacebookException) {
        onError(error.message ?: "Unable to sign in with Facebook")
    }

    override fun onSuccess(result: LoginResult) {
        coroutineScope.launch {
            val token = result.accessToken.token
            val credential = FacebookAuthProvider.getCredential(token)
            try {
                val user = Firebase.auth.signInWithCredential(credential).await().user
                signInResult = SignInResult(
                    data = user?.run {
                        UserData(
                            userId = uid,
                            username = displayName ?: "",
                            profileUrl = photoUrl.toString()
                        )
                    },
                    errorMessage = null
                )
                onSuccess(signInResult)
            } catch (e: Exception) {
                e.printStackTrace()
                signInResult = SignInResult(null, e.message)
                onError(e.message ?: "")
            }
        }
    }
}