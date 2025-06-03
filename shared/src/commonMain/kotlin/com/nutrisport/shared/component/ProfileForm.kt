package com.nutrisport.shared.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nutrisport.shared.component.dialog.CountryPickerDialog
import com.nutrisport.shared.domain.Country

@Composable
fun ProfileForm(
    modifier: Modifier = Modifier,
    country: Country,
    onCountrySelect: (Country) -> Unit,
    firstName: String,
    onFirstNameChange: (String) -> Unit,
    lastName: String,
    onLastNameChange: (String) -> Unit,
    email: String,
    city: String?,
    onCityChange: (String) -> Unit,
    postalCode: Int?,
    onPostalCodeChange: (Int?) -> Unit,
    address: String?,
    onAddressChange: (String) -> Unit,
    phoneNumber: String?,
    onPhoneNumberChange: (String) -> Unit
) {
    var showCountryPickerDialog by remember { mutableStateOf(false) }
    AnimatedVisibility(
        visible = showCountryPickerDialog
    ) {
        CountryPickerDialog(
            country = country,
            onDismiss = { showCountryPickerDialog = false },
            onConfirmClick = { selectedCountry ->
                showCountryPickerDialog = false
                onCountrySelect(selectedCountry)
            }
        )
    }
    Column(
        modifier = modifier
            .fillMaxSize()
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
            value = city ?: "",
            onValueChange = onCityChange,
            error = city?.length !in 3..50
        )
        CustomTextField(
            placeHolder = "Postal Code",
            value = "${postalCode ?: ""}",
            onValueChange = { onPostalCodeChange(it.toIntOrNull()) },
            error = postalCode == null || postalCode.toString().length !in 3..8,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        CustomTextField(
            placeHolder = "Address",
            value = address ?: "",
            onValueChange = onAddressChange,
            error = address?.length !in 3..100
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AlertTextField(
                text = "+${country.dialCode}",
                icon = country.flag,
                onClick = { showCountryPickerDialog = true }
            )
            Spacer(Modifier.width(12.dp))
            CustomTextField(
                placeHolder = "Phone Number",
                value = phoneNumber ?: "",
                onValueChange = onPhoneNumberChange,
                error = phoneNumber?.length !in 5..15,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}