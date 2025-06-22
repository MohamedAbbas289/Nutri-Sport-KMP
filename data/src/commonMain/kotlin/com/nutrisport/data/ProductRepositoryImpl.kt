package com.nutrisport.data

import com.nutrisport.data.domain.ProductRepository
import com.nutrisport.shared.domain.Product
import com.nutrisport.shared.domain.ProductCategory
import com.nutrisport.shared.util.RequestState
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest

class ProductRepositoryImpl : ProductRepository {
    override fun getCurrentUserId(): String? {
        return Firebase.auth.currentUser?.uid
    }

    override fun readDiscountedProducts(): Flow<RequestState<List<Product>>> = channelFlow {
        try {
            val userId = getCurrentUserId()
            if (userId != null) {
                val database = Firebase.firestore
                database.collection(collectionPath = "products")
                    .where { "isDiscounted" equalTo true }
                    .snapshots
                    .collectLatest { query ->
                        val products = query.documents.map { document ->
                            Product(
                                id = document.id,
                                title = document.get(field = "title"),
                                createdAt = document.get(field = "createdAt"),
                                description = document.get(field = "description"),
                                thumbnail = document.get(field = "thumbnail"),
                                category = document.get(field = "category"),
                                flavors = document.get(field = "flavors"),
                                weight = document.get(field = "weight"),
                                price = document.get(field = "price"),
                                isPopular = document.get(field = "isPopular"),
                                isDiscounted = document.get(field = "isDiscounted"),
                                isNew = document.get(field = "isNew")
                            )
                        }
                        send(RequestState.Success(products.map { it.copy(title = it.title.uppercase()) }))
                    }
            } else {
                send(RequestState.Error("User is not available."))
            }
        } catch (e: Exception) {
            send(RequestState.Error("Error while reading products: ${e.message}"))
        }
    }

    override fun readNewProducts(): Flow<RequestState<List<Product>>> = channelFlow {
        try {
            val userId = getCurrentUserId()
            if (userId != null) {
                val database = Firebase.firestore
                database.collection(collectionPath = "products")
                    .where { "isNew" equalTo true }
                    .snapshots
                    .collectLatest { query ->
                        val products = query.documents.map { document ->
                            Product(
                                id = document.id,
                                title = document.get(field = "title"),
                                createdAt = document.get(field = "createdAt"),
                                description = document.get(field = "description"),
                                thumbnail = document.get(field = "thumbnail"),
                                category = document.get(field = "category"),
                                flavors = document.get(field = "flavors"),
                                weight = document.get(field = "weight"),
                                price = document.get(field = "price"),
                                isPopular = document.get(field = "isPopular"),
                                isDiscounted = document.get(field = "isDiscounted"),
                                isNew = document.get(field = "isNew")
                            )
                        }
                        send(RequestState.Success(products.map { it.copy(title = it.title.uppercase()) }))
                    }
            } else {
                send(RequestState.Error("User is not available."))
            }
        } catch (e: Exception) {
            send(RequestState.Error("Error while reading products: ${e.message}"))
        }
    }

    override fun readProductByIdFlow(id: String): Flow<RequestState<Product>> = channelFlow {
        try {
            val userId = getCurrentUserId()
            if (userId != null) {
                val database = Firebase.firestore
                database.collection(collectionPath = "products")
                    .document(documentPath = id)
                    .snapshots
                    .collectLatest { document ->
                        if (document.exists) {
                            val product = Product(
                                id = document.id,
                                title = document.get(field = "title"),
                                createdAt = document.get(field = "createdAt"),
                                description = document.get(field = "description"),
                                thumbnail = document.get(field = "thumbnail"),
                                category = document.get(field = "category"),
                                flavors = document.get(field = "flavors"),
                                weight = document.get(field = "weight"),
                                price = document.get(field = "price"),
                                isPopular = document.get(field = "isPopular"),
                                isDiscounted = document.get(field = "isDiscounted"),
                                isNew = document.get(field = "isNew")
                            )
                            send(RequestState.Success(product.copy(title = product.title.uppercase())))
                        } else {
                            send(RequestState.Error("Product is not available."))
                        }

                    }
            } else {
                send(RequestState.Error("User is not available."))
            }
        } catch (e: Exception) {
            send(RequestState.Error("Error while reading the product: ${e.message}"))
        }
    }

    override fun readProductsByIdsFlow(ids: List<String>): Flow<RequestState<List<Product>>> =
        channelFlow {
            try {
                val userId = getCurrentUserId()
                if (userId != null) {
                    val database = Firebase.firestore

                    val allProducts = mutableListOf<Product>()
                    val chunks = ids.chunked(10)
                    val productCollection = database.collection(collectionPath = "products")
                    chunks.forEachIndexed { index, chunk ->
                        productCollection
                            .where { "id" inArray chunk }
                            .snapshots
                            .collectLatest { query ->
                                val products = query.documents.map { document ->
                                    Product(
                                        id = document.id,
                                        title = document.get(field = "title"),
                                        createdAt = document.get(field = "createdAt"),
                                        description = document.get(field = "description"),
                                        thumbnail = document.get(field = "thumbnail"),
                                        category = document.get(field = "category"),
                                        flavors = document.get(field = "flavors"),
                                        weight = document.get(field = "weight"),
                                        price = document.get(field = "price"),
                                        isPopular = document.get(field = "isPopular"),
                                        isDiscounted = document.get(field = "isDiscounted"),
                                        isNew = document.get(field = "isNew")
                                    )
                                }
                                allProducts.addAll(products)
                                if (index == chunks.lastIndex) {
                                    send(RequestState.Success(allProducts.map { it.copy(title = it.title.uppercase()) }))
                                }
                            }
                    }
                } else {
                    send(RequestState.Error("User is not available."))
            }
        } catch (e: Exception) {
            send(RequestState.Error("Error while reading the product: ${e.message}"))
        }
    }

    override fun readProductsByCategoryFlow(category: ProductCategory): Flow<RequestState<List<Product>>> =
        channelFlow {
            try {
                val userId = getCurrentUserId()
                if (userId != null) {
                    val database = Firebase.firestore
                    database.collection(collectionPath = "products")
                        .where { "category" equalTo category.name }
                        .snapshots
                        .collectLatest { query ->
                            val products = query.documents.map { document ->
                                Product(
                                    id = document.id,
                                    title = document.get(field = "title"),
                                    createdAt = document.get(field = "createdAt"),
                                    description = document.get(field = "description"),
                                    thumbnail = document.get(field = "thumbnail"),
                                    category = document.get(field = "category"),
                                    flavors = document.get(field = "flavors"),
                                    weight = document.get(field = "weight"),
                                    price = document.get(field = "price"),
                                    isPopular = document.get(field = "isPopular"),
                                    isDiscounted = document.get(field = "isDiscounted"),
                                    isNew = document.get(field = "isNew")
                                )
                            }
                            send(RequestState.Success(products.map { it.copy(title = it.title.uppercase()) }))
                        }
                } else {
                    send(RequestState.Error("User is not available."))
                }
            } catch (e: Exception) {
                send(RequestState.Error("Error while reading the product: ${e.message}"))
            }
        }
}