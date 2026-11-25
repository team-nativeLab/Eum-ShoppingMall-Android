package com.nativelap.eumshoppingmall.debuggallery

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GalleryRegistryTest {
    @Test
    fun registryIds_areUnique() {
        val galleryIds = GalleryRegistry.entries.map { galleryEntry -> galleryEntry.id }
        assertEquals(galleryIds.size, galleryIds.toSet().size)
    }

    @Test
    fun registry_coversEveryFigmaFrameNumber() {
        val coveredNumbers =
            GalleryRegistry.entries
                .mapNotNull { galleryEntry -> Regex("^A-(\\d+)").find(galleryEntry.id)?.groupValues?.get(1)?.toInt() }
                .toSet()
        // 삭제된 기능의 프레임(A-15·A-25, 공지 A-40·A-41)은 결번이다.
        assertEquals((1..57).toSet() - setOf(15, 25, 40, 41), coveredNumbers)
    }

    @Test
    fun implementedEntries_resolveToPreviewMethods() {
        val unresolvedIds =
            GalleryRegistry.entries
                .filter { galleryEntry -> galleryEntry.isImplemented }
                .filter { galleryEntry -> resolvePreviewMethod(galleryEntry.preview!!) == null }
                .map { galleryEntry -> galleryEntry.id }
        assertTrue("Preview 함수를 찾지 못한 항목: $unresolvedIds", unresolvedIds.isEmpty())
    }

    @Test
    fun find_matchesIdIgnoringCase() {
        assertNotNull(GalleryRegistry.find("a-33"))
    }
}
