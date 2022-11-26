package com.example.jobfinder.di

import com.example.jobfinder.data.FirebaseRepository
import com.example.jobfinder.utils.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class FirebaseModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth{
        return Firebase.auth
    }

    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    fun provideFirebaseRepository(
        auth:FirebaseAuth,
        firestore: FirebaseFirestore,
        storage: FirebaseStorage,
        manager: PreferenceManager
    ):FirebaseRepository{

        return FirebaseRepository(auth,firestore,storage,manager)
    }

    @Provides
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }
}