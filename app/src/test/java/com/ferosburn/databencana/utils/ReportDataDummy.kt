package com.ferosburn.databencana.utils

import com.ferosburn.databencana.data.source.remote.DataReport
import com.ferosburn.databencana.data.source.remote.GeometryReport
import com.ferosburn.databencana.data.source.remote.ObjectReport
import com.ferosburn.databencana.data.source.remote.OutputReport
import com.ferosburn.databencana.data.source.remote.PropertiesReport
import com.ferosburn.databencana.data.source.remote.PropertyTags
import com.ferosburn.databencana.data.source.remote.ResultReport

object ReportDataDummy {

    private val data = DataReport(
        statusCode = 200,
        result = ResultReport(
            type = "Topology",
            objectReport = ObjectReport(
                output = OutputReport(
                    type = "GeometryCollection",
                    geometries = listOf(
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
                        ),
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
                        ),
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
                )
            ),
            arcs = listOf(),
            bbox = listOf(106.6677472407, -6.1203495768),
        ),
        message = null
    )

    fun generateListGeometryReport() = data.result!!.objectReport.output.geometries

    fun generateMappedDisasterEntities() = DataMapper.mapResponseToEntities(generateListGeometryReport())

    fun generateMappedDisasterModel() = DataMapper.mapEntitiesToDomain(
        generateMappedDisasterEntities()
    )
}