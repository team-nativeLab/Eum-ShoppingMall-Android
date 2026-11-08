package com.nativelap.eumshoppingmall.ui.preview

import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Compact · 320dp · 큰 글자",
    group = "반응형",
    widthDp = 320,
    heightDp = 800,
    fontScale = 1.3f,
)
@Preview(
    name = "Compact · 320dp · 최대 글자",
    group = "반응형",
    widthDp = 320,
    heightDp = 800,
    fontScale = 1.5f,
)
@Preview(
    name = "Phone · 390dp",
    group = "반응형",
    widthDp = 390,
    heightDp = 844,
)
@Preview(
    name = "Tablet · 800dp",
    group = "반응형",
    widthDp = 800,
    heightDp = 1200,
)
annotation class ResponsiveUiPreview
