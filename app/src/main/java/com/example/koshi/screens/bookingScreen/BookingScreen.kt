package com.example.koshi.screens.bookingScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.koshi.screens.Detailscreen.DetailViewModel
import com.example.koshi.screens.cartScreen.CartViewModel
import com.example.koshi.ui.theme.KoshiTheme
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.ceil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    navigateBack: () -> Unit,
    cartViewModel: CartViewModel,
    detailViewModel: DetailViewModel,
    onConfirmBooking: () -> Unit
) {
    val bookingViewModel: BookingViewModel = hiltViewModel()
    val uiState by bookingViewModel.uiState.collectAsState()
    val cartState by cartViewModel.uiState.collectAsState()
    val service by detailViewModel.selectedService.collectAsState()
    val isPrevMonthEnabled by bookingViewModel.isPrevMonthEnabled.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book an Appointment", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            Button(
                onClick = { onConfirmBooking() },
                enabled = uiState.selectedDate != null && uiState.selectedTimeSlot != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp)
                    .offset(y= (-24).dp)
            ) {
                Text("Confirm Booking", fontSize = 18.sp)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            CalendarHeader(
                currentMonth = uiState.currentDisplayMonth,
                isPrevEnabled = isPrevMonthEnabled,
                onPrevMonth = { bookingViewModel.onPrevMonth() },
                onNextMonth = { bookingViewModel.onNextMonth() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            CalendarGrid(
                dates = uiState.dates,
                firstDayOffset = uiState.firstDayOfMonthOffset,
                selectedDate = uiState.selectedDate,
                isDateAvailable = bookingViewModel::isDateAvailable,
                onDateSelected = bookingViewModel::onDateSelected
            )

            AnimatedVisibility(
                visible = uiState.selectedDate != null,
                enter = fadeIn(animationSpec = tween(durationMillis = 300, delayMillis = 150)) +
                        slideInVertically(
                            initialOffsetY = { it / 2 },
                            animationSpec = tween(durationMillis = 400)
                        ),
                exit = fadeOut(animationSpec = tween(150))
            ) {
                TimeSlotSelector(
                    timeSlots = uiState.availableTimeSlots,
                    selectedTimeSlot = uiState.selectedTimeSlot,
                    onTimeSlotSelected = bookingViewModel::onTimeSlotSelected
                )
            }
        }
    }
}

@Composable
fun CalendarHeader(
    currentMonth: YearMonth,
    isPrevEnabled: Boolean,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onPrevMonth, enabled = isPrevEnabled) {
            Icon(Icons.Default.ChevronLeft, contentDescription = "Previous Month")
        }
        Text(
            text = "${
                currentMonth.month.getDisplayName(
                    TextStyle.FULL,
                    Locale.getDefault()
                )
            } ${currentMonth.year}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        IconButton(onClick = onNextMonth) {
            Icon(Icons.Default.ChevronRight, contentDescription = "Next Month")
        }
    }
}

@Composable
fun CalendarGrid(
    dates: List<LocalDate>,
    firstDayOffset: Int,
    selectedDate: LocalDate?,
    isDateAvailable: (LocalDate) -> Boolean,
    onDateSelected: (LocalDate) -> Unit
) {
    val totalCells = firstDayOffset + dates.size
    val rows = ceil(totalCells / 7f).toInt()

    val cellHeight = 50.dp
    val verticalSpacing = 10.dp

    val gridHeight = (cellHeight * rows) + (verticalSpacing * (rows - 1))
    val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(vertical = 12.dp, horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }

        Divider(
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxWidth()
                .height(gridHeight)
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            userScrollEnabled = false
        ) {
            items(firstDayOffset) { Spacer(Modifier) }
            items(dates.size) { index ->
                val date = dates[index]
                val isAvailable = isDateAvailable(date)
                val isSelected = date == selectedDate
                DateCell(
                    date = date,
                    isAvailable = isAvailable,
                    isSelected = isSelected,
                    onDateSelected = { onDateSelected(date) }
                )
            }
        }
    }
}

@Composable
fun DateCell(
    date: LocalDate,
    isAvailable: Boolean,
    isSelected: Boolean,
    onDateSelected: () -> Unit
) {
    val backgroundColor = when {
        isSelected -> MaterialTheme.colorScheme.primaryContainer
        else -> Color.Transparent
    }
    val contentColor = when {
        isSelected -> MaterialTheme.colorScheme.onPrimaryContainer
        isAvailable -> MaterialTheme.colorScheme.onSurface
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f)
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(enabled = isAvailable, onClick = onDateSelected)
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            color = contentColor,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
@Composable
fun TimeSlotSelector(
    timeSlots: List<TimeSlot>,
    selectedTimeSlot: TimeSlot?,
    onTimeSlotSelected: (TimeSlot) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState())

    ) {
        Text(
            "Available Slots",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        Spacer(modifier = Modifier.height(12.dp))

        if (timeSlots.isEmpty()) {
            Text(
                "No available slots for this day.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                timeSlots.forEach { slot ->
                    val isSelected = slot == selectedTimeSlot
                    TimeSlotChip(
                        timeSlot = slot,
                        isSelected = isSelected,
                        onTimeSlotSelected = { onTimeSlotSelected(slot) }
                    )
                }
            }
        }
    }
}

@Composable
fun TimeSlotChip(
    timeSlot: TimeSlot,
    isSelected: Boolean,
    onTimeSlotSelected: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier
            .defaultMinSize(minWidth = 80.dp)
            .clickable(onClick = onTimeSlotSelected)
            .animateContentSize()
            .fillMaxWidth()
    ) {
        Text(
            text = timeSlot.format(),
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            textAlign = TextAlign.Center
        )
    }
}



@Preview(showBackground = true)
@Composable
fun BookingScreenPreview() {
    KoshiTheme {
        val bookingViewModel: BookingViewModel = hiltViewModel()
        val cartViewModel: CartViewModel = hiltViewModel()
        val detailViewModel: DetailViewModel = hiltViewModel()
        BookingScreen(
            navigateBack = {},
            cartViewModel = cartViewModel,
            detailViewModel = detailViewModel,
            onConfirmBooking = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarHeaderPreview() {
    KoshiTheme {
        CalendarGrid(
            dates = emptyList(),
            firstDayOffset = 0,
            selectedDate = null,
            isDateAvailable = { true },
            onDateSelected = {}
        )
    }
}
