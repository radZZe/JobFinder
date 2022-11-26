package com.example.jobfinder.data

import com.example.jobfinder.data.models.Employer
import com.example.jobfinder.data.models.Student
import com.example.jobfinder.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class FirebaseRepository(
    auth:FirebaseAuth,
    firestore: FirebaseFirestore,
    storage: FirebaseStorage,
    manager: PreferenceManager
):FirebaseRepositoryInterface {

    val auth = auth
    val database = firestore
    val storage = storage
    val manager = manager
    override fun login(email:String,password:String,onComplete:()->Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            database.collection(KEY_COLLECTION_USERS).whereEqualTo(KEY_USER_EMAIL,email).get()
                .addOnCompleteListener {
                    it.result.documents[0].reference.get().addOnCompleteListener {
                        var userId = it.result.get(KEY_USER_ID)!! as String
                        var email = it.result.get(KEY_USER_EMAIL)!! as String
                        var name = it.result.get(KEY_USER_NAME)!! as String
                        var surname = it.result.get(KEY_USER_SURNAME)!! as String
                        var lastname = it.result.get(KEY_USER_LASTNAME)!! as String
                        var age = it.result.get(KEY_USER_AGE)!! as String
                        var male = it.result.get(KEY_USER_MALE)!! as String
                        var image = it.result.get(KEY_USER_IMAGE)!!as String
                        if(it.result.get(KEY_USER_TYPE)!! == "employer"){
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

                        }else{
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
        auth.createUserWithEmailAndPassword(user.email,user.password).addOnCompleteListener {
            database.collection(KEY_COLLECTION_USERS).document(user.id).set(user).addOnCompleteListener {
                onComplete()
            }
        }
    }

    override fun signUpAsEmployer(user: Employer, onComplete: () -> Unit) {
        auth.createUserWithEmailAndPassword(user.email,user.password).addOnCompleteListener {
            database.collection(KEY_COLLECTION_USERS).document(user.id).set(user).addOnCompleteListener {
                onComplete()
            }
        }
    }


}