package com.ferosburn.databencana.utils

import com.ferosburn.databencana.data.source.local.DisasterEntity
import com.ferosburn.databencana.data.source.remote.GeometryReport
import com.ferosburn.databencana.data.source.remote.PropertiesReport
import com.ferosburn.databencana.data.source.remote.PropertyTags
import org.junit.Assert.assertEquals
import org.junit.Test

class DataMapperTest {

    @Test
    fun testMapResponseToEntities() {
        val input = listOf(
            GeometryReport(
                type = "Point",
                properties = PropertiesReport(
                    pkey = "322065",
                    createdAt = "2023-08-14T07:09:10.538Z",
                    source = "grasp",
                    status = "confirmed",
                    url = "b86d0fb5-67b4-41ec-9869-a022ba0efb33",
                    imageUrl = "https://images.petabencana.id/b86d0fb5-67b4-41ec-9869-a022ba0efb33.jpg",
                    disasterType = "flood",
                    propertyReportData = null,
                    propertyTags = PropertyTags(
                        instanceRegionCode = "ID-BT",
                        districtId = null,
                        regionCode = null,
                        localAreaId = null
                    ),
                    title = "",
                    text = "",
                    partnerCode = "",
                    partnerIcon = ""
                ),
                coordinates = listOf(106.6677472407, -6.1203495768)
            )
        )

        val result = DataMapper.mapResponseToEntities(input)
        val disasterEntity = result[0]

        assertEquals(322065, disasterEntity.pkey)
        assertEquals("2023-08-14T07:09:10.538Z", disasterEntity.createdAt)
        assertEquals("https://images.petabencana.id/b86d0fb5-67b4-41ec-9869-a022ba0efb33.jpg", disasterEntity.imageUrl)
        assertEquals("flood", disasterEntity.disasterType)
        assertEquals("ID-BT", disasterEntity.instanceRegionCode)
        assertEquals(106.6677472407, disasterEntity.longitude, 0.01)
        assertEquals(-6.1203495768, disasterEntity.latitude, 0.01)
    }

    @Test
    fun testMapEntitiesToDomain() {
        val input = listOf(
            DisasterEntity(
                pkey = 322065,
                createdAt = "2023-08-14T07:09:10.538Z",
                imageUrl = "https://images.petabencana.id/b86d0fb5-67b4-41ec-9869-a022ba0efb33.jpg",
                disasterType = "flood",
                instanceRegionCode = "ID-BT",
                longitude = 106.6677472407,
                latitude = -6.1203495768
            )
        )

        val result = DataMapper.mapEntitiesToDomain(input)
        val disasterModel = result[0]
        assertEquals(322065, disasterModel.pkey)
        assertEquals("2023-08-14T07:09:10.538Z", disasterModel.createdAt)
        assertEquals("https://images.petabencana.id/b86d0fb5-67b4-41ec-9869-a022ba0efb33.jpg", disasterModel.imageUrl)
        assertEquals("flood", disasterModel.disasterType)
        assertEquals("ID-BT", disasterModel.instanceRegionCode)
        assertEquals(106.6677472407, disasterModel.longitude, 0.01)
        assertEquals(-6.1203495768, disasterModel.latitude, 0.01)
    }
}