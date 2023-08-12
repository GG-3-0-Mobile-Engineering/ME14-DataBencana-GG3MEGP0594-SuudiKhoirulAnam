package com.ferosburn.databencana.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DisasterModel(
    val pkey: Int,
    val createdAt: String,
    val imageUrl: String? = null,
    val disasterType: String,
    val instanceRegionCode: String? = null,
    val longitude: Double,
    val latitude: Double
) : Parcelable