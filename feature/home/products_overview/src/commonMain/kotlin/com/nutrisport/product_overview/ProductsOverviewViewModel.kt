package com.nutrisport.product_overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutrisport.data.domain.ProductRepository
import com.nutrisport.shared.util.RequestState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ProductsOverviewViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {
    val products = combine(
        productRepository.readNewProducts(),
        productRepository.readDiscountedProducts()
    ) { newProducts, discountedProducts ->
        when {
            newProducts.isSuccess() && discountedProducts.isSuccess() -> {
                RequestState.Success(newProducts.getSuccessData() + discountedProducts.getSuccessData())
            }

            newProducts.isError() -> newProducts
            discountedProducts.isError() -> discountedProducts
            else -> RequestState.Loading
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RequestState.Loading
    )
}