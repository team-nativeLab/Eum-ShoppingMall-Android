package com.nativelap.eumshoppingmall.debuggallery

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing

private val ViewportBackdropColor = Color(0xFFD9D9D9)
private val ViewportSurfaceColor = Color.White

@Composable
fun ScreenGalleryListScreen(
    entries: List<GalleryScreenEntry>,
    onEntryClick: (GalleryScreenEntry) -> Unit,
) {
    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        Text(
            text = stringResource(R.string.debug_gallery_title),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(EumSpacing.x16),
        )
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(entries, key = { galleryEntry -> galleryEntry.id }) { galleryEntry ->
                GalleryEntryRow(entry = galleryEntry, onClick = { onEntryClick(galleryEntry) })
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun GalleryEntryRow(
    entry: GalleryScreenEntry,
    onClick: () -> Unit,
) {
    val textColor =
        if (entry.isImplemented) {
            MaterialTheme.colorScheme.onBackground
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
    Row(
        modifier =
        Modifier
            .fillMaxWidth()
            .clickable(enabled = entry.isImplemented, onClick = onClick)
            .padding(horizontal = EumSpacing.x16, vertical = EumSpacing.x12),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = entry.title,
            style = MaterialTheme.typography.bodyLarge,
            color = textColor,
            modifier = Modifier.weight(1f),
        )
        if (!entry.isImplemented) {
            Text(
                text = stringResource(R.string.debug_gallery_unimplemented),
                style = MaterialTheme.typography.labelMedium,
                color = textColor,
            )
        }
    }
}

@Composable
fun ScreenGalleryDetailScreen(
    entry: GalleryScreenEntry,
    widthDp: Int,
    fontScale: Float,
    onWidthChange: (Int) -> Unit,
    onFontScaleChange: (Float) -> Unit,
    onBackClick: () -> Unit,
) {
    // 인셋은 컨트롤 바에만 소비하고 뷰포트 안쪽 화면에는 그대로 전달해 실제 기기와 같은 패딩이 적용되게 한다.
    // 인셋 값은 px 단위라 viewport의 density 축소와 무관하게 유지된다.
    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        GalleryControlBar(
            title = entry.title,
            widthDp = widthDp,
            fontScale = fontScale,
            onWidthChange = onWidthChange,
            onFontScaleChange = onFontScaleChange,
            onBackClick = onBackClick,
        )
        GalleryViewport(
            widthDp = widthDp,
            fontScale = fontScale,
            modifier = Modifier.weight(1f),
        ) {
            val previewRef = entry.preview
            val previewMethod = remember(previewRef) { previewRef?.let(::resolvePreviewMethod) }
            if (previewMethod != null) {
                GalleryPreviewContent(previewMethod)
            } else {
                Text(
                    text = stringResource(R.string.debug_gallery_missing_preview, entry.id),
                    modifier = Modifier.padding(EumSpacing.x16),
                )
            }
        }
    }
}

@Composable
private fun GalleryControlBar(
    title: String,
    widthDp: Int,
    fontScale: Float,
    onWidthChange: (Int) -> Unit,
    onFontScaleChange: (Float) -> Unit,
    onBackClick: () -> Unit,
) {
    Column(
        modifier =
        Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBackClick) { Text(stringResource(R.string.debug_gallery_back)) }
            Text(text = title, style = MaterialTheme.typography.titleMedium)
        }
        Row(
            modifier =
            Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = EumSpacing.x16),
            horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(stringResource(R.string.debug_gallery_width_label), style = MaterialTheme.typography.labelMedium)
            GalleryDisplayOptions.widthOptionsDp.forEach { optionWidthDp ->
                FilterChip(
                    selected = widthDp == optionWidthDp,
                    onClick = { onWidthChange(optionWidthDp) },
                    label = { Text(stringResource(R.string.debug_gallery_width_option, optionWidthDp)) },
                )
            }
            Text(stringResource(R.string.debug_gallery_font_scale_label), style = MaterialTheme.typography.labelMedium)
            GalleryDisplayOptions.fontScaleOptions.forEach { optionFontScale ->
                FilterChip(
                    selected = fontScale == optionFontScale,
                    onClick = { onFontScaleChange(optionFontScale) },
                    label = { Text(stringResource(R.string.debug_gallery_font_scale_option, optionFontScale.toString())) },
                )
            }
        }
    }
}

/**
 * 선택한 폭(dp)과 글자 배율을 시스템 설정 변경 없이 적용한다.
 * 기기 폭보다 넓으면 density를 줄여 같은 dp 레이아웃을 축소해 보여준다.
 */
@Composable
private fun GalleryViewport(
    widthDp: Int,
    fontScale: Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth().background(ViewportBackdropColor),
        contentAlignment = Alignment.TopCenter,
    ) {
        val deviceDensity = LocalDensity.current
        val deviceConfiguration = LocalConfiguration.current
        val fitRatio = if (widthDp > maxWidth.value) maxWidth.value / widthDp else 1f
        val viewportDensity = Density(deviceDensity.density * fitRatio, fontScale)
        val viewportConfiguration =
            remember(deviceConfiguration, widthDp, fontScale, fitRatio) {
                Configuration(deviceConfiguration).apply {
                    this.fontScale = fontScale
                    screenWidthDp = widthDp
                    screenHeightDp = (deviceConfiguration.screenHeightDp / fitRatio).toInt()
                }
            }
        CompositionLocalProvider(
            LocalDensity provides viewportDensity,
            LocalConfiguration provides viewportConfiguration,
        ) {
            Box(
                modifier =
                Modifier
                    .width(widthDp.dp)
                    .fillMaxHeight()
                    .background(ViewportSurfaceColor),
            ) {
                content()
            }
        }
    }
}
