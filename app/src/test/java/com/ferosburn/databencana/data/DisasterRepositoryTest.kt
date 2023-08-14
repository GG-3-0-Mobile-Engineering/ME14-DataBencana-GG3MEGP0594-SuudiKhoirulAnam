package com.ferosburn.databencana.data

import com.ferosburn.databencana.data.source.local.DisasterEntity
import com.ferosburn.databencana.data.source.local.LocalDataSource
import com.ferosburn.databencana.data.source.remote.GeometryReport
import com.ferosburn.databencana.data.source.remote.RemoteDataSource
import com.ferosburn.databencana.data.source.remote.ReportResponse
import com.ferosburn.databencana.domain.repository.IDisasterRepository
import com.ferosburn.databencana.presentation.home.HomeFragment
import com.ferosburn.databencana.utils.ReportDataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DisasterRepositoryTest {

    @Mock
    private lateinit var remoteDataSource: RemoteDataSource
    @Mock
    private lateinit var localDataSource: LocalDataSource
    private lateinit var repository: IDisasterRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = DisasterRepository(remoteDataSource, localDataSource)
    }

    @Test
    fun testGetRecentReports() = runBlocking {
        val timePeriod = HomeFragment.DAY_TIME_PERIOD * 3

        val mockGeometryReports: List<GeometryReport> = ReportDataDummy.generateListGeometryReport()
        val mockResponse: ReportResponse<List<GeometryReport>> = ReportResponse.Success(mockGeometryReports)

        // Mock remote data source
        `when`(remoteDataSource.getRecentReports(timePeriod)).thenReturn(flowOf(mockResponse))

        // Mock data mapper and local data source
        val mockMappedEntities: List<DisasterEntity> = ReportDataDummy.generateMappedDisasterEntities()
        `when`(localDataSource.getAllDisaster()).thenReturn(flowOf(mockMappedEntities))

        val result = repository.getRecentReports(timePeriod, null, null).toList()
        Assert.assertTrue(result.isNotEmpty())
    }

    @Test
    fun testGetReports() = runBlocking {
        val startTime = "2023-06-30T00:00:00+0700"
        val endTime = "2023-07-01T00:00:00+0700"

        val mockGeometryReports: List<GeometryReport> = ReportDataDummy.generateListGeometryReport()
        val mockResponse: ReportResponse<List<GeometryReport>> = ReportResponse.Success(mockGeometryReports)

        // Mock remote data source
        `when`(remoteDataSource.getReports(startTime, endTime)).thenReturn(flowOf(mockResponse))

        // Mock data mapper and local data source
        val mockMappedEntities: List<DisasterEntity> = ReportDataDummy.generateMappedDisasterEntities()
        `when`(localDataSource.getAllDisaster()).thenReturn(flowOf(mockMappedEntities))

        val result = repository.getReports(startTime, endTime, null).toList()
        Assert.assertTrue(result.isNotEmpty())
    }
}