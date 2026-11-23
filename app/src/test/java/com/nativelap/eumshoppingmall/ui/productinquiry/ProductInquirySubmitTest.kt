package com.nativelap.eumshoppingmall.ui.productinquiry

import org.junit.Assert.assertEquals
import org.junit.Test

class ProductInquirySubmitTest {
    @Test
    fun submitShowsUnavailableNoticeInsteadOfSaving() {
        assertEquals(ProductInquirySubmitOutcome.UnavailableNotice, resolveProductInquirySubmit())
    }
}
