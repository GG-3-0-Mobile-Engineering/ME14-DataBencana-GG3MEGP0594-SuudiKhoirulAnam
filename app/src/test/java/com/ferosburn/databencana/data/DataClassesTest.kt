package com.ferosburn.databencana.data

import com.ferosburn.databencana.data.source.remote.DataReport
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert.assertEquals
import org.junit.Test

class DataClassesTest {
    @Test
    fun testMoshiSerialization() {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter(DataReport::class.java)

        val responseJsonString = """
        {
          "statusCode": 200,
          "result": {
            "type": "Topology",
            "objects": {
              "output": {
                "type": "GeometryCollection",
                "geometries": [
                  {
                    "type": "Point",
                    "properties": {
                      "pkey": "322065",
                      "created_at": "2023-08-14T07:09:10.538Z",
                      "source": "grasp",
                      "status": "confirmed",
                      "url": "b86d0fb5-67b4-41ec-9869-a022ba0efb33",
                      "image_url": "https://images.petabencana.id/b86d0fb5-67b4-41ec-9869-a022ba0efb33.jpg",
                      "disaster_type": "flood",
                      "report_data": {
                        "report_type": "flood",
                        "flood_depth": 198
                      },
                      "tags": {
                        "district_id": null,
                        "region_code": "3671",
                        "local_area_id": null,
                        "instance_region_code": "ID-BT"
                      },
                      "title": null,
                      "text": "anjirr dalam banger",
                      "partner_code": null,
                      "partner_icon": null
                    },
                    "coordinates": [
                      106.6677472407,
                      -6.1203495768
                    ]
                  }
                ]
              }
            },
            "arcs": [],
            "bbox": [
              106.6677472407,
              -6.1203495768,
              106.6677472407,
              -6.1203495768
            ]
          }
        }
    """.trimIndent()

        val dataReport = adapter.fromJson(responseJsonString)
        val result = dataReport?.result
        val output = result?.objectReport?.output
        val arcs = result?.arcs
        val bbox = result?.bbox
        val geometries = output?.geometries
        val geometry = geometries?.get(0)
        val properties = geometry?.properties
        val reportData = properties?.propertyReportData
        val tags = properties?.propertyTags
        assertEquals(200, dataReport?.statusCode)
        assertEquals("Topology", result?.type)
        assertEquals("GeometryCollection", output?.type)
        assertEquals("Point", geometry?.type)
        assertEquals(listOf(106.6677472407, -6.1203495768), geometry?.coordinates)
        assertEquals("322065", properties?.pkey)
        assertEquals("2023-08-14T07:09:10.538Z", properties?.createdAt)
        assertEquals("grasp", properties?.source)
        assertEquals("confirmed", properties?.status)
        assertEquals("b86d0fb5-67b4-41ec-9869-a022ba0efb33", properties?.url)
        assertEquals(
            "https://images.petabencana.id/b86d0fb5-67b4-41ec-9869-a022ba0efb33.jpg",
            properties?.imageUrl
        )
        assertEquals("flood", properties?.disasterType)
        assertEquals(null, properties?.title)
        assertEquals("anjirr dalam banger", properties?.text)
        assertEquals(null, properties?.partnerCode)
        assertEquals(null, properties?.partnerIcon)
        assertEquals("flood", reportData?.reportType)
        assertEquals(198, reportData?.floodDepth)
        assertEquals(null, tags?.districtId)
        assertEquals("3671", tags?.regionCode)
        assertEquals(null, tags?.localAreaId)
        assertEquals("ID-BT", tags?.instanceRegionCode)
        assertEquals(listOf<Double>(), arcs)
        assertEquals(
            listOf<Double>(106.6677472407, -6.1203495768, 106.6677472407, -6.1203495768),
            bbox
        )
    }
}