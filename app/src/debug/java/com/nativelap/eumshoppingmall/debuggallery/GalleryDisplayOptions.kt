package com.nativelap.eumshoppingmall.debuggallery

/** 화면 폭·글자 크기 토글 후보와 adb extra 허용 범위. */
object GalleryDisplayOptions {
    const val DEFAULT_WIDTH_DP = 390
    const val DEFAULT_FONT_SCALE = 1.0f
    const val MIN_WIDTH_DP = 240
    const val MAX_WIDTH_DP = 1200
    const val MIN_FONT_SCALE = 0.85f
    const val MAX_FONT_SCALE = 2.0f

    val widthOptionsDp = listOf(320, 390, 800)
    val fontScaleOptions = listOf(1.0f, 1.3f, 1.5f)
}
