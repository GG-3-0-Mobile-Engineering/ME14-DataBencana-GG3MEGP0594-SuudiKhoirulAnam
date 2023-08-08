package com.ferosburn.databencana.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "disaster")
data class DisasterEntity (
    @PrimaryKey
    val pkey: String,
    val createdAt: String,
    val imageUrl: String?,
    val disasterType: String,
    val instanceRegionCode: String,
    val longitude: Double,
    val latitude: Double
)