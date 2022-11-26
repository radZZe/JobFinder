package com.example.jobfinder.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.jobfinder.data.models.Project
import com.example.jobfinder.utils.KEY_COLLECTION_PROJECTS
import com.example.jobfinder.utils.KEY_COLLECTION_USERS_PROJECTS
import com.example.jobfinder.utils.KEY_STATE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage

class FirebaseRepository(
    auth: FirebaseAuth,
    firestore: FirebaseFirestore,
    storage: FirebaseStorage,
):FirebaseRepositoryInterface {

    val database = firestore
    val storage = storage
    
    override fun getProjects(
        liveData: MutableLiveData<ArrayList<Project>>,
        onSuccess: () -> Unit
    ) {
        var projects = ArrayList<Project>()
        database.collection(KEY_COLLECTION_PROJECTS).whereEqualTo(KEY_STATE, true)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {

                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {

                    if (error != null) {
                        Log.d("Firestore Error", error.message.toString())
                        return
                    } else {
                        for (dc: DocumentChange in value?.documentChanges!!) {
                            if (dc.type == DocumentChange.Type.ADDED) {
                                projects.add(dc.document.toObject(Project::class.java))
                            }
                        }
                        projects.sortByDescending {
                            it.createdAt
                        }
                        liveData.value = projects
                        onSuccess()
                    }
                }
            })
    }

    override fun getEmployerProjects(
        userId: String,
        liveData: MutableLiveData<ArrayList<Project>>,
        onSuccess: () -> Unit
    ) {
        var projects = ArrayList<Project>()
//        database.collection(KEY_COLLECTION_USERS)
//            .whereEqualTo(KEY_USER_ID, userId)
//            .get().addOnCompleteListener {
//
//                it.result.documents[0].reference.collection(KEY_COLLECTION_PROJECTS)
//                    .whereEqualTo(KEY_STATE, true)
//                    .addSnapshotListener(object : EventListener<QuerySnapshot> {
//                        override fun onEvent(
//                            value: QuerySnapshot?,
//                            error: FirebaseFirestoreException?
//                        ) {
//                            if (error != null) {
//                                Log.d("Firestore Error", error.message.toString())
//                                return
//                            } else {
//                                for (dc: DocumentChange in value?.documentChanges!!) {
//                                    if (dc.type == DocumentChange.Type.ADDED) {
//                                        projects.add(dc.document.toObject(Project::class.java))
//                                    }
//                                }
//                                projects.sortByDescending {
//                                    it.createdAt
//                                }
//                                liveData.value = projects
//                                onSuccess()
//                            }
//                        }
//                    })
//
//            }

    }


//    override fun addProject(project: Project) {
//        database.collection(KEY_COLLECTION_PROJECTS)
//            .document(project.id)
//            .set(project)
//            .addOnSuccessListener {
//                database.collection(KEY_COLLECTION_USERS).whereEqualTo(KEY_COLLECTION_USERS_PROJECTS, project.id).get()
//                    .addOnSuccessListener {
//
//                        it.documents[0].reference.collection(KEY_COLLECTION_USERS_ITEMS)
//                            .document(project.id)
//                            .set(project).addOnSuccessListener {
////                                showToast("Success")
//                            }
//
//                    }
//
//            }
//
//    }

}