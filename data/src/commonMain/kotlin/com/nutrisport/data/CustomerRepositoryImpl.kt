package com.nutrisport.data

import com.nutrisport.data.domain.CustomerRepository
import com.nutrisport.shared.domain.CartItem
import com.nutrisport.shared.domain.Customer
import com.nutrisport.shared.util.RequestState
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

class CustomerRepositoryImpl : CustomerRepository {
    override suspend fun createCustomer(
        user: FirebaseUser?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            if (user != null) {
                val customerCollection = Firebase.firestore.collection("customer")
                val customer = Customer(
                    id = user.uid,
                    firstName = user.displayName?.split(" ")?.firstOrNull() ?: "Unknown",
                    lastName = user.displayName?.split(" ")?.lastOrNull() ?: "Unknown",
                    email = user.email ?: "Unknown"
                )

                val customerExists = customerCollection.document(user.uid).get().exists

                if (customerExists) {
                    onSuccess()
                } else {
                    customerCollection.document(user.uid).set(customer)
                    customerCollection.document(user.uid)
                        .collection("privateData")
                        .document("role")
                        .set(mapOf("isAdmin" to false))
                    onSuccess()
                }
            } else {
                onError("User is not available.")
            }
        } catch (e: Exception) {
            onError("Error while creating a customer: ${e.message}")
        }
    }

    override fun getCurrentUserId(): String? {
        return Firebase.auth.currentUser?.uid
    }

    override fun readCustomerFlow(): Flow<RequestState<Customer>> = channelFlow {
        try {
            val userId = getCurrentUserId()
            if (userId != null) {
                val database = Firebase.firestore
                database.collection("customer")
                    .document(userId)
                    .snapshots
                    .collect { document ->
                        if (document.exists) {
                            val privateDataDocument = database.collection("customer")
                                .document(userId)
                                .collection("privateData")
                                .document("role")
                                .get()
                            val customer = Customer(
                                id = document.id,
                                firstName = document.get("firstName"),
                                lastName = document.get("lastName"),
                                email = document.get("email"),
                                city = document.get("city"),
                                postalCode = document.get("postalCode"),
                                address = document.get("address"),
                                phoneNumber = document.get("phoneNumber"),
                                cart = document.get("cart"),
                                isAdmin = privateDataDocument.get("isAdmin")
                            )
                            send(RequestState.Success(customer))
                        } else {
                            send(RequestState.Error("Customer does not exist."))
                        }
                    }
            } else {
                send(RequestState.Error("User is not available."))
            }
        } catch (e: Exception) {
            send(RequestState.Error("Error while reading customer: ${e.message}"))
        }
    }

    override suspend fun updateCustomer(
        customer: Customer,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val userId = getCurrentUserId()
            if (userId != null) {
                val firestore = Firebase.firestore
                val customerCollection = firestore.collection("customer")

                val existingCustomer = customerCollection
                    .document(customer.id)
                    .get()
                if (existingCustomer.exists) {
                    customerCollection.document(customer.id)
                        .update(
                            "firstName" to customer.firstName,
                            "lastName" to customer.lastName,
                            "city" to customer.city,
                            "postalCode" to customer.postalCode,
                            "address" to customer.address,
                            "phoneNumber" to customer.phoneNumber
                        )
                    onSuccess()
                } else {
                    onError("Customer not found.")
                }
            } else {
                onError("User is not available.")
            }
        } catch (e: Exception) {
            onError("Error while updating customer information: ${e.message}")
        }
    }

    override suspend fun addItemToCart(
        cartItem: CartItem,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val userId = getCurrentUserId()
            if (userId != null) {
                val database = Firebase.firestore
                val customerCollection = database.collection("customer")

                val existingCustomer = customerCollection
                    .document(userId)
                    .get()
                if (existingCustomer.exists) {
                    val existingCartItem = existingCustomer.get<List<CartItem>>("cart")
                    val updatedCart = existingCartItem + cartItem
                    customerCollection.document(userId)
                        .set(
                            data = mapOf("cart" to updatedCart),
                            merge = true
                        )
                    onSuccess()
                } else {
                    onError("Customer not found.")
                }
            } else {
                onError("User is not available.")
            }
        } catch (e: Exception) {
            onError("Error while adding item to cart: ${e.message}")
        }
    }

    override suspend fun updateCartItemQuantity(
        id: String,
        quantity: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val userId = getCurrentUserId()
            if (userId != null) {
                val database = Firebase.firestore
                val customerCollection = database.collection("customer")

                val existingCustomer = customerCollection
                    .document(userId)
                    .get()
                if (existingCustomer.exists) {
                    val existingCartItem = existingCustomer.get<List<CartItem>>("cart")
                    val updatedCart = existingCartItem.map { cartItem ->
                        if (cartItem.id == id) {
                            cartItem.copy(quantity = quantity)
                        } else cartItem
                    }
                    customerCollection.document(userId)
                        .update(data = mapOf("cart" to updatedCart))
                    onSuccess()
                } else {
                    onError("Customer not found.")
                }
            } else {
                onError("User is not available.")
            }
        } catch (e: Exception) {
            onError("Error while adding item to cart: ${e.message}")
        }
    }

    override suspend fun deleteCartItem(
        id: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val userId = getCurrentUserId()
            if (userId != null) {
                val database = Firebase.firestore
                val customerCollection = database.collection("customer")

                val existingCustomer = customerCollection
                    .document(userId)
                    .get()
                if (existingCustomer.exists) {
                    val existingCartItem = existingCustomer.get<List<CartItem>>("cart")
                    val updatedCart = existingCartItem.filterNot { cartItem ->
                        cartItem.id == id
                    }
                    customerCollection.document(userId)
                        .update(data = mapOf("cart" to updatedCart))
                    onSuccess()
                } else {
                    onError("Customer not found.")
                }
            } else {
                onError("User is not available.")
            }
        } catch (e: Exception) {
            onError("Error while adding item to cart: ${e.message}")
        }
    }

    override suspend fun signOut(): RequestState<Unit> {
        return try {
            Firebase.auth.signOut()
            RequestState.Success(Unit)
        } catch (e: Exception) {
            RequestState.Error("Error while signing out: ${e.message}")
        }
    }
}