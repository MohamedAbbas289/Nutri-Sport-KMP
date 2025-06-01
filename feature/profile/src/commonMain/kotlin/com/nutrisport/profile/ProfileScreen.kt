package com.nutrisport.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.nutrisport.shared.Surface
import com.nutrisport.shared.component.ProfileForm
import com.nutrisport.shared.domain.Country


@Composable
fun ProfileScreen() {
    var country by remember { mutableStateOf(Country.Egypt) }
    Box(
        modifier = Modifier
            .background(Surface)
            .systemBarsPadding()
    ) {
        ProfileForm(
            country = Country.UnitedStates,
            onCountrySelect = { selectedCountry ->
                country = selectedCountry
            },
            firstName = "Mohamed",
            onFirstNameChange = {},
            lastName = "",
            onLastNameChange = {},
            email = "",
            city = "",
            onCityChange = {},
            postalCode = null,
            onPostalCodeChange = {},
            address = "",
            onAddressChange = {},
            phoneNumber = "",
            onPhoneNumberChange = {}
        )
    }

}