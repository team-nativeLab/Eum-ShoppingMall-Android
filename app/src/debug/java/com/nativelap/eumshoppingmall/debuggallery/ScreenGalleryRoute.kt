package com.nativelap.eumshoppingmall.debuggallery

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

/** 목록·상세 전환과 폭·글자 크기 상태를 소유한다. */
@Composable
fun ScreenGalleryRoute(
    launchRequest: GalleryLaunchRequest,
) {
    var selectedScreenId by rememberSaveable { mutableStateOf<String?>(launchRequest.screenId) }
    var widthDp by rememberSaveable { mutableStateOf(launchRequest.widthDp) }
    var fontScale by rememberSaveable { mutableStateOf(launchRequest.fontScale) }
    val selectedEntry = selectedScreenId?.let(GalleryRegistry::find)

    BackHandler(enabled = selectedEntry != null) { selectedScreenId = null }

    if (selectedEntry == null) {
        ScreenGalleryListScreen(
            entries = GalleryRegistry.entries,
            onEntryClick = { clickedEntry -> selectedScreenId = clickedEntry.id },
        )
    } else {
        ScreenGalleryDetailScreen(
            entry = selectedEntry,
            widthDp = widthDp,
            fontScale = fontScale,
            onWidthChange = { widthDp = it },
            onFontScaleChange = { fontScale = it },
            onBackClick = { selectedScreenId = null },
        )
    }
}
