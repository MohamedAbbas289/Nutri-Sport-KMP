package com.nutrisport.shared

object Constants {
    const val WEB_CLIENT_ID =
        "979601099891-h56sv2knaub7uak7m6j70ojl7j7tg0hd.apps.googleusercontent.com"

    const val PAYPAL_CLIENT_ID =
        "AaEM9sRXnwa4v-CXjx_HzUYGgnQk9KtR_AHaDpceFAb-xh8EAGiOwFZ5HsCEdUIQPpD0ZaoGjOMe42HN"

    const val PAYPAL_SECRET_ID =
        "EGmZtj6llMfIxYakvsCBFw0JNfdk6xVw7BSBjlgx_E82SwMSbNGUeoFbSJFr-rCiyRuKrzRWSM4_6wFI"

    const val PAYPAL_AUTH_KEY = "$PAYPAL_CLIENT_ID:$PAYPAL_SECRET_ID"

    const val PAYPAL_AUTH_ENDPOINT = "https://api-m.sandbox.paypal.com/v1/oauth2/token"

    const val MAX_QUANTITY = 10
    const val MIN_QUANTITY = 1
}