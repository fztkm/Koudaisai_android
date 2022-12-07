package com.nit.koudaisai.common.ext

import android.util.Patterns
import java.util.regex.Pattern

private const val MIN_PASS_LENGTH = 6
private const val PASS_PATTERN = "^[a-zA-Z0-9]{4,}$"
private const val PHONE_PATTERN =
    "/\\A(((0(\\d{1}[-(]?\\d{4}|\\d{2}[-(]?\\d{3}|\\d{3}[-(]?\\d{2}|\\d{4}[-(]?\\d{1}|[5789]0[-(]?\\d{4})[-)]?)|\\d{1,4}\\-?)\\d{4}|0120[-(]?\\d{3}[-)]?\\d{3})\\z/"
private const val MAX_COMPANION_NUM = 2

fun String.isValidEmail(): Boolean {
    return this.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidPassword(): Boolean {
    return this.isNotBlank()
            && this.length >= MIN_PASS_LENGTH
            && Pattern.compile(PASS_PATTERN).matcher(this).matches()
}

fun String.isValidPhoneNumber(): Boolean {
    return this.isNotBlank()
}

fun String.passwordMatches(repeated: String): Boolean {
    return this == repeated
}

fun String.toCompanions(): Int {
    var value = this.toIntOrNull()
    return if (value != null) {
        if (value < 0) {
            value = 0
        } else if (value > MAX_COMPANION_NUM) {
            value = MAX_COMPANION_NUM
        }
        value
    } else {
        0
    }
}

fun String.idFromParameter(): String {
    return this.substring(1, this.length - 1)
}