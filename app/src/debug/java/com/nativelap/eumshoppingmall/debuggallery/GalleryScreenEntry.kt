package com.nativelap.eumshoppingmall.debuggallery

/** Preview 함수가 선언된 Kotlin 파일 클래스와 함수 이름. */
data class PreviewRef(
    val className: String,
    val functionName: String,
)

/** 갤러리 목록의 한 항목. [preview]가 null이면 코드가 없는 프레임(미구현)이다. */
data class GalleryScreenEntry(
    val id: String,
    val title: String,
    val preview: PreviewRef?,
) {
    val isImplemented: Boolean get() = preview != null
}
