package com.ferosburn.databencana.data.source.remote

import com.squareup.moshi.Json

data class DataReport(
    val statusCode: Int,
    val result: ResultReport?,
    val message: String?,
)

data class ResultReport(
    val type: String,
    @Json(name = "objects")
    val objectReport: ObjectReport,
    val arcs: List<Any>,
    val bbox: List<Double>
)

data class ObjectReport(
    val output: OutputReport
)

data class OutputReport(
    val type: String,
    val geometries: List<GeometryReport>
)

data class GeometryReport(
    val type: String,
    val properties: PropertiesReport,
    val coordinates: List<Double>
)

data class PropertiesReport(
    val pkey: String,
    @Json(name = "created_at")
    val createdAt: String,
    val source: String,
    val status: String,
    val url: String,
    @Json(name = "image_url")
    val imageUrl: String?,
    @Json(name = "disaster_type")
    val disasterType: String,
    @Json(name = "report_data")
    val propertyReportData: PropertyReportData,
    @Json(name = "tags")
    val propertyTags: PropertyTags,
    val title: String?,
    val text: String?,
    @Json(name = "partner_code")
    val partnerCode: String?,
    @Json(name = "partner_icon")
    val partnerIcon: String?
)

data class PropertyReportData(
    @Json(name = "report_type")
    val reportType: String,
    // reportType = flood
    @Json(name = "flood_depth")
    val floodDepth: Int?,
    // reportType = structure
    val structureFailure: Int?,
    // reportType = road
    @Json(name = "accessabilityFailure")
    val accessibilityFailure: Int?,
    val condition: Int?,
    // reportType = haze
    val visibility: Int?,
    val airQuality: Int?,
    // reportType = wind
    val impact: Int?,
    // reportType = volcano
    val volcanicSigns: List<Int>?,
    val evacuationNumber: Int?,
    val evacuationArea: Boolean?,
    // reportType = fire
    val fireDistance: Double?,
    val fireLocation: LongLat?,
    val personLocation: LongLat?,
    val fireRadius: LongLat?
)

data class LongLat(
    @Json(name = "lng")
    val longitude: Double,
    @Json(name = "lat")
    val latitude: Double,
)

data class PropertyTags(
    @Json(name = "district_id")
    val districtId: Any?,
    @Json(name = "region_code")
    val regionCode: String?,
    @Json(name = "local_area_id")
    val localAreaId: String?,
    @Json(name = "instance_region_code")
    val instanceRegionCode: String?,
)
