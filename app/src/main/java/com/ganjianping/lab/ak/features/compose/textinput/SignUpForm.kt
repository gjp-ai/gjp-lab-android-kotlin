package com.ganjianping.lab.ak.features.compose.textinput

/** The sign-up sample's input and its validation rules, kept free of Compose so tests can check them. */
data class SignUpForm(
    val name: String = "",
    val email: String = "",
    val password: String = ""
) {
    /** One message per broken rule, in field order; empty when the form is valid. */
    val problems: List<String>
        get() = buildList {
            if (name.isBlank()) add("Enter your name.")
            if (!EmailPattern.matches(email)) add("Enter an email like name@example.com.")
            if (password.length < MinimumPasswordLength) add("Use at least $MinimumPasswordLength characters for the password.")
        }

    val isValid: Boolean get() = problems.isEmpty()

    companion object {
        const val MinimumPasswordLength = 8
        // Something before and after the @, a dot in the domain, and no spaces anywhere.
        private val EmailPattern = Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")
    }
}
