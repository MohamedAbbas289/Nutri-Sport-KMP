package com.nutrisport.di

import com.nutrisport.admin_panel.AdminPanelViewModel
import com.nutrisport.auth.AuthViewModel
import com.nutrisport.cart.CartViewModel
import com.nutrisport.category_search.CategoryScreenViewModel
import com.nutrisport.checkout.CheckoutViewModel
import com.nutrisport.checkout.domain.PaypalApi
import com.nutrisport.data.AdminRepositoryImpl
import com.nutrisport.data.CustomerRepositoryImpl
import com.nutrisport.data.OrderRepositoryImpl
import com.nutrisport.data.ProductRepositoryImpl
import com.nutrisport.data.domain.AdminRepository
import com.nutrisport.data.domain.CustomerRepository
import com.nutrisport.data.domain.OrderRepository
import com.nutrisport.data.domain.ProductRepository
import com.nutrisport.details.DetailsViewModel
import com.nutrisport.home.HomeGraphViewModel
import com.nutrisport.manage_product.ManageProductViewModel
import com.nutrisport.payment_completed.PaymentViewModel
import com.nutrisport.product_overview.ProductsOverviewViewModel
import com.nutrisport.profile.ProfileViewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sharedModule = module {
    single<CustomerRepository> { CustomerRepositoryImpl() }
    single<AdminRepository> { AdminRepositoryImpl() }
    single<ProductRepository> { ProductRepositoryImpl() }
    // the koin module is smart enough to get that customer repository we provided in the get() function
    single<OrderRepository> { OrderRepositoryImpl(customerRepository = get()) }
    single<PaypalApi> { PaypalApi() }
    viewModelOf(::AuthViewModel)
    viewModelOf(::HomeGraphViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::ManageProductViewModel)
    viewModelOf(::AdminPanelViewModel)
    viewModelOf(::ProductsOverviewViewModel)
    viewModelOf(::DetailsViewModel)
    viewModelOf(::CartViewModel)
    viewModelOf(::CategoryScreenViewModel)
    viewModelOf(::CheckoutViewModel)
    viewModelOf(::PaymentViewModel)
}

expect val targetModule: Module

fun initializeKoin(
    config: (KoinApplication.() -> Unit)? = null
) {
    startKoin {
        config?.invoke(this)
        modules(sharedModule, targetModule)
    }
}