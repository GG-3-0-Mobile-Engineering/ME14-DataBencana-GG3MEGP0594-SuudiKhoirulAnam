package com.ferosburn.databencana.utils

import com.ferosburn.databencana.domain.model.DisasterTypes
import com.ferosburn.databencana.domain.model.Provinces
import org.junit.Assert.assertEquals
import org.junit.Test

class ExtensionsTest {

    @Test
    fun testProvinceNameToProvinces() {
        val provinceName = "Banten"
        assertEquals(Provinces.BANTEN, provinceName.provinceNameToProvinces())
    }

    @Test
    fun testProvinceCodeToProvinces() {
        val provinceCode = "ID-BT"
        assertEquals(Provinces.BANTEN, provinceCode.provinceCodeToProvinces())
    }

    @Test
    fun testDisasterValueToDisasterTypes() {
        val disasterValue = "fire"
        assertEquals(DisasterTypes.FIRE, disasterValue.disasterValueToDisasterTypes())
    }

    @Test
    fun testDisasterNameToDisasterTypes() {
        val disasterName = "Kebakaran"
        assertEquals(DisasterTypes.FIRE, disasterName.disasterNameToDisasterTypes())
    }

    @Test
    fun testLocalDateToFormattedDateTime() {
        val localDate = "20-12-2012"
        assertEquals("2012-12-20T00:00:00+0000", localDate.localDateToFormattedDateTime("dd-MM-yyyy"))
        assertEquals(null, "".localDateToFormattedDateTime("dd-MM-yyyy"))
    }
}