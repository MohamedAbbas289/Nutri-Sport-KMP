package com.nutrisport.data

import com.nutrisport.data.domain.AdminRepository
import com.nutrisport.shared.domain.Product
import com.nutrisport.shared.util.RequestState
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.storage.File
import dev.gitlive.firebase.storage.storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withTimeout
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AdminRepositoryImpl : AdminRepository {
    override fun getCurrentUserId(): String? {
        return Firebase.auth.currentUser?.uid
    }

    override suspend fun createNewProduct(
        product: Product,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val currentUserId = getCurrentUserId()
            if (currentUserId != null) {
                val firestore = Firebase.firestore
                val productCollection = firestore.collection(collectionPath = "products")
                productCollection.document(product.id)
                    .set(product.copy(title = product.title.lowercase()))
                onSuccess()
            } else {
                onError("User is not available.")
            }
        } catch (e: Exception) {
            onError("Error while creating a new product: ${e.message}")
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun uploadImageToStorage(file: File): String? {
        return if (getCurrentUserId() != null) {
            val storage = Firebase.storage.reference
            val imagePath = storage.child(path = "images/${Uuid.random().toHexString()}")
            try {
                withTimeout(timeMillis = 20000L) {
                    imagePath.putFile(file)
                    imagePath.getDownloadUrl()
                }
            } catch (e: Exception) {
                null
            }
        } else null
    }

    override suspend fun deleteImageFromStorage(
        downloadUrl: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val storagePath = extractFirebaseStoragePath(downloadUrl)
            if (storagePath != null) {
                Firebase.storage.reference(storagePath).delete()
                onSuccess()
            } else {
                onError("Storage Path is null.")
            }
        } catch (e: Exception) {
            onError("Error while deleting image: ${e.message}")
        }
    }

    override fun readLastTenProducts(): Flow<RequestState<List<Product>>> = channelFlow {
        try {
            val userId = getCurrentUserId()
            if (userId != null) {
                val database = Firebase.firestore
                database.collection(collectionPath = "products")
                    .orderBy("createdAt", Direction.DESCENDING)
                    .limit(10)
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

    override suspend fun readProductById(id: String): RequestState<Product> {
        return try {
            val userId = getCurrentUserId()
            if (userId != null) {
                val database = Firebase.firestore
                val productDocument = database.collection(collectionPath = "products")
                    .document(documentPath = id)
                    .get()
                if (productDocument.exists) {
                    val product = Product(
                        id = productDocument.id,
                        title = productDocument.get(field = "title"),
                        createdAt = productDocument.get(field = "createdAt"),
                        description = productDocument.get(field = "description"),
                        thumbnail = productDocument.get(field = "thumbnail"),
                        category = productDocument.get(field = "category"),
                        flavors = productDocument.get(field = "flavors"),
                        weight = productDocument.get(field = "weight"),
                        price = productDocument.get(field = "price"),
                        isPopular = productDocument.get(field = "isPopular"),
                        isDiscounted = productDocument.get(field = "isDiscounted"),
                        isNew = productDocument.get(field = "isNew")
                    )
                    RequestState.Success(product.copy(title = product.title.uppercase()))
                } else {
                    RequestState.Error("Selected product not found.")
                }
            } else {
                RequestState.Error("User is not available.")
            }
        } catch (e: Exception) {
            RequestState.Error("Error while reading product: ${e.message}")
        }
    }

    override suspend fun updateProductThumbnail(
        productId: String,
        downloadUrl: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val userId = getCurrentUserId()
            if (userId != null) {
                val database = Firebase.firestore
                val productCollection = database.collection(collectionPath = "products")
                val existingProduct = productCollection
                    .document(documentPath = productId)
                    .get()
                if (existingProduct.exists) {
                    productCollection.document(productId)
                        .update("thumbnail" to downloadUrl)
                    onSuccess()
                } else {
                    onError("Selected product not found.")
                }
            } else {
                onError("User is not available.")
            }
        } catch (e: Exception) {
            onError("Error while updating thumbnail image: ${e.message}")
        }
    }

    override suspend fun updateProduct(
        product: Product,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val userId = getCurrentUserId()
            if (userId != null) {
                val database = Firebase.firestore
                val productCollection = database.collection(collectionPath = "products")
                val existingProduct = productCollection
                    .document(documentPath = product.id)
                    .get()
                if (existingProduct.exists) {
                    productCollection.document(product.id)
                        .update(product.copy(title = product.title.lowercase()))
                    onSuccess()
                } else {
                    onError("Selected product not found.")
                }
            } else {
                onError("User is not available.")
            }
        } catch (e: Exception) {
            onError("Error while updating thumbnail image: ${e.message}")
        }
    }

    override suspend fun deleteProduct(
        productId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val userId = getCurrentUserId()
            if (userId != null) {
                val database = Firebase.firestore
                val productCollection = database.collection(collectionPath = "products")
                val existingProduct = productCollection
                    .document(documentPath = productId)
                    .get()
                if (existingProduct.exists) {
                    productCollection.document(productId)
                        .delete()
                    onSuccess()
                } else {
                    onError("Selected product not found.")
                }
            } else {
                onError("User is not available.")
            }
        } catch (e: Exception) {
            onError("Error while deleting product: ${e.message}")
        }
    }

    override fun searchProductByTitle(searchQuery: String): Flow<RequestState<List<Product>>> =
        channelFlow {
            try {
                val userId = getCurrentUserId()
                if (userId != null) {
                    val database = Firebase.firestore

                    val queryText = searchQuery.trim().lowercase()
                    val endText = queryText + "\uf8ff"

                    database.collection(collectionPath = "products")
//                    .orderBy("title")
//                    .startAt(queryText)
//                    .endAt(endText)
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
                            send(
                                RequestState.Success(
                                products
                                    .filter { it.title.contains(queryText) }
                                    .map { it.copy(title = it.title.uppercase()) }
                            ))
                        }
                } else {
                    send(RequestState.Error("User is not available."))
                }
            } catch (e: Exception) {
                send(RequestState.Error("Error while searching product: ${e.message}"))
            }
        }

    private fun extractFirebaseStoragePath(downloadUrl: String): String? {
        val startIndex = downloadUrl.indexOf("/o/") + 3
        if (startIndex < 3) null

        val endIndex = downloadUrl.indexOf("?", startIndex)
        val encodedPath = if (endIndex != -1) {
            downloadUrl.substring(startIndex, endIndex)
        } else {
            downloadUrl.substring(startIndex)
        }
        return decodeFirebasePath(encodedPath)
    }

    private fun decodeFirebasePath(encodedPath: String): String {
        return encodedPath
            .replace("%2F", "/")
            .replace("%20", " ")
    }
}