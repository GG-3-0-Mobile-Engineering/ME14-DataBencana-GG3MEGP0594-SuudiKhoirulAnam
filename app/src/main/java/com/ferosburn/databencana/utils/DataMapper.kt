package com.ferosburn.databencana.utils

import com.ferosburn.databencana.data.source.local.DisasterEntity
import com.ferosburn.databencana.data.source.remote.GeometryReport
import com.ferosburn.databencana.domain.model.DisasterModel

object DataMapper {

    fun mapResponseToEntities(input: List<GeometryReport>): List<DisasterEntity> {
        val disasterList = ArrayList<DisasterEntity>()
        input.map {
            val disaster = DisasterEntity(
                pkey = it.properties.pkey.toInt(),
                createdAt = it.properties.createdAt,
                imageUrl = it.properties.imageUrl,
                disasterType = it.properties.disasterType,
                instanceRegionCode = it.properties.propertyTags.instanceRegionCode,
                longitude = it.coordinates[0],
                latitude = it.coordinates[1]
            )
            disasterList.add(disaster)
        }
        return disasterList
    }

    fun mapEntitiesToDomain(input: List<DisasterEntity>): List<DisasterModel> =
        input.map {
            DisasterModel(
                pkey = it.pkey,
                createdAt = it.createdAt,
                imageUrl = it.imageUrl,
                disasterType = it.disasterType,
                instanceRegionCode = it.instanceRegionCode,
                longitude = it.longitude,
                latitude = it.latitude
            )
        }
}