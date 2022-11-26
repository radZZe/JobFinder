package com.example.jobfinder.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.jobfinder.data.models.Employer
import com.example.jobfinder.data.models.Project
import com.example.jobfinder.data.models.Student
import com.example.jobfinder.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage

class FirebaseRepository(
    auth: FirebaseAuth,
    firestore: FirebaseFirestore,
    storage: FirebaseStorage,
    manager: PreferenceManager
) : FirebaseRepositoryInterface {

    val auth = auth
    val database = firestore
    val storage = storage
    val manager = manager


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

    override fun login(email: String, password: String, onComplete: () -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            database.collection(KEY_COLLECTION_USERS).whereEqualTo(KEY_USER_EMAIL, email).get()
                .addOnCompleteListener {
                    it.result.documents[0].reference.get().addOnCompleteListener {
                        var userId = it.result.get(KEY_USER_ID)!! as String
                        var email = it.result.get(KEY_USER_EMAIL)!! as String
                        var name = it.result.get(KEY_USER_NAME)!! as String
                        var surname = it.result.get(KEY_USER_SURNAME)!! as String
                        var lastname = it.result.get(KEY_USER_LASTNAME)!! as String
                        var age = it.result.get(KEY_USER_AGE)!! as String
                        var male = it.result.get(KEY_USER_MALE)!! as String
                        var image = it.result.get(KEY_USER_IMAGE)!! as String
                        if (it.result.get(KEY_USER_TYPE)!! == "employer") {
                            var company = it.result.get(KEY_USER_COMPANY)!! as String
                            var employer = Employer(
                                id = userId,
                                email = email,
                                password = password,
                                createdAt = null,
                                companyName = company,
                                male = male,
                                age = age,
                                image = image,
                                name = name,
                                surname = surname,
                                lastName = lastname
                            )
                            manager.putEmployer(employer)
                            onComplete()

                        } else {
                            var Uni = it.result.get(KEY_USER_UNI)!! as String
                            var student = Student(
                                id = userId,
                                email = email,
                                password = password,
                                null,
                                male = male,
                                Uni = Uni,
                                age = age,
                                image = image,
                                name = name,
                                surname = surname,
                                lastName = lastname
                            )
                            manager.putStudent(student)
                            onComplete()
                        }
                    }
                }
        }
    }

    override fun signUpAsStudent(user: Student, onComplete: () -> Unit) {
        auth.createUserWithEmailAndPassword(user.email, user.password).addOnCompleteListener {
            database.collection(KEY_COLLECTION_USERS).document(user.id).set(user)
                .addOnCompleteListener {
                    onComplete()
                }
        }
    }

    override fun signUpAsEmployer(user: Employer, onComplete: () -> Unit) {
        auth.createUserWithEmailAndPassword(user.email, user.password).addOnCompleteListener {
            database.collection(KEY_COLLECTION_USERS).document(user.id).set(user)
                .addOnCompleteListener {
                    onComplete()
                }
        }
    }

    override fun getFilteredProjects(
        filter: String,
        liveData: MutableLiveData<ArrayList<Project>>,
        onSuccess: () -> Unit
    ) {
        var projects = ArrayList<Project>()
        database.collection(KEY_COLLECTION_PROJECTS).whereEqualTo(KEY_STATE, true).whereEqualTo(
            KEY_PROJECT_TYPE, filter
        )
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