package com.nutrisport.data.domain

import com.nutrisport.shared.domain.CartItem
import com.nutrisport.shared.domain.Customer
import com.nutrisport.shared.util.RequestState
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {

    suspend fun createCustomer(
        user: FirebaseUser?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    fun getCurrentUserId(): String?

    fun readCustomerFlow(): Flow<RequestState<Customer>>

    suspend fun updateCustomer(
        customer: Customer,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    suspend fun addItemToCart(
        cartItem: CartItem,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    suspend fun updateCartItemQuantity(
        id: String,
        quantity: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    suspend fun deleteCartItem(
        id: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    suspend fun deleteAllCartItems(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    suspend fun signOut(): RequestState<Unit>
}