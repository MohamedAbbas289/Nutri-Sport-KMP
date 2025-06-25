package com.nutrisport.shared

object Constants {
    const val WEB_CLIENT_ID =
        "979601099891-h56sv2knaub7uak7m6j70ojl7j7tg0hd.apps.googleusercontent.com"

    const val PAYPAL_CLIENT_ID =
        "ATiCItdY-eGljy-zwu5ItbuMnsht7Vs6iYAvMgeQZ2Hc4YebEeno-cS1Vwh89Fz716B1yjxBXcVMBDg1"

    const val PAYPAL_SECRET_ID =
        "ECqX5X0JuW__QNbB8DyFI8eAXtfwmihW1CMQEzmbP9WOGCUP7D74Qpg3jYOXqxYnwosqFnlko6oRSRGg"

    const val PAYPAL_AUTH_KEY = "$PAYPAL_CLIENT_ID:$PAYPAL_SECRET_ID"
    const val PAYPAL_AUTH_ENDPOINT = "https://api-m.sandbox.paypal.com/v1/oauth2/token"
    const val PAYPAL_CHECKOUT_ENDPOINT = "https://api-m.sandbox.paypal.com/v2/checkout/orders"

    const val RETURN_URL = "com.midoo.abbas.nutrisport://paypalpay?success=true"
    const val CANCEL_URL = "com.midoo.abbas.nutrisport://paypalpay?cancel=true"

    const val MAX_QUANTITY = 10
    const val MIN_QUANTITY = 1
}