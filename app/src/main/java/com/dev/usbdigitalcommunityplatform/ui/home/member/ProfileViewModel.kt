package com.dev.usbdigitalcommunityplatform.ui.home.member

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dev.usbdigitalcommunityplatform.ui.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel : ViewModel() {
    var userProfile by mutableStateOf(UserProfile())
        private set

    init {
        fetchUserProfile()
    }

    private fun fetchUserProfile() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .addSnapshotListener { snapshot, _ ->
                    snapshot?.toObject(UserProfile::class.java)?.let {
                        userProfile = it
                    }
                }
        }
    }
}
