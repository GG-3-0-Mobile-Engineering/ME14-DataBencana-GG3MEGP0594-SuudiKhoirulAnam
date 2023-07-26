package com.ferosburn.databencana.util

import android.os.Build
import androidx.annotation.RequiresApi
import com.ferosburn.databencana.data.DisasterTypes
import com.ferosburn.databencana.data.Provinces
import java.time.LocalDate
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

@RequiresApi(Build.VERSION_CODES.O)
fun String.changeDateFormat(oldPattern: String, newPattern: String): String {
    val date = LocalDate.parse(this, DateTimeFormatter.ofPattern(oldPattern))
    return date.format(DateTimeFormatter.ofPattern(newPattern))
}


@RequiresApi(Build.VERSION_CODES.O)
fun String.toFormattedDate(initialPattern: String): String? {
    val date = LocalDate.parse(this, DateTimeFormatter.ofPattern(initialPattern))
    return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"))
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.toDatetime(initialPattern: String): LocalDate {
    return LocalDate.parse(this, DateTimeFormatter.ofPattern(initialPattern))
}