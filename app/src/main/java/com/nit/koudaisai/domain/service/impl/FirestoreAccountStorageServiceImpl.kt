package com.nit.koudaisai.domain.service.impl

import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.nit.koudaisai.domain.model.KoudaisaiUser
import com.nit.koudaisai.domain.model.ReserveCapacity
import com.nit.koudaisai.domain.service.AccountStorageService
import com.nit.koudaisai.utils.KoudaisaiDay
import javax.inject.Inject

class FirestoreAccountStorageServiceImpl @Inject constructor() : AccountStorageService {

    private var listenerRegistrationList: MutableList<ListenerRegistration> = mutableListOf()
    override fun getUser(
        userId: String,
        onError: (Throwable) -> Unit,
        onSuccess: (KoudaisaiUser) -> Unit
    ) {
        Firebase.firestore.collection(USER_COLLECTION)
            .document(userId)
            .get()
            .addOnFailureListener(onError)
            .addOnSuccessListener { result ->
                val user = result.toObject<KoudaisaiUser>()
                user?.let(onSuccess)
            }
    }

    override fun addUserListener(
        userId: String,
        onError: (Throwable) -> Unit,
        onSuccess: (KoudaisaiUser) -> Unit
    ) {
        val listenerRegistration = Firebase.firestore.collection(USER_COLLECTION)
            .document(userId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    onError(e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val user = snapshot.toObject<KoudaisaiUser>()
                    user?.let(onSuccess)
                }
            }
        listenerRegistrationList.add(listenerRegistration)
    }

    override fun removeListener() {
        for (listener in listenerRegistrationList) {
            listener.remove()
        }
    }

    override fun saveUser(uid: String, user: KoudaisaiUser, onResult: (Throwable?) -> Unit) {
        Firebase.firestore.collection(USER_COLLECTION)
            .document(uid)
            .set(user)
            .addOnCompleteListener { onResult(it.exception) }
    }

    override fun saveSubUser(
        user: KoudaisaiUser,
        onSuccess: (String) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        Firebase.firestore.collection(USER_COLLECTION)
            .add(user)
            .addOnSuccessListener {
                onSuccess(it.id)
            }
            .addOnFailureListener { onError(it) }
    }

    override fun updateUser(uid: String, user: KoudaisaiUser, onResult: (Throwable?) -> Unit) {
        Firebase.firestore.collection(USER_COLLECTION)
            .document(uid)
            .set(user, SetOptions.merge())
            .addOnCompleteListener { onResult(it.exception) }
    }

    override fun deleteUser(userId: String, onResult: (Throwable?) -> Unit) {
        Firebase.firestore.collection(USER_COLLECTION)
            .document(userId)
            .delete()
            .addOnCompleteListener { onResult(it.exception) }
    }

    override fun getDayCapacity(
        day: KoudaisaiDay,
        onError: (Throwable) -> Unit,
        onSuccess: (ReserveCapacity) -> Unit
    ) {
        val docId = when (day) {
            KoudaisaiDay.DayOne -> DAY1
            KoudaisaiDay.DayTwo -> DAY2
        }
        Firebase.firestore.collection(CAPACITY_COLLECTION)
            .document(docId)
            .get()
            .addOnFailureListener(onError)
            .addOnSuccessListener { result ->
                val capacity = result.toObject<ReserveCapacity>()
                capacity?.let(onSuccess)
            }
    }

    override fun setDayCapacity(
        day: KoudaisaiDay,
        totalParticipants: Int,
        onResult: (Throwable?) -> Unit
    ) {
        val data = hashMapOf(TOTAL_PARTICIPANTS_KEY to totalParticipants)
        val docId = when (day) {
            KoudaisaiDay.DayOne -> DAY1
            KoudaisaiDay.DayTwo -> DAY2
        }
        Firebase.firestore.collection(CAPACITY_COLLECTION)
            .document(docId)
            .set(data, SetOptions.merge())
            .addOnCompleteListener { onResult(it.exception) }
    }

    override fun incrementTotalParticipants(day: KoudaisaiDay, onResult: (Throwable?) -> Unit) {
        val docId = when (day) {
            KoudaisaiDay.DayOne -> DAY1
            KoudaisaiDay.DayTwo -> DAY2
        }
        val db = Firebase.firestore
        val docRef = db.collection(CAPACITY_COLLECTION).document(docId)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            val newTotalParticipants = snapshot.getDouble(TOTAL_PARTICIPANTS_KEY)!! + 1
            transaction.update(docRef, TOTAL_PARTICIPANTS_KEY, newTotalParticipants)
        }.addOnCompleteListener { onResult(it.exception) }
    }

    override fun decrementTotalParticipants(day: KoudaisaiDay, onResult: (Throwable?) -> Unit) {
        val docId = when (day) {
            KoudaisaiDay.DayOne -> DAY1
            KoudaisaiDay.DayTwo -> DAY2
        }
        val db = Firebase.firestore
        val docRef = db.collection(CAPACITY_COLLECTION).document(docId)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            val newTotalParticipants = snapshot.getDouble(TOTAL_PARTICIPANTS_KEY)!! - 1
            transaction.update(docRef, TOTAL_PARTICIPANTS_KEY, newTotalParticipants)
        }.addOnCompleteListener { onResult(it.exception) }
    }

    override fun decrementTotalParticipants(
        day: KoudaisaiDay,
        count: Int,
        onResult: (Throwable?) -> Unit
    ) {
        val docId = when (day) {
            KoudaisaiDay.DayOne -> DAY1
            KoudaisaiDay.DayTwo -> DAY2
        }
        val db = Firebase.firestore
        val docRef = db.collection(CAPACITY_COLLECTION).document(docId)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            val newTotalParticipants = snapshot.getDouble(TOTAL_PARTICIPANTS_KEY)!! - count
            transaction.update(docRef, TOTAL_PARTICIPANTS_KEY, newTotalParticipants)
        }.addOnCompleteListener { onResult(it.exception) }
    }

    companion object {
        const val USER_COLLECTION = "TestKoudaisaiUser"
        const val CAPACITY_COLLECTION = "TestReserveCapacity"
        const val TOTAL_PARTICIPANTS_KEY = "totalParticipants"
        const val DAY1 = "day1"
        const val DAY2 = "day2"
    }
}