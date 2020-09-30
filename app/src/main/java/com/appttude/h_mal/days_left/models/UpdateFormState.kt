package com.appttude.h_mal.days_left.models

/**
 * Data validation state of the login form.
 */
data class UpdateFormState(
    var emailError: Int? = null,
    var newEmailError: Int? = null,
    var passwordError: Int? = null,
    var newPasswordError: Int? = null
)