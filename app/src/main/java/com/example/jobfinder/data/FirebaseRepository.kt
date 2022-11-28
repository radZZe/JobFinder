package com.example.jobfinder.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.jobfinder.data.models.*
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

        val projIds = ArrayList<String>()
        database.collection(KEY_COLLECTION_USERS)
            .document(manager.getString(KEY_USER_ID)!!).collection(KEY_COLLECTION_PROJECTS)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                } else {
                    if (value != null) {
                        for (documentChange in value.documentChanges) {

                            var doc = documentChange.document
                            projIds.add(doc.getString(KEY_PROJECT_ID)!!)
//                            var id = doc.get(KEY_PROJECT_ID).toString()
//                            var title = doc.get(KEY_PROJECT_TITLE).toString()
//                            var state = doc.getBoolean(KEY_STATE)
//                            var createdAt = doc.getDate(KEY_CREATED_AT)
//                            var creatorId = doc.get(KEY_CREATOR_ID).toString()
//                            var employer = doc.get(KEY_EMPLOYER).toString()
//                            var type = doc.get(KEY_TYPE).toString()
//                            var skills = doc.get(KEY_SKILLS).toString()
//                            var desc = doc.get(KEY_DESCRIPTION).toString()
//                            var salary = doc.get(KEY_SALARY).toString()
//                            val project = Project(
//                                id,
//                                createdAt!!,
//                                title,
//                                desc,
//                                skills,
//                                type,
//                                state!!,
//                                creatorId,
//                                salary,
//                                employer
//                            )
//                            projects.add(project)
                            getProjectsByIds(projIds, liveData, onSuccess)
                        }

                    }
                }
            }

    }

    fun getProjectsByIds(ids: ArrayList<String>, liveData: MutableLiveData<ArrayList<Project>>, onSuccess: () -> Unit) {
        var projects = ArrayList<Project>()
        for (i in ids) {
            database.collection(KEY_COLLECTION_PROJECTS).document(i).get().addOnCompleteListener {

                var doc = it.result
                var id = doc.get(KEY_PROJECT_ID).toString()
                var title = doc.get(KEY_PROJECT_TITLE).toString()
                var state = doc.getBoolean(KEY_STATE)
                var createdAt = doc.getDate(KEY_CREATED_AT)
                var creatorId = doc.get(KEY_CREATOR_ID).toString()
                var employer = doc.get(KEY_EMPLOYER).toString()
                var type = doc.get(KEY_TYPE).toString()
                var skills = doc.get(KEY_SKILLS).toString()
                var desc = doc.get(KEY_DESCRIPTION).toString()
                var salary = doc.get(KEY_SALARY).toString()
                val project = Project(
                    id,
                    createdAt!!,
                    title,
                    desc,
                    skills,
                    type,
                    state!!,
                    creatorId,
                    salary,
                    employer
                )
                projects.add(project)
                projects.sortByDescending {
                    it.createdAt
                }
                liveData.value = projects
            }

            onSuccess()
        }
    }


    override fun addProject(project: Project) {
        database.collection(KEY_COLLECTION_PROJECTS)
            .document(project.id)
            .set(project)
            .addOnSuccessListener {
                var projectMember = ProjectMember(
                    id = manager.getString(KEY_USER_ID)!!,
                    owner = "${manager.getString(KEY_USER_NAME)!!} ${manager.getString(
                        KEY_USER_SURNAME)}"
                )
                database.collection(KEY_COLLECTION_PROJECTS)
                    .document(project.id).collection(KEY_COLLECTION_MEMBERS).add(projectMember)
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

    fun listenMessageTeam(
        members: ArrayList<TeamMember>,
        teamId: String,
        messages: MutableLiveData<ArrayList<Message>>,
        onSuccess: (ArrayList<Message>) -> Unit
    ) {
        members.forEach {
            database.collection(KEY_COLLECTION_USERS).document(it.id)
                .collection(KEY_COLLECTION_TEAM_CHAT).document(teamId).collection(
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

    fun sendMessageForTeam(teamId: String, text: String, senderId: String) {
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
                    KEY_COLLECTION_TEAM_CHAT
                ).document(teamId).collection(KEY_COLLECTION_MESSAGES).add(message)

            }
        }
    }

    fun getFeedbacks(project:Project,feedBacks:MutableLiveData<ArrayList<UserFeedback>>,onSuccess: () -> Unit){
        val feedbacks = kotlin.collections.ArrayList<UserFeedback>()
        database.collection(KEY_COLLECTION_PROJECTS).document(project.id).collection(
            KEY_COLLECTION_FEEDBACK).addSnapshotListener { value, error ->
            if(error!=null){
                return@addSnapshotListener
            }else{
                if(value!=null){
                   for(documentChange in value.documentChanges){
                       var doc = documentChange.document
                       var userId = doc.getString("userId")!!
                       var name= doc.getString("name")!!
                       var surname= doc.getString("surname")!!
                       var lastName= doc.getString("lastName")!!
                       var age= doc.getString("age")!!
                       var sex= doc.getString("sex")!!
                       var uni= doc.getString("uni")!!
                       var brief= doc.getString("brief")!!
                       var feedBack = UserFeedback(
                           userId = userId,
                           name = name,
                           surname = surname,
                           lastName = lastName,
                           age = age,
                           sex = sex,
                           uni = uni,
                           brief = brief
                       )
                       if (feedBack !in feedbacks) {
                           feedbacks.add(feedBack)
                       }
                   }
                    feedBacks.value = feedbacks
                    onSuccess()
                }
            }
        }
    }

    fun feedback(project: Project,brief:String) {
        val userFeedback = UserFeedback(
            userId = manager.getString(KEY_USER_ID)!!,
            name =manager.getString(KEY_USER_NAME)!! ,
            surname = manager.getString(KEY_USER_SURNAME)!!,
            lastName = manager.getString(KEY_USER_LASTNAME)!!,
            age = manager.getString(KEY_USER_AGE)!!,
            sex = manager.getString(KEY_USER_MALE)!!,
            uni = manager.getString(KEY_USER_UNI)!!,
            brief = brief
        )

        database.collection(KEY_COLLECTION_PROJECTS).document(project.id)
            .collection(KEY_COLLECTION_FEEDBACK).add(userFeedback)

    }

    fun feedBackReject(project: Project,userId:String){
        database.collection(KEY_COLLECTION_PROJECTS).document(project.id).collection(
            KEY_COLLECTION_FEEDBACK).whereEqualTo("userId",userId).get().addOnCompleteListener {
            it.result.documents[0].reference.delete()
        }
    }

    fun addStudentToProject(project: Project,userId:String){
        database.collection(KEY_COLLECTION_PROJECTS).document(project.id).collection(
            KEY_COLLECTION_FEEDBACK).whereEqualTo("userId",userId).get().addOnCompleteListener {
                it.result.documents[0].reference.delete()
        }
        database.collection(KEY_COLLECTION_USERS).document(userId).get().addOnCompleteListener {
            var name = it.result.get(KEY_USER_NAME)!! as String
            var surname = it.result.get(KEY_USER_SURNAME)!! as String
            val projectMember = ProjectMember(
                id = userId,
                owner = "${name}  ${surname}"
            )
            val studentProject = StudentProject(
                id = project.id,
                title = project.title
            )
            database.collection(KEY_COLLECTION_PROJECTS).document(project.id).collection(
                KEY_COLLECTION_MEMBERS
            ).add(projectMember).addOnCompleteListener {
                database.collection(KEY_COLLECTION_USERS).document(userId)
                    .collection(KEY_COLLECTION_PROJECTS).document(project.id).set(studentProject)
            }
        }

    }

    fun createTeam(team: Team) {
        var teamMember = TeamMember(
            id = manager.getString(KEY_USER_ID)!! as String,
            name = manager.getString(KEY_USER_NAME)!! as String,
            surname = manager.getString(KEY_USER_SURNAME)!!,
            uni = manager.getString(KEY_USER_UNI)!!
        )
        var studentTeam = StudentTeam(
            id = team.id,
            name = team.name
        )
        database.collection(KEY_COLLECTION_TEAMS).document(team.id).set(team)
            .addOnCompleteListener {
                database.collection(KEY_COLLECTION_TEAMS).document(team.id).collection(
                    KEY_COLLECTION_MEMBERS
                ).add(teamMember).addOnCompleteListener {
                    database.collection(KEY_COLLECTION_USERS)
                        .document(manager.getString(KEY_USER_ID)!!)
                        .collection(KEY_COLLECTION_TEAMS).document(team.id).set(studentTeam)
                }
            }

    }


}