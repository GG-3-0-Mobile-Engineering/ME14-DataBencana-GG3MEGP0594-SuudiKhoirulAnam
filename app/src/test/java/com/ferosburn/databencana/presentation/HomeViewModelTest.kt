package com.ferosburn.databencana.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.ferosburn.databencana.data.Resource
import com.ferosburn.databencana.domain.model.DisasterModel
import com.ferosburn.databencana.domain.usecase.DisasterUseCase
import com.ferosburn.databencana.presentation.home.HomeFragment
import com.ferosburn.databencana.presentation.home.HomeViewModel
import com.ferosburn.databencana.utils.ReportDataDummy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@Suppress("UNCHECKED_CAST")
@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    @Mock
    private lateinit var disasterUseCase: DisasterUseCase
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel(disasterUseCase)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun testGetRecentReports() = testScope.runTest {
        val timePeriod = HomeFragment.DAY_TIME_PERIOD * 3
        val mockDisasterModels: List<DisasterModel> = ReportDataDummy.generateMappedDisasterModel()
        val mockObserver: Observer<Resource<List<DisasterModel>>> = mock(Observer::class.java) as Observer<Resource<List<DisasterModel>>>

        val captor = ArgumentCaptor.forClass(Resource::class.java) as ArgumentCaptor<Resource<List<DisasterModel>>>
        viewModel.getRecentReports(timePeriod, null, null).observeForever(mockObserver)

        `when`(disasterUseCase.getRecentReports(timePeriod, null, null))
            .thenReturn(flow { emit(Resource.Success(mockDisasterModels)) })

        advanceTimeBy(1000)

        verify(mockObserver).onChanged(captor.capture())
        Assert.assertEquals(Resource.Success(mockDisasterModels), captor.value)
    }

    @Test
    fun testGetReports() = testScope.runTest {
        val startTime = "2023-06-30T00:00:00+0700"
        val endTime = "2023-07-01T00:00:00+0700"

        val mockDisasterModels: List<DisasterModel> = ReportDataDummy.generateMappedDisasterModel()
        val mockObserver: Observer<Resource<List<DisasterModel>>> = mock(Observer::class.java) as Observer<Resource<List<DisasterModel>>>

        val captor = ArgumentCaptor.forClass(Resource::class.java) as ArgumentCaptor<Resource<List<DisasterModel>>>
        viewModel.getReports(startTime, endTime, null).observeForever(mockObserver)

        `when`(disasterUseCase.getReports(startTime, endTime, null))
            .thenReturn(flow { emit(Resource.Success(mockDisasterModels)) })

        advanceTimeBy(1000)

        verify(mockObserver).onChanged(captor.capture())
        Assert.assertEquals(Resource.Success(mockDisasterModels), captor.value)
    }
}