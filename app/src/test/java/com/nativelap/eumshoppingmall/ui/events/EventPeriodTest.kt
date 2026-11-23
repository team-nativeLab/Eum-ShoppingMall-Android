package com.nativelap.eumshoppingmall.ui.events

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class EventPeriodTest {
    private val start = LocalDate.of(2026, 11, 1)
    private val end = LocalDate.of(2026, 11, 30)

    @Test
    fun statusIsUpcomingBeforeStartDay() {
        assertEquals(EventPeriodStatus.Upcoming, eventPeriodStatus(start, end, LocalDate.of(2026, 10, 31)))
    }

    @Test
    fun startDayAndEndDayAreOngoing() {
        assertEquals(EventPeriodStatus.Ongoing, eventPeriodStatus(start, end, start))
        assertEquals(EventPeriodStatus.Ongoing, eventPeriodStatus(start, end, end))
    }

    @Test
    fun statusIsEndedAfterEndDay() {
        assertEquals(EventPeriodStatus.Ended, eventPeriodStatus(start, end, LocalDate.of(2026, 12, 1)))
    }

    @Test
    fun tabsFilterEventsByTodayStatus() {
        val today = LocalDate.of(2026, 11, 23)
        val events = listOf(
            EventUiModel("ongoing", "진행", start, end, 0),
            EventUiModel("upcoming", "예정", LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 31), 0),
            EventUiModel("ended", "종료", LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 30), 0),
        )

        assertEquals(listOf("ongoing", "upcoming", "ended"), events.filterFor(EventListTab.All, today).map { it.id })
        assertEquals(listOf("ongoing"), events.filterFor(EventListTab.Ongoing, today).map { it.id })
        assertEquals(listOf("upcoming"), events.filterFor(EventListTab.Upcoming, today).map { it.id })
        assertEquals(listOf("ended"), events.filterFor(EventListTab.Ended, today).map { it.id })
    }

    @Test
    fun dateTextUsesDotDateFormat() {
        assertEquals("2026.11.01", formatEventDate(start))
    }
}
