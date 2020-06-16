package com.kapil.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.kapil.domain.entity.Event
import com.kapil.domain.entity.Job
import com.kapil.domain.entity.JobFilterProperties
import com.kapil.presentation.common.ShortLivedItem
import com.kapil.presentation.events.*
import com.kapil.presentation.home.HomeActivity
import com.kapil.presentation.jobs.JobListViewEntity
import com.kapil.presentation.jobs.JobsViewModel
import com.kapil.presentation.jobs.jobfilter.JobFilterViewModel
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import java.util.*


@RunWith(AndroidJUnit4::class)
class HomeActivityTest : KoinTest {

    @get:Rule
    val animationsRule = AnimationTestRule()

    @Rule
    @JvmField
    val rule = ActivityTestRule(HomeActivity::class.java, true, false)

    private lateinit var jobsViewModel: JobsViewModel
    private lateinit var eventsViewModel: EventsViewModel
    private lateinit var jobFilterViewModel: JobFilterViewModel

    private lateinit var jobListLiveData: LiveData<JobListViewEntity>
    private lateinit var jobToDeleteLiveData: LiveData<ShortLivedItem<Job>>
    private lateinit var isLoadingLiveData: LiveData<Boolean>
    private lateinit var isErrorLiveData: LiveData<Pair<Boolean, () -> Unit>>
    private lateinit var eventListLiveData: MutableLiveData<EventListViewEntity>
    private lateinit var isOpenCalendarSuccessfulLiveData: LiveData<ShortLivedItem<Boolean>>
    private lateinit var roleSearchLiveData: LiveData<ShortLivedItem<List<String>>>
    private lateinit var companySearchLiveData: LiveData<ShortLivedItem<List<String>>>

    private val dummyEventListViewEntity = EventListViewEntity(
        jobFilterProperties = JobFilterProperties.DEFAULT,
        groupByJob = true,
        sortOrder = SortOrder.DATE_DESC,
        eventGroups = listOf(
            EventGroupListItem(
                jobId = Job.ID_ILLEGAL_VALUE,
                companyName = "Company name",
                role = "Role",
                companyAddress = "Company address",
                events = listOf(
                    Event(
                        title = "Event title",
                        startTime = Date(0)
                    )
                )
            )
        ),
        events = listOf(
            EventListItem(
                jobId = Job.ID_ILLEGAL_VALUE,
                title = "Event title",
                companyName = "Company name",
                role = "Role",
                startTime = "Event start time",
                companyAddress = "Company address"
            )
        )
    )

    @Before
    fun setup() {
        jobsViewModel = mockk()
        eventsViewModel = mockk()
        jobFilterViewModel = mockk()

        jobListLiveData = mockk()
        jobToDeleteLiveData = mockk()
        isLoadingLiveData = mockk()
        isErrorLiveData = mockk()
        isOpenCalendarSuccessfulLiveData = mockk()
        roleSearchLiveData = mockk()
        companySearchLiveData = mockk()

        eventListLiveData = MutableLiveData()

        every { jobsViewModel.observeJobList() } returns Unit
        every { eventsViewModel.observeEventList() } returns Unit

        every { jobsViewModel.jobListViewEntity } returns jobListLiveData
        every { jobListLiveData.observe(any(), any()) } returns Unit
        every { jobsViewModel.jobListViewEntity } returns jobListLiveData
        every { jobListLiveData.observe(any(), any()) } returns Unit
        every { jobsViewModel.jobToDelete } returns jobToDeleteLiveData
        every { jobToDeleteLiveData.observe(any(), any()) } returns Unit
        every { jobsViewModel.isLoading } returns isLoadingLiveData
        every { isLoadingLiveData.observe(any(), any()) } returns Unit
        every { jobsViewModel.isError } returns isErrorLiveData
        every { isErrorLiveData.observe(any(), any()) } returns Unit
        every { eventsViewModel.eventListViewEntity } returns eventListLiveData
        every { eventsViewModel.isOpenCalendarRequestSuccessful } returns isOpenCalendarSuccessfulLiveData
        every { isOpenCalendarSuccessfulLiveData.observe(any(), any()) } returns Unit
        every { eventsViewModel.isLoading } returns isLoadingLiveData
        every { eventsViewModel.isError } returns isErrorLiveData
        every { jobFilterViewModel.roleSearchResults } returns roleSearchLiveData
        every { roleSearchLiveData.observe(any(), any()) } returns Unit
        every { jobFilterViewModel.companySearchResults } returns companySearchLiveData
        every { companySearchLiveData.observe(any(), any()) } returns Unit

        loadKoinModules(module {
            viewModel { jobsViewModel }
            viewModel { eventsViewModel }
            viewModel { jobFilterViewModel }
        })
    }

    @After
    fun cleanUp() {
        stopKoin()
    }

    @Test
    fun testEventsScreenSortButtonVisibilityLogic() {
        rule.launchActivity(null)

        onView(withId(R.id.navigation_my_events)).perform(click())

        onView(withId(R.id.groupByJobBtn)).check(matches(not(isDisplayed())))
        onView(withId(R.id.dateSortBtn)).check(matches(not(isDisplayed())))

        eventListLiveData.postValue(dummyEventListViewEntity)

        onView(withId(R.id.groupByJobBtn)).check(matches(isDisplayed()))
        onView(withId(R.id.dateSortBtn)).check(matches(isDisplayed()))

        onView(allOf(withId(R.id.filterBtn), isDescendantOfA(withId(R.id.eventsFragmentRoot))))
            .perform(click())

        onView(withId(R.id.groupByJobBtn)).check(matches(not(isDisplayed())))
        onView(withId(R.id.dateSortBtn)).check(matches(not(isDisplayed())))

        onView(allOf(withId(R.id.filterBtn), isDescendantOfA(withId(R.id.eventsFragmentRoot))))
            .perform(click())

        onView(withId(R.id.groupByJobBtn)).check(matches(isDisplayed()))
        onView(withId(R.id.dateSortBtn)).check(matches(isDisplayed()))

        eventListLiveData.postValue(
            dummyEventListViewEntity.copy(eventGroups = emptyList(), events = emptyList())
        )

        onView(withId(R.id.groupByJobBtn)).check(matches(not(isDisplayed())))
        onView(withId(R.id.dateSortBtn)).check(matches(not(isDisplayed())))
    }
}
