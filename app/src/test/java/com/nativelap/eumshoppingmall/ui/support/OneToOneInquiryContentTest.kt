package com.nativelap.eumshoppingmall.ui.support

import org.junit.Assert.assertEquals
import org.junit.Test

class OneToOneInquiryContentTest {
    @Test
    fun contentLongerThanLimitIsTruncated() {
        val longContent = "가".repeat(ONE_TO_ONE_INQUIRY_CONTENT_MAX_LENGTH + 5)

        assertEquals(ONE_TO_ONE_INQUIRY_CONTENT_MAX_LENGTH, limitOneToOneInquiryContent(longContent).length)
    }

    @Test
    fun contentWithinLimitIsKept() {
        assertEquals("문의 내용입니다.", limitOneToOneInquiryContent("문의 내용입니다."))
    }
}
