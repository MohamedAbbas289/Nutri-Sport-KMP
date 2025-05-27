package com.nutrisport.data.domain

import com.nutrisport.shared.util.RequestState
import dev.gitlive.firebase.auth.FirebaseUser

interface CustomerRepository {

    suspend fun createCustomer(
        user: FirebaseUser?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    fun getCurrentUserId(): String?

    suspend fun signOut(): RequestState<Unit>
}