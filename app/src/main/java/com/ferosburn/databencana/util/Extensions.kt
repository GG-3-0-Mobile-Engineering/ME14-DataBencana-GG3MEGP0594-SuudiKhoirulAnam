package com.ferosburn.databencana.util

import com.ferosburn.databencana.network.DisasterTypes
import com.ferosburn.databencana.network.Provinces
import com.google.android.material.textfield.TextInputLayout
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun String.provinceNameToProvinces(): Provinces? {
    return Provinces.values().firstOrNull() { it.provinceName == this }
}

fun String.provinceCodeToProvinces(): Provinces? {
    return Provinces.values().firstOrNull() { it.code == this }
}

fun String.disasterValueToDisasterTypes(): DisasterTypes? {
    return DisasterTypes.values().firstOrNull() { it.disasterValue == this }
}

fun String.disasterNameToDisasterTypes(): DisasterTypes? {
    return DisasterTypes.values().firstOrNull() { it.disasterName == this }
}

fun String.localDateToFormattedDateTime(initialPattern: String): String? {
    if (this.isNotBlank()) {
        val date = LocalDate.parse(this, DateTimeFormatter.ofPattern(initialPattern))
            .atStartOfDay().atOffset(ZoneOffset.UTC)
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"))
    }
    return null
}

fun TextInputLayout.setInputError(message: String) {
    isErrorEnabled = true
    error = message
}