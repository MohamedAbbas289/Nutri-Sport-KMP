package com.nutrisport.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutrisport.data.domain.ProductRepository
import com.nutrisport.shared.util.RequestState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class DetailsViewModel(
    private val productRepository: ProductRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val product = productRepository.readProductByIdFlow(
        savedStateHandle.get<String>("id") ?: "0"
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RequestState.Loading
    )

    var selectedFlavor: String? by mutableStateOf(null)
        private set

    fun updateFlavor(value: String) {
        selectedFlavor = value
    }
}