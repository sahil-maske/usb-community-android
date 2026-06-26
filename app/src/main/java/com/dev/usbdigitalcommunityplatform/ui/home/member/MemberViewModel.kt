package com.dev.usbdigitalcommunityplatform.ui.home.member

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dev.usbdigitalcommunityplatform.ui.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MemberViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    var user by mutableStateOf(UserProfile())
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun loadUser() {

        val currentUser = auth.currentUser ?: return

        isLoading = true

        firestore.collection("users")
            .document(currentUser.uid)
            .get()
            .addOnSuccessListener { document ->

                if (document.exists()) {

                    user = document.toObject(UserProfile::class.java)
                        ?: UserProfile()

                    Log.d("MemberViewModel", "User Loaded")

                }

                isLoading = false
            }
            .addOnFailureListener { e ->

                Log.e("MemberViewModel", e.message.toString())

                isLoading = false
            }
    }
}