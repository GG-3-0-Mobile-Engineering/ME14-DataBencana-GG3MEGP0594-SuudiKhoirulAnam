package com.ferosburn.databencana.data.source.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface ReportService {
    @GET("reports/archive")
    suspend fun getReports(
        @Query("start") start: String,
        @Query("end") end: String,
        @Query("admin") provinceCode: String?
    ): DataReport

    @GET("reports")
    suspend fun getRecentReports(
        @Query("timeperiod") timePeriod: Int,
        @Query("admin") provinceCode: String?,
        @Query("disaster") disasterValue: String?
    ): DataReport
}