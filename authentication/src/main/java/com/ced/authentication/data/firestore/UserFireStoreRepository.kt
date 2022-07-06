package com.ced.authentication.data.firestore

import com.ced.authentication.data.entity.UserEntity
import com.ced.authentication.data.mapper.toDomain
import com.ced.authentication.data.mapper.toEntity
import com.ced.authentication.domain.model.User
import com.ced.authentication.domain.repository.UserRepository
import com.ced.commons.util.log.Logger
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Observable.create
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */
class UserFireStoreRepository @Inject constructor() :
    UserRepository {

    private val fireStore: FirebaseFirestore
        get() = FirebaseFirestore.getInstance()

    private val userRef by lazy {
        fireStore.collection("/user")
    }

    override fun findByEmail(email: String): Observable<User> {
        return create { emitter ->
            userRef
                .whereEqualTo("email", email).get()
                .addOnCompleteListener { task ->
                    if (!task.isComplete) return@addOnCompleteListener

                    val result = task.result?.toObjects(UserEntity::class.java)
                    if (result.isNullOrEmpty()) {
                        emitter.onError(IllegalArgumentException("Email not found."))
                    } else {
                        val user = result.first().toDomain()
                        emitter.onNext(user)
                    }
                }
        }
    }

    override fun findByNumber(number: String): Observable<User> {
        return create { emitter ->
            userRef
                .whereEqualTo("mobileNumber", number).get()
                .addOnCompleteListener { task ->
                    val result = task.result?.toObjects(UserEntity::class.java)
                    if (!task.isComplete) return@addOnCompleteListener

                    if (result.isNullOrEmpty()) {
                        emitter.onError(IllegalArgumentException("Mobile number not found."))
                    } else {
                        val user = result.first().toDomain()
                        emitter.onNext(user)
                    }
                }
        }
    }

    override fun add(user: User): Single<User> {
        return Single.create { emitter ->
            val doc = userRef.document("${user.id}")
            val entity = user.toEntity()

            doc.set(entity)
                .addOnCompleteListener { Logger.d(TAG, "Successfully added a user: '${user.id}'") }
                .addOnFailureListener { Logger.w(TAG, "Error while adding user with id: '${user.id}'", it) }

            emitter.onSuccess(user)
        }
    }

    companion object {
        private val TAG = UserFireStoreRepository::class.java.simpleName
    }
}