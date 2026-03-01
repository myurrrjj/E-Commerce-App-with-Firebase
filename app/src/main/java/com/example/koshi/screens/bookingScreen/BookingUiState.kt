package com.example.koshi.screens.bookingScreen

import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

data class BookingUiState(
    val selectedDate: LocalDate? = null,
    val selectedTimeSlot: TimeSlot? = null,
    val currentDisplayMonth: YearMonth = YearMonth.now(),
//    val isPrevMonthEnabled: Boolean = false,
    val dates: List<LocalDate> = emptyList(),
    val firstDayOfMonthOffset: Int = 0,
    val availableTimeSlots: List<TimeSlot> = emptyList()
)

data class TimeSlot(
    val startTime: LocalTime,
    val endTime: LocalTime
) {
    fun format(): String {
        val formatter = DateTimeFormatter.ofPattern("h:mm a")
        return "${startTime.format(formatter)} - ${endTime.format(formatter)}"

    }
}