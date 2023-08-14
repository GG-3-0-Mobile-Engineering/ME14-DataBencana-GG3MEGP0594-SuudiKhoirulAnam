package com.ferosburn.databencana.utils

import com.ferosburn.databencana.domain.model.DisasterTypes
import com.ferosburn.databencana.domain.model.Provinces
import org.junit.Assert
import org.junit.Test

class ExtensionsTest {

    @Test
    fun provinceNameToProvincesTest() {
        val provinceName = "Banten"
        Assert.assertEquals(Provinces.BANTEN, provinceName.provinceNameToProvinces())
    }

    @Test
    fun provinceCodeToProvincesTest() {
        val provinceCode = "ID-BT"
        Assert.assertEquals(Provinces.BANTEN, provinceCode.provinceCodeToProvinces())
    }

    @Test
    fun disasterValueToDisasterTypesTest() {
        val disasterValue = "fire"
        Assert.assertEquals(DisasterTypes.FIRE, disasterValue.disasterValueToDisasterTypes())
    }

    @Test
    fun disasterNameToDisasterTypesTest() {
        val disasterName = "Kebakaran"
        Assert.assertEquals(DisasterTypes.FIRE, disasterName.disasterNameToDisasterTypes())
    }

    @Test
    fun localDateToFormattedDateTimeTest() {
        val localDate = "20-12-2012"
        Assert.assertEquals("2012-12-20T00:00:00+0000", localDate.localDateToFormattedDateTime("dd-MM-yyyy"))
        Assert.assertEquals(null, "".localDateToFormattedDateTime("dd-MM-yyyy"))
    }
}