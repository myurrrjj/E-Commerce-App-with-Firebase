package com.example.koshi.screens.bookingScreen

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(BookingUiState())
    val uiState = _uiState.asStateFlow()


    val isPrevMonthEnabled: StateFlow<Boolean> = uiState.map { it.currentDisplayMonth!= YearMonth.now() }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000),false
    )




    private val unavailableDates = listOf(
        LocalDate.now().plus(3, ChronoUnit.DAYS), LocalDate.now().plus(
            7,
            ChronoUnit.DAYS
        )
    )

    init {
        generateCalendarDates(YearMonth.now())
        generateAvailableTimeSlots(LocalDate.now())
    }

    private fun generateCalendarDates(yearMonth: YearMonth) {
        val firstDay = yearMonth.atDay(1)
        val daysInMonth = yearMonth.lengthOfMonth()
        val firstDayOfWeek = firstDay.dayOfWeek.value % 7
        val dates = (1..daysInMonth).map { day ->
            yearMonth.atDay(day)
        }

        _uiState.update {
            it.copy(
                dates = dates,
                firstDayOfMonthOffset = firstDayOfWeek
            )
        }
    }

    private fun generateAvailableTimeSlots(date: LocalDate) {
        val slots = (9..16).map { hour ->
            TimeSlot(
                startTime = LocalTime.of(hour, 0),
                endTime = LocalTime.of(hour + 1, 0)
            )
        }.filter { slot ->
            if (date.isEqual(LocalDate.now())) {
                slot.startTime.isAfter(LocalTime.now())
            } else {
                true
            }
        }
        _uiState.update { it.copy(availableTimeSlots = slots, selectedTimeSlot = null) }
    }

    fun onNextMonth() {
        val nextMonth = _uiState.value.currentDisplayMonth.plusMonths(1)
        generateCalendarDates(nextMonth)
        _uiState.update {
            it.copy(currentDisplayMonth = nextMonth)
        }
    }

    fun onPrevMonth() {

            val prevMonth = _uiState.value.currentDisplayMonth.minusMonths(1)
            generateCalendarDates(prevMonth)
            _uiState.update {
                it.copy(currentDisplayMonth = prevMonth)
            }

    }

    fun isDateAvailable(date: LocalDate): Boolean{
        return date.isAfter(LocalDate.now().minusDays(1)) && !unavailableDates.contains(date)
    }
    fun onDateSelected(date: LocalDate){
        if (isDateAvailable(date)) {
            _uiState.update {
                it.copy(
                    selectedDate = date,
                    selectedTimeSlot = null
                )
            }
            generateAvailableTimeSlots(date)
        }
    }

    fun onTimeSlotSelected(timeSlot: TimeSlot) {
        _uiState.update { it.copy(selectedTimeSlot = timeSlot) }
    }

}