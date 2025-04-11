package com.example.ticketmag.Misc

class Validator {
    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex(
            "[a-zA-Z0-9+._%\\-]{1,24}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,24}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,24}" +
                    ")+"
        )
        return email.matches(emailRegex)
    }

    fun isValidBulgarianPhone(phone: String): Boolean {
        // valid formats
        // +359 XX XXX XXXX
        // 0XX XXX XXX
        // (359) XX XXX XXXX
        val cleanPhone = phone.replace(Regex("[\\s\\-()]"), "")

        val bulgarianPhoneRegex = Regex(
            "^(?:(?:\\+|00)359|0)" +  // Prefix: +359, 00359, or 0
                    "(?:87|88|89|98|99)" +    // Valid Bulgarian mobile prefixes
                    "[0-9]{7}$"               // Remaining 7 digits
        )

        return cleanPhone.matches(bulgarianPhoneRegex)
    }

    fun isValidLength(text: String): Boolean {
        return text.length in 2..24
    }

    fun isValidLengthLong(text: String): Boolean {
        return text.length in 2..1000
    }

    fun isValidSeatCount(seatCount: String): Boolean {
        val count = seatCount.toIntOrNull() // Convert string to integer, null if invalid
        return count != null && count in 1..500 // True if between 1 and 500
    }

    fun isValidPrice(price: String): Boolean {
        // Check format: integer or decimal with up to 2 places (e.g., "10", "10.5", "10.50")
        if (!price.matches(Regex("^\\d+(\\.\\d{1,2})?$"))) {
            return false
        }
        // Parse to double and validate range
        val priceValue = price.toDoubleOrNull() ?: return false
        return priceValue in 1.00..1000.00
    }

    fun isValidPasswordFormat(password: String): Boolean {
        // At least 6 characters, one uppercase, one lowercase, one number, and one symbol
        val hasMinLength = password.length >= 6
        val hasUpperCase = Regex(".*[A-Z].*").matches(password)
        val hasLowerCase = Regex(".*[a-z].*").matches(password)
        val hasNumber = Regex(".*[0-9].*").matches(password)
        val hasSymbol = Regex(".*[!@#\$%^&*()_+=\\-\\[\\]{}|;:,.<>?].*").matches(password)

        return hasMinLength && hasUpperCase && hasLowerCase && hasNumber && hasSymbol
    }

    fun doPasswordsMatch(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }
}