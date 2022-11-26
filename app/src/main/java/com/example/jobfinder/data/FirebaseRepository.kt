package com.example.jobfinder.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class FirebaseRepository(
    auth:FirebaseAuth,
    firestore: FirebaseFirestore,
    storage: FirebaseStorage,
):FirebaseRepositoryInterface {

    val database = firestore
    val storage = storage


}