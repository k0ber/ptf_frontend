package org.patifiner.client.signup.ui

import kotlinx.datetime.LocalDate
import org.patifiner.client.base.Constants
import org.patifiner.client.signup.SignupRequest

data class SignupUiState(
    val name: String = "",
    val surname: String = "",
    val year: String = "",
    val month: String = "",
    val day: String = "",
    val email: String = "",
    val password: String = "",
    val confirm: String = "",
    val loading: Boolean = false,
) {

    val nameValid get() = name.trim().length >= 2
    val surnameValid get() = surname.trim().length >= 2

    val y get() = year.toIntOrNull()
    val m get() = month.toIntOrNull()
    val d get() = day.toIntOrNull()

    val dateValid: Boolean
        get() {
            val yy = y ?: return false
            val mm = m ?: return false
            val dd = d ?: return false
            return try {
                LocalDate(yy, mm, dd); true
            } catch (_: Throwable) {
                false
            }
        }

    val emailValid get() = Regex(Constants.EMAIL_REGEX).matches(email.trim())
    val passwordValid get() = password.length >= 8
    val confirmValid get() = confirm == password && confirm.isNotEmpty()

    val canSubmit get() = nameValid && surnameValid && dateValid && emailValid && passwordValid && confirmValid && !loading

    fun toRequest() = SignupRequest(
        name = name.trim(),
        surname = surname.trim(),
        birthDate = LocalDate(y!!, m!!, d!!),
        email = email.trim(),
        password = password
    )
}