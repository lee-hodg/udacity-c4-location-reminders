package com.udacity.project4.locationreminders.savereminder

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.R
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.getOrAwaitValue
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class SaveReminderViewModelTest {

    private lateinit var saveReminderViewModel: SaveReminderViewModel
    private lateinit var fakeDataSource: FakeDataSource

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val list = listOf(ReminderDataItem("title",
            "description",
            "location",(-360..360).random().toDouble(),
            (-360..360).random().toDouble()))
    private val firstReminder = list[0]


    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun check_normal_loading() {
        fakeDataSource = FakeDataSource()
        saveReminderViewModel = SaveReminderViewModel(ApplicationProvider.getApplicationContext(),
                fakeDataSource)
        mainCoroutineRule.pauseDispatcher()
        saveReminderViewModel.validateAndSaveReminder(firstReminder)
        Assert.assertThat(saveReminderViewModel.showLoading.getOrAwaitValue(),
                CoreMatchers.`is`(true))
    }

    @Test
    fun check_no_title() {
        fakeDataSource = FakeDataSource(null)
        saveReminderViewModel = SaveReminderViewModel(ApplicationProvider.getApplicationContext(),
                fakeDataSource)
        firstReminder.title = null
        saveReminderViewModel.validateAndSaveReminder(firstReminder)
        Assert.assertThat(saveReminderViewModel.showSnackBarInt.getOrAwaitValue(),
                CoreMatchers.`is`(R.string.err_enter_title))
    }

    @Test
    fun check_no_location() {
        fakeDataSource = FakeDataSource(null)
        saveReminderViewModel = SaveReminderViewModel(ApplicationProvider.getApplicationContext(),
                fakeDataSource)
        firstReminder.title = "X X X"
        firstReminder.location = null
        saveReminderViewModel.validateAndSaveReminder(firstReminder)
        Assert.assertThat(saveReminderViewModel.showSnackBarInt.getOrAwaitValue(),
                CoreMatchers.`is`(R.string.err_select_location))
    }

}