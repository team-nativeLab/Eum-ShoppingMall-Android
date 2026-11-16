package com.nativelap.eumshoppingmall.debuggallery

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composer
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.runtime.currentComposer
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * Preview 함수는 private이라 직접 호출할 수 없다.
 * Compose 컴파일러가 만든 `(Composer, Int)` 시그니처를 리플렉션으로 찾아 기존 fixture를 그대로 재사용한다.
 */
fun resolvePreviewMethod(previewRef: PreviewRef): Method? = runCatching {
    Class
        .forName(previewRef.className)
        .getDeclaredMethod(
            previewRef.functionName,
            Composer::class.java,
            Int::class.javaPrimitiveType,
        ).also { previewMethod -> previewMethod.isAccessible = true }
}.getOrNull()

@OptIn(InternalComposeApi::class)
@Composable
fun GalleryPreviewContent(
    previewMethod: Method,
) {
    invokePreview(previewMethod, currentComposer)
}

// changed=0이면 Compose가 파라미터가 바뀐 것으로 보고 항상 본문을 실행한다.
// Composable 실행 중 예외를 삼키면 Composer 그룹 상태가 깨지므로 원인 예외만 풀어 로그로 남기고 다시 던진다.
// try/catch는 Composable 안에서 쓸 수 없어 일반 함수로 분리했다.
private fun invokePreview(
    previewMethod: Method,
    composer: Composer,
) {
    try {
        previewMethod.invoke(null, composer, 0)
    } catch (invocationError: InvocationTargetException) {
        val previewError = invocationError.targetException ?: invocationError
        Log.e(TAG, "Preview 실행 실패: ${previewMethod.name}", previewError)
        throw previewError
    }
}

private const val TAG = "ScreenGallery"
