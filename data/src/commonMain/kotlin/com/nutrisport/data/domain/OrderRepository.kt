package com.nutrisport.data.domain

import com.nutrisport.shared.domain.Order

interface OrderRepository {
    fun getCurrentUserId(): String?

    suspend fun createNewOrder(
        order: Order,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    )
}