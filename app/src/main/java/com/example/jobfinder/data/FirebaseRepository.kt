package com.example.jobfinder.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.jobfinder.data.models.*

import com.example.jobfinder.utils.KEY_COLLECTION_PROJECTS
import com.example.jobfinder.utils.KEY_COLLECTION_USERS_PROJECTS
import com.example.jobfinder.utils.KEY_STATE

import com.example.jobfinder.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList

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

    fun getChats(onComplete: (ArrayList<ChatItem>) -> Unit) {
        val chats = arrayListOf<ChatItem>()
        database.collection(KEY_COLLECTION_USERS).document(manager.getString(KEY_USER_ID)!!)
            .collection(KEY_COLLECTION_TEAMS).addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                } else {
                    if (value != null) {
                        for (documentChange in value.documentChanges) {
                            var doc = documentChange.document
                            var name = doc.get(KEY_TEAM_NAME)!! as String
                            var id = doc.get(KEY_TEAM_ID)!! as String
                            var type = KEY_TEAM
                            var item = ChatItem(type = type, id = id, name = name)
                            chats.add(item)
                        }
                        database.collection(KEY_COLLECTION_USERS)
                            .document(manager.getString(KEY_USER_ID)!!).collection(
                                KEY_COLLECTION_PROJECTS
                            ).addSnapshotListener { value, error ->
                                if (error != null) {
                                    return@addSnapshotListener
                                } else {
                                    if (value != null) {
                                        for (documentChange in value.documentChanges) {
                                            var doc = documentChange.document
                                            var id =
                                                doc.get(KEY_PROJECT_ID)!! as String
                                            var title = doc.get(
                                                KEY_PROJECT_TITLE
                                            )!! as String
                                            var type = KEY_PROJECT
                                            var item = ChatItem(
                                                id = id,
                                                name = title,
                                                type = type
                                            )
                                            chats.add(item)


                                        }
                                        onComplete(chats)
                                    }
                                }
                            }
                    } else {
                        database.collection(KEY_COLLECTION_USERS)
                            .document(manager.getString(KEY_USER_ID)!!).collection(
                                KEY_COLLECTION_PROJECTS
                            ).addSnapshotListener { value, error ->
                                if (error != null) {
                                    return@addSnapshotListener
                                } else {
                                    if (value != null) {
                                        value.documents.forEach {
                                            it.reference.get().addOnCompleteListener {
                                                var id =
                                                    it.result.get(KEY_PROJECT_ID)!! as String
                                                var title = it.result.get(
                                                    KEY_PROJECT_TITLE
                                                )!! as String
                                                var type = KEY_PROJECT
                                                var item = ChatItem(
                                                    id = id,
                                                    name = title,
                                                    type = type
                                                )
                                                chats.add(item)

                                            }
                                        }
                                        onComplete(chats)
                                    }
                                }
                            }
                    }

                }
            }
    }


    override fun getEmployerProjects(
        userId: String,
        liveData: MutableLiveData<ArrayList<Project>>,
        onSuccess: () -> Unit
    ) {
        var projects = ArrayList<Project>()
        database.collection(KEY_COLLECTION_USERS)
            .whereEqualTo(KEY_USER_ID, userId)
            .get().addOnCompleteListener {

                it.result.documents[0].reference.collection(KEY_COLLECTION_PROJECTS)
                    .whereEqualTo(KEY_STATE, true)
                    .addSnapshotListener(object : EventListener<QuerySnapshot> {
                        override fun onEvent(
                            value: QuerySnapshot?,
                            error: FirebaseFirestoreException?
                        ) {
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

    }

    override fun getEmployeeProjects(
        userId: String,
        liveData: MutableLiveData<ArrayList<Project>>,
        onSuccess: () -> Unit
    ) {
        var projects = ArrayList<Project>()
        database.collection(KEY_COLLECTION_USERS)
            .whereEqualTo(KEY_USER_ID, userId)
            .get().addOnCompleteListener {

                it.result.documents[0].reference.collection(KEY_COLLECTION_PROJECTS)
                    .whereEqualTo(KEY_STATE, true)
                    .addSnapshotListener(object : EventListener<QuerySnapshot> {
                        override fun onEvent(
                            value: QuerySnapshot?,
                            error: FirebaseFirestoreException?
                        ) {
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

    }


    override fun addProject(project: Project) {
        database.collection(KEY_COLLECTION_PROJECTS)
            .document(project.id)
            .set(project)
            .addOnSuccessListener {
                database.collection(KEY_COLLECTION_USERS)
                    .whereEqualTo(KEY_USER_ID, project.creatorId).get()
                    .addOnSuccessListener {

                        it.documents[0].reference.collection(KEY_COLLECTION_USERS_PROJECTS)
                            .document(project.id)
                            .set(project).addOnSuccessListener { }

                    }

            }

    }

    fun listenMessageProject(
        members: ArrayList<ProjectMember>,
        projectid: String,
        messages: MutableLiveData<ArrayList<Message>>,
        onSuccess: (ArrayList<Message>) -> Unit
    ) {
        members.forEach {
            database.collection(KEY_COLLECTION_USERS)
                .document(it.id)
                .collection(KEY_COLLECTION_PROJECT_CHAT).document(projectid).collection(
                    KEY_COLLECTION_MESSAGES
                ).addSnapshotListener { value, error ->
                    if (error != null) {
                        return@addSnapshotListener
                    } else {
                        if (value != null) {
                            for (documentChange in value.documentChanges) {
                                var doc = documentChange.document
                                var id = doc.get(KEY_MESSAGE_ID)!! as String
                                var senderId = doc.get(KEY_SENDER_ID)!! as String
                                var text = doc.get(KEY_MESSAGE_TEXT)!! as String
                                var timestamp = doc.getDate(KEY_MESSAGE_TIMESTAMP)!!
                                var owner = doc.get(KEY_MESSAGE_OWNER)!! as String
                                var message = Message(
                                    id = id,
                                    text = text,
                                    senderId = senderId,
                                    timestamp = timestamp,
                                    owner = owner
                                )
                                if (message !in messages.value!!) {
                                    messages.value!!.add(message)
                                }
                            }
                            messages.value!!.sortBy { it -> it.timestamp }
                            onSuccess(messages.value!!)
                        }
                    }
                }


        }

    }

    fun sendMessageForProject(projectId: String, text: String, senderId: String) {

        SNTPClient.getDate(
            TimeZone.getTimeZone(Calendar.getInstance().getTimeZone().toString())
        ) { _, date, _ ->
            var id = UUID.randomUUID().mostSignificantBits.toString()
            var owner =
                "${manager.getString(KEY_USER_NAME)!!} ${manager.getString(KEY_USER_SURNAME)!!}"
            if (date != null) {
                val message = Message(
                    id = id,
                    timestamp = date,
                    senderId = senderId,
                    text = text,
                    owner = owner
                )
                database.collection(KEY_COLLECTION_USERS).document(senderId).collection(
                    KEY_COLLECTION_PROJECT_CHAT
                ).document(projectId).collection(KEY_COLLECTION_MESSAGES).add(message)

            }
        }


    }


}