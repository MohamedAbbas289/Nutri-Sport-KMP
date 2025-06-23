package com.nutrisport.data

import com.nutrisport.data.domain.CustomerRepository
import com.nutrisport.data.domain.OrderRepository
import com.nutrisport.shared.domain.Order
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore

class OrderRepositoryImpl(
    private val customerRepository: CustomerRepository
) : OrderRepository {
    override fun getCurrentUserId(): String? {
        return Firebase.auth.currentUser?.uid
    }

    override suspend fun createNewOrder(
        order: Order,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val userId = getCurrentUserId()
            if (userId != null) {
                val database = Firebase.firestore
                val orderCollection = database.collection("order")
                orderCollection.document(order.id)
                    .set(order)
                customerRepository.deleteAllCartItems(
                    onSuccess = {},
                    onError = {}
                )
                onSuccess()
            } else {
                onError("User is not available.")
            }
        } catch (e: Exception) {
            onError("Error while adding item to cart: ${e.message}")
        }
    }
}