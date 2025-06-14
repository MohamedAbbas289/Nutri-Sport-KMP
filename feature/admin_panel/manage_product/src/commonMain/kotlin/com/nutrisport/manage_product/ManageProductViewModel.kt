package com.nutrisport.manage_product

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutrisport.data.domain.AdminRepository
import com.nutrisport.shared.domain.Product
import com.nutrisport.shared.domain.ProductCategory
import com.nutrisport.shared.util.RequestState
import dev.gitlive.firebase.storage.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
data class ManageProductState(
    val id: String = Uuid.random().toHexString(),
    //val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val title: String = "",
    val description: String = "",
    val thumbnail: String = "thumbnail image",
    val category: ProductCategory = ProductCategory.Protein,
    val flavors: String = "",
    val weight: Int? = null,
    val price: Double = 0.0,
//    val isNew: Boolean = false,
//    val isPopular: Boolean = false,
//    val isDiscounted: Boolean = false
)

class ManageProductViewModel(
    private val adminRepository: AdminRepository
) : ViewModel() {
    var screenState by mutableStateOf(ManageProductState())
        private set

    var thumbnailUploaderState by mutableStateOf<RequestState<Unit>>(RequestState.Idle)
        private set

    val isFormValid: Boolean
        get() = screenState.title.isNotEmpty() &&
                screenState.description.isNotEmpty() &&
                screenState.thumbnail.isNotEmpty() &&
                screenState.price != 0.0

    fun updateId(value: String) {
        screenState = screenState.copy(id = value)
    }

//    fun updateCreatedAt(value: Long) {
//        screenState = screenState.copy(createdAt = value)
//    }

    fun updateTitle(value: String) {
        screenState = screenState.copy(title = value)
    }

    fun updateDescription(value: String) {
        screenState = screenState.copy(description = value)
    }

    fun updateThumbnail(value: String) {
        screenState = screenState.copy(thumbnail = value)
    }

    fun updateThumbnailUploaderState(value: RequestState<Unit>) {
        thumbnailUploaderState = value
    }

    fun updateCategory(value: ProductCategory) {
        screenState = screenState.copy(category = value)
    }

    fun updateFlavors(value: String) {
        screenState = screenState.copy(flavors = value)
    }

    fun updateWeight(value: Int?) {
        screenState = screenState.copy(weight = value)
    }

    fun updatePrice(value: Double) {
        screenState = screenState.copy(price = value)
    }

//    fun updateNew(value: Boolean) {
//        screenState = screenState.copy(isNew = value)
//    }
//
//    fun updatePopular(value: Boolean) {
//        screenState = screenState.copy(isPopular = value)
//    }
//
//    fun updateDiscounted(value: Boolean) {
//        screenState = screenState.copy(isDiscounted = value)
//    }

    fun createNewProduct(
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            adminRepository.createNewProduct(
                product = Product(
                    id = screenState.id,
                    title = screenState.title,
                    description = screenState.description,
                    thumbnail = screenState.thumbnail,
                    category = screenState.category.name,
                    flavors = screenState.flavors.split(","),
                    weight = screenState.weight,
                    price = screenState.price
                ),
                onSuccess = onSuccess,
                onError = onError
            )
        }
    }

    fun uploadThumbnailToStorage(
        file: File?,
        onSuccess: () -> Unit
    ) {
        if (file == null) {
            thumbnailUploaderState =
                RequestState.Error("File is null, Error while selecting image.")
            return
        }
        thumbnailUploaderState = RequestState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val downloadUrl = adminRepository.uploadImageToStorage(file)

                if (downloadUrl.isNullOrEmpty()) {
                    throw Exception("Failed to retrieve a download URL after the upload.")
                }

                onSuccess()
                updateThumbnailUploaderState(RequestState.Success(Unit))
                updateThumbnail(downloadUrl)
            } catch (e: Exception) {
                updateThumbnailUploaderState(
                    RequestState.Error("Error while uploading image: ${e.message}")
                )
            }
        }

    }

    fun deleteThumbnailFromStorage(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            adminRepository.deleteImageFromStorage(
                downloadUrl = screenState.thumbnail,
                onSuccess = {
                    updateThumbnail("")
                    updateThumbnailUploaderState(RequestState.Idle)
                    onSuccess()
                },
                onError = onError
            )
        }
    }
}