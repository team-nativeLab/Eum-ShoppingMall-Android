package com.nativelap.eumshoppingmall.ui.home

import org.junit.Assert.assertEquals
import org.junit.Test

class HomeCompanyInfoTest {
    @Test
    fun emptyIntroHasNoVisibleRows() {
        assertEquals(emptyList<HomeCompanyInfoRow>(), HomeCompanyInfoUiModel().visibleInfoRows())
    }

    @Test
    fun blankValuesAreHiddenAndVisibleRowsKeepFieldOrder() {
        val intro = HomeCompanyInfoUiModel(
            companyName = " 샘플 상호 ",
            representativeName = "",
            address = "서울특별시 샘플구",
            email = "   ",
        )

        assertEquals(
            listOf(
                HomeCompanyInfoRow(HomeCompanyInfoField.CompanyName, "샘플 상호"),
                HomeCompanyInfoRow(HomeCompanyInfoField.Address, "서울특별시 샘플구"),
            ),
            intro.visibleInfoRows(),
        )
    }

    @Test
    fun everyFilledFieldBecomesVisibleRowInFooterOrder() {
        val intro = HomeCompanyInfoUiModel(
            companyName = "샘플 상호",
            representativeName = "샘플 대표",
            businessRegistrationNumber = "000-00-00000",
            mailOrderSalesNumber = "샘플 신고번호",
            address = "샘플 주소",
            email = "sample@example.com",
            customerServiceHours = "평일 00:00 – 00:00",
        )

        assertEquals(HomeCompanyInfoField.values().toList(), intro.visibleInfoRows().map { row -> row.field })
    }
}
