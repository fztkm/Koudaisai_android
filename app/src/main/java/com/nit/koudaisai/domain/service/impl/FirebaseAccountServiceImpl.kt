package com.nit.koudaisai.domain.service.impl

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nit.koudaisai.domain.service.AccountService
import javax.inject.Inject

class FirebaseAccountServiceImpl @Inject constructor() : AccountService {

    override fun hasUser(): Boolean {
        return Firebase.auth.currentUser != null
    }

    override fun isAnonymousUser(): Boolean {
        return Firebase.auth.currentUser?.isAnonymous ?: true
    }

    override fun getUserId(): String {
        return Firebase.auth.currentUser?.uid.orEmpty()
    }

    override fun getUserEmail(): String {
        return Firebase.auth.currentUser?.email.orEmpty()
    }

    override fun authenticate(email: String, password: String, onResult: (Throwable?) -> Unit) {
        Firebase.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            onResult(it.exception)
        }
    }

    override fun createAccount(email: String, password: String, onResult: (Throwable?) -> Unit) {
        Firebase.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            onResult(it.exception)
        }
    }

    override fun sendRecoveryEmail(email: String, onResult: (Throwable?) -> Unit) {
        Firebase.auth.sendPasswordResetEmail(email).addOnCompleteListener {
            onResult(it.exception)
        }
    }

    override fun createAnonymousAccount(onResult: (Throwable?) -> Unit) {
        Firebase.auth.signInAnonymously().addOnCompleteListener { onResult(it.exception) }
    }

    override fun linkAccount(email: String, password: String, onResult: (Throwable?) -> Unit) {
        val credential = EmailAuthProvider.getCredential(email, password)
        Firebase.auth.currentUser!!.linkWithCredential(credential)
            .addOnCompleteListener { onResult(it.exception) }
    }

    override fun deleteAccount(onResult: (Throwable?) -> Unit) {
        Firebase.auth.currentUser!!.delete()
            .addOnCompleteListener { onResult(it.exception) }
    }

    override fun signOut() {
        Firebase.auth.signOut()
    }

    override fun reAuthenticate(email: String, password: String, onResult: (Throwable?) -> Unit) {
        val user = Firebase.auth.currentUser!!
        val credential = EmailAuthProvider.getCredential(email, password)
        user.reauthenticate(credential).addOnCompleteListener { onResult(it.exception) }
    }

}