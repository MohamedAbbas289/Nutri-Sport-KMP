package com.nutrisport.shared.domain

import com.nutrisport.shared.Resources
import org.jetbrains.compose.resources.DrawableResource

enum class Country(
    val dialCode: Int,
    val code: String,
    val flag: DrawableResource
) {
    Algeria(
        dialCode = 213,
        code = "DZ",
        flag = Resources.Flag.Algeria
    ),
    Egypt(
        dialCode = 20,
        code = "EG",
        flag = Resources.Flag.Egypt
    ),
    India(
        dialCode = 91,
        code = "IN",
        flag = Resources.Flag.India
    ),
    Iraq(
        dialCode = 964,
        code = "IQ",
        flag = Resources.Flag.Iraq
    ),
    Libya(
        dialCode = 218,
        code = "LY",
        flag = Resources.Flag.Libya
    ),
    Morocco(
        dialCode = 212,
        code = "MA",
        flag = Resources.Flag.Morocco
    ),
    SaudiArabia(
        dialCode = 966,
        code = "SA",
        flag = Resources.Flag.SaudiArabia
    ),
    Serbia(
        dialCode = 381,
        code = "RS",
        flag = Resources.Flag.Serbia
    ),
    Syria(
        dialCode = 963,
        code = "SY",
        flag = Resources.Flag.Syria
    ),
    UnitedStates(
        dialCode = 1,
        code = "US",
        flag = Resources.Flag.Usa
    )
}
