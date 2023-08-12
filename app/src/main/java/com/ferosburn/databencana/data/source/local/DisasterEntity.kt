package com.ferosburn.databencana.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "disaster")
data class DisasterEntity (
    @PrimaryKey
    val pkey: Int,
    val createdAt: String,
    val imageUrl: String? = null,
    val disasterType: String,
    val instanceRegionCode: String? = null,
    val longitude: Double,
    val latitude: Double
)