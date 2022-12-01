package com.example.jobfinder.data

import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.jobfinder.R
import com.example.jobfinder.data.models.*
import com.example.jobfinder.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.*

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

    override fun login(
        email: String,
        password: String,
        onComplete: () -> Unit,
        onFail: () -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
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
            .addOnFailureListener {
                onFail()
                AlertDialog.Builder(APP_ACTIVITY)
                    .setTitle(APP_ACTIVITY.applicationContext.getString(R.string.failed_sign_n))
                    .setMessage(APP_ACTIVITY.applicationContext.getString(R.string.failed_sign_in_message))
                    .setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, _ ->
                        dialogInterface.cancel()
                    })
                    .create()
                    .show()
            }
    }

    override fun signUpAsStudent(user: Student, onComplete: () -> Unit) {
        auth.createUserWithEmailAndPassword(user.email, user.password).addOnCompleteListener {
            database.collection(KEY_COLLECTION_USERS).document(user.id).set(user)
                .addOnCompleteListener {
                    manager.putStudent(user)
                    onComplete()
                }
        }
    }

    override fun signUpAsEmployer(user: Employer, onComplete: () -> Unit) {
        auth.createUserWithEmailAndPassword(user.email, user.password).addOnCompleteListener {
            database.collection(KEY_COLLECTION_USERS).document(user.id).set(user)
                .addOnCompleteListener {
                    manager.putEmployer(user)
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
                            if (documentChange.type != DocumentChange.Type.REMOVED) {
                                chats.add(item)
                            } else {
                                chats.remove(item)
                            }
                        }
                        onComplete(chats)
                    }
                }
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
                            if (documentChange.type != DocumentChange.Type.REMOVED) {
                                chats.add(item)
                            } else {
                                chats.remove(item)
                            }
                        }
                        onComplete(chats)
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

        val projectsIds = ArrayList<String>()
        database.collection(KEY_COLLECTION_USERS)
            .document(manager.getString(KEY_USER_ID)!!).collection(KEY_COLLECTION_PROJECTS)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                } else {
                    if (value != null) {
                        for (documentChange in value.documentChanges) {

                            var doc = documentChange.document
                            projectsIds.add(doc.getString(KEY_PROJECT_ID)!!)
                            getProjectsByIds(projectsIds, liveData, onSuccess)
                        }
                        onSuccess()
                    }
                }
            }

    }

    fun getProjectsByIds(
        ids: ArrayList<String>,
        liveData: MutableLiveData<ArrayList<Project>>,
        onSuccess: () -> Unit
    ) {
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
                    owner = "${manager.getString(KEY_USER_NAME)!!} ${
                        manager.getString(
                            KEY_USER_SURNAME
                        )
                    }"
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

    fun getFeedbacks(project: Project, onSuccess: (ArrayList<UserFeedback>) -> Unit) {
        val feedbacks = arrayListOf<UserFeedback>()
        database.collection(KEY_COLLECTION_PROJECTS).document(project.id).collection(
            KEY_COLLECTION_FEEDBACK
        ).addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            } else {
                if (value != null) {
                    for (documentChange in value.documentChanges) {
                        var doc = documentChange.document
                        var userId = doc.getString("userId")!!
                        var name = doc.getString("name")!!
                        var surname = doc.getString("surname")!!
                        var lastName = doc.getString("lastName")!!
                        var age = doc.getString("age")!!
                        var sex = doc.getString("sex")!!
                        var uni = doc.getString("uni")!!
                        var brief = doc.getString("brief")!!
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
                        if (documentChange.type == DocumentChange.Type.REMOVED) {
                            feedbacks.remove(feedBack)
                        } else {

                            if (feedBack !in feedbacks) {
                                feedbacks.add(feedBack)
                            }
                        }

                    }
                    onSuccess(feedbacks)
                }
            }
        }
    }

    fun feedback(project: Project, brief: String, onFail: () -> Unit) {
        val userFeedback = UserFeedback(
            userId = manager.getString(KEY_USER_ID)!!,
            name = manager.getString(KEY_USER_NAME)!!,
            surname = manager.getString(KEY_USER_SURNAME)!!,
            lastName = manager.getString(KEY_USER_LASTNAME)!!,
            age = manager.getString(KEY_USER_AGE)!!,
            sex = manager.getString(KEY_USER_MALE)!!,
            uni = manager.getString(KEY_USER_UNI)!!,
            brief = brief
        )
        database.collection(KEY_COLLECTION_PROJECTS).document(project.id).collection(
            KEY_COLLECTION_FEEDBACK
        ).whereEqualTo(KEY_FEEDBACK_USER_ID, userFeedback.userId).get().addOnCompleteListener {
            var documents = it.result.documents
            if (documents.size == 0) {
                database.collection(KEY_COLLECTION_PROJECTS).document(project.id)
                    .collection(KEY_COLLECTION_FEEDBACK).add(userFeedback)
            } else {
                onFail()
            }
        }


    }

    fun feedBackReject(project: Project, userId: String) {
        database.collection(KEY_COLLECTION_PROJECTS).document(project.id).collection(
            KEY_COLLECTION_FEEDBACK
        ).whereEqualTo("userId", userId).get().addOnCompleteListener {
            it.result.documents[0].reference.delete()
        }
    }

    fun addStudentToProject(project: Project, userId: String, onFail: () -> Unit) {
        database.collection(KEY_COLLECTION_PROJECTS).document(project.id).collection(
            KEY_COLLECTION_FEEDBACK
        ).whereEqualTo("userId", userId).get().addOnCompleteListener {
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
            ).whereEqualTo(KEY_USER_ID, userId).get().addOnCompleteListener {
                var documents = it.result.documents
                if (documents.size == 0) {
                    database.collection(KEY_COLLECTION_PROJECTS).document(project.id).collection(
                        KEY_COLLECTION_MEMBERS
                    ).add(projectMember).addOnCompleteListener {
                        database.collection(KEY_COLLECTION_USERS).document(userId)
                            .collection(KEY_COLLECTION_PROJECTS).document(project.id)
                            .set(studentProject)
                    }
                } else {
                    onFail()
                }
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

    fun signOut(onComplete: () -> Unit) {
        auth.signOut()
        manager.clear()
        onComplete()
    }

    fun addUserToTeam(
        email: String,
        teamName: String,
        teamId: String,
        onComplete: (name: String, surname: String) -> Unit,
        onFail: () -> Unit
    ) {
        database.collection(KEY_COLLECTION_USERS).whereEqualTo(KEY_USER_EMAIL, email).get()
            .addOnSuccessListener {
                if (it.documents.size > 0) {
                    it.documents[0].reference.get().addOnCompleteListener {
                        var doc = it.result
                        if(doc.getString(KEY_USER_TYPE) == KEY_STUDENT){
                            var id = doc.getString(KEY_USER_ID)!!
                            var name = doc.getString(KEY_USER_NAME)!!
                            var surname = doc.getString(KEY_USER_SURNAME)!!
                            var uni = doc.getString(KEY_USER_UNI)!!
                            var teamMember = TeamMember(
                                id = id,
                                name = name,
                                surname = surname,
                                uni = uni
                            )
                            var studentTeam = StudentTeam(
                                id = teamId,
                                name = teamName
                            )
                            database.collection(KEY_COLLECTION_TEAMS).document(teamId).collection(
                                KEY_COLLECTION_MEMBERS
                            ).whereEqualTo(KEY_USER_ID, id).get().addOnCompleteListener {
                                var documents = it.result.documents
                                if (documents.size == 0) {
                                    database.collection(KEY_COLLECTION_USERS).document(id).collection(
                                        KEY_COLLECTION_TEAMS
                                    ).add(studentTeam).addOnSuccessListener {
                                        database.collection(KEY_COLLECTION_TEAMS).document(teamId)
                                            .collection(
                                                KEY_COLLECTION_MEMBERS
                                            ).add(teamMember).addOnSuccessListener {
                                                onComplete(name, surname)
                                            }
                                    }
                                } else {
                                    onFail()
                                }

                            }
                        }else{
                            onFail()
                        }
                    }
                } else {
                    onFail()
                }

            }
            .addOnFailureListener {
                onFail()
            }
    }

    fun getOwnerTeamId(teamId: String, onComplete: (teamID: String) -> Unit) {
        database.collection(KEY_COLLECTION_TEAMS).document(teamId).get().addOnSuccessListener {
            onComplete(it.get(KEY_OWNER_TEAM_ID)!! as String)
        }
    }

    fun delProjectChatMember(
        userId: String,
        projectId: String,
        onSuccess: () -> Unit,
        onFail: () -> Unit
    ) {
        database.collection(KEY_COLLECTION_PROJECTS).document(projectId).get()
            .addOnSuccessListener {
                var ownerId = it.getString(KEY_CREATOR_ID)
                if (manager.getString(KEY_USER_ID) == ownerId) {
                    database.collection(KEY_COLLECTION_PROJECTS).document(projectId).collection(
                        KEY_COLLECTION_MEMBERS
                    ).whereEqualTo(KEY_USER_ID, userId).get().addOnCompleteListener {
                        it.result.documents[0].reference.delete().addOnSuccessListener {
                            database.collection(KEY_COLLECTION_USERS).document(userId).collection(
                                KEY_COLLECTION_PROJECTS
                            ).document(projectId).delete().addOnSuccessListener {
                                database.collection(KEY_COLLECTION_USERS).document(userId)
                                    .collection(
                                        KEY_COLLECTION_PROJECT_CHAT
                                    ).document(projectId).collection(
                                    KEY_COLLECTION_MESSAGES
                                ).get().addOnSuccessListener {
                                    it.documents.forEach {
                                        it.reference.delete()
                                    }
                                    onSuccess()
                                }
                            }
                        }
                    }
                } else {
                    onFail()
                }
            }
    }

    fun delTeamChatMember(
        userId: String,
        teamId: String,
        onSuccess: () -> Unit,
        onFail: () -> Unit
    ) {
        database.collection(KEY_COLLECTION_TEAMS).document(teamId).get().addOnSuccessListener {
            val ownerId = it.getString(KEY_OWNER_TEAM_ID)
            if (manager.getString(KEY_USER_ID) == ownerId) {
                database.collection(KEY_COLLECTION_TEAMS).document(teamId).collection(
                    KEY_COLLECTION_MEMBERS
                ).whereEqualTo(KEY_USER_ID, userId).get().addOnSuccessListener {
                    it.documents[0].reference.delete().addOnSuccessListener {
                        database.collection(KEY_COLLECTION_USERS).document(userId).collection(
                            KEY_COLLECTION_TEAMS
                        ).whereEqualTo(KEY_USER_ID, teamId).get().addOnSuccessListener {
                            it.documents[0].reference.delete().addOnSuccessListener {
                                database.collection(KEY_COLLECTION_USERS).document(userId)
                                    .collection(
                                        KEY_COLLECTION_TEAM_CHAT
                                    ).document(teamId).collection(
                                    KEY_COLLECTION_MESSAGES
                                ).get().addOnSuccessListener {
                                    it.documents.forEach {
                                        it.reference.delete()
                                    }
                                    onSuccess()
                                }
                            }
                        }
                    }
                }
            } else {
                onFail()
            }
        }
    }

    fun getProjectMembersChat(id: String, onComplete: (ArrayList<ProjectMember>) -> Unit) {
        var members = arrayListOf<ProjectMember>()
        database.collection(KEY_COLLECTION_PROJECTS).document(id)
            .collection(KEY_COLLECTION_MEMBERS).addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                } else {
                    if (value != null) {
                        for (documentChange in value.documentChanges) {
                            var doc = documentChange.document
                            var id = doc.get(KEY_USER_ID)!! as String
                            var owner = doc.get(KEY_OWNER)!! as String
                            var member = ProjectMember(
                                id = id,
                                owner = owner
                            )
                            if(documentChange.type != DocumentChange.Type.REMOVED){
                                members.add(member)
                            }else{
                                members.remove(member)
                            }
                        }
                        onComplete(members)
                    }
                }

            }
    }

    fun getTeamMembersChat(id: String, onComplete: (members: ArrayList<TeamMember>) -> Unit) {
        var members = arrayListOf<TeamMember>()
        database.collection(KEY_COLLECTION_TEAMS).document(id)
            .collection(KEY_COLLECTION_MEMBERS).addSnapshotListener {value,error ->
                if(error!=null){
                    return@addSnapshotListener
                }else{
                    if(value!=null){
                        for(documentChange in value.documentChanges){
                            var doc = documentChange.document
                            var id = doc.get(KEY_USER_ID)!! as String
                            var name = doc.get(KEY_USER_NAME)!! as String
                            var surname = doc.get(KEY_USER_SURNAME)!! as String
                            var uni = doc.get(KEY_USER_UNI)!! as String
                            var member = TeamMember(
                                id = id,
                                name = name,
                                surname = surname,
                                uni = uni
                            )
                            if(documentChange.type != DocumentChange.Type.REMOVED){
                                members.add(member)
                            }else{
                                members.remove(member)
                            }
                        }
                        onComplete(members)
                    }
                }
            }
    }
}