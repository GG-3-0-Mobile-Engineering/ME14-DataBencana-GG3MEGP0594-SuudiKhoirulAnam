package com.ferosburn.databencana.data.source.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://data.petabencana.id"
private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

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

object Report {
    val retrofitService: ReportService by lazy {
        retrofit.create(ReportService::class.java)
    }
}