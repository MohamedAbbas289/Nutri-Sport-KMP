package com.nutrisport.shared.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileForm(
    modifier: Modifier = Modifier,
    firstName: String,
    onFirstNameChange: (String) -> Unit,
    lastName: String,
    onLastNameChange: (String) -> Unit,
    email: String,
    city: String,
    onCityChange: (String) -> Unit,
    postalCode: Int?,
    onPostalCodeChange: (Int?) -> Unit,
    address: String,
    onAddressChange: (String) -> Unit,
    phoneNumber: String?,
    onPhoneNumberChange: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = 24.dp,
                vertical = 12.dp
            )
            .imePadding()
            .verticalScroll(state = rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CustomTextField(
            placeHolder = "First Name",
            value = firstName,
            onValueChange = onFirstNameChange,
            error = firstName.length !in 3..50
        )
        CustomTextField(
            placeHolder = "Last Name",
            value = lastName,
            onValueChange = onLastNameChange,
            error = lastName.length !in 3..50
        )
        CustomTextField(
            placeHolder = "Email",
            value = email,
            onValueChange = {},
            enabled = false
        )
        CustomTextField(
            placeHolder = "City",
            value = city,
            onValueChange = onCityChange,
            error = city.length !in 3..50
        )
        CustomTextField(
            placeHolder = "Postal Code",
            value = "${postalCode ?: ""}",
            onValueChange = { onPostalCodeChange(it.toIntOrNull()) },
            error = postalCode.toString().length !in 3..8
        )
        CustomTextField(
            placeHolder = "Address",
            value = address,
            onValueChange = onAddressChange,
            error = address.length !in 3..100
        )
        CustomTextField(
            placeHolder = "Phone Number",
            value = phoneNumber ?: "",
            onValueChange = onPhoneNumberChange,
            error = phoneNumber?.length !in 5..10
        )
    }
}