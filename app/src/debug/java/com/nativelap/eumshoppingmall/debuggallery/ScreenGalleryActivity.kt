package com.nativelap.eumshoppingmall.debuggallery

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme

/**
 * debug 전용 화면 갤러리. launcher에 노출하지 않고 adb로 실행한다.
 *
 * `adb shell am start -n com.nativelap.eumshoppingmall/.debuggallery.ScreenGalleryActivity --es screen A-33 --ei width 320 --ef fontScale 1.5`
 */
class ScreenGalleryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val launchRequest = intent.toGalleryLaunchRequest()
        val requestedScreenId = launchRequest.screenId
        if (requestedScreenId != null && GalleryRegistry.find(requestedScreenId) == null) {
            Log.w(TAG, "알 수 없는 screen id: $requestedScreenId")
            Toast.makeText(this, getString(R.string.debug_gallery_unknown_screen, requestedScreenId), Toast.LENGTH_LONG).show()
        }
        setContent {
            EumShoppingMallTheme {
                ScreenGalleryRoute(launchRequest = launchRequest)
            }
        }
    }
}

data class GalleryLaunchRequest(
    val screenId: String?,
    val widthDp: Int,
    val fontScale: Float,
)

private fun Intent.toGalleryLaunchRequest(): GalleryLaunchRequest {
    val requestedWidth = getIntExtra(EXTRA_WIDTH, GalleryDisplayOptions.DEFAULT_WIDTH_DP)
    val requestedFontScale = getFloatExtra(EXTRA_FONT_SCALE, GalleryDisplayOptions.DEFAULT_FONT_SCALE)
    return GalleryLaunchRequest(
        screenId = getStringExtra(EXTRA_SCREEN),
        widthDp = requestedWidth.coerceIn(GalleryDisplayOptions.MIN_WIDTH_DP, GalleryDisplayOptions.MAX_WIDTH_DP),
        fontScale = requestedFontScale.coerceIn(GalleryDisplayOptions.MIN_FONT_SCALE, GalleryDisplayOptions.MAX_FONT_SCALE),
    )
}

private const val EXTRA_SCREEN = "screen"
private const val EXTRA_WIDTH = "width"
private const val EXTRA_FONT_SCALE = "fontScale"

private const val TAG = "ScreenGallery"
