package com.nativelap.eumshoppingmall.ui.events

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/** 오늘 날짜 기준 이벤트 기간 상태. 시작일과 종료일 당일은 진행 중이다. */
enum class EventPeriodStatus { Upcoming, Ongoing, Ended }

fun eventPeriodStatus(startDate: LocalDate, endDate: LocalDate, today: LocalDate): EventPeriodStatus = when {
    today.isBefore(startDate) -> EventPeriodStatus.Upcoming
    today.isAfter(endDate) -> EventPeriodStatus.Ended
    else -> EventPeriodStatus.Ongoing
}

/** 이벤트 목록 탭. 전체 탭은 모든 상태를 보여 준다. */
enum class EventListTab(private val periodStatus: EventPeriodStatus?) {
    All(null),
    Ongoing(EventPeriodStatus.Ongoing),
    Upcoming(EventPeriodStatus.Upcoming),
    Ended(EventPeriodStatus.Ended),
    ;

    fun matches(status: EventPeriodStatus): Boolean = periodStatus == null || periodStatus == status
}

fun List<EventUiModel>.filterFor(tab: EventListTab, today: LocalDate): List<EventUiModel> = filter { event -> tab.matches(eventPeriodStatus(event.startDate, event.endDate, today)) }

private val EventPeriodDateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

/** 화면에 보이는 날짜 문자열(예: 2026.11.01). 기간 구분 문구는 strings 리소스가 맡는다. */
fun formatEventDate(date: LocalDate): String = date.format(EventPeriodDateFormat)
