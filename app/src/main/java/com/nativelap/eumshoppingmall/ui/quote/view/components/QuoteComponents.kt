package com.nativelap.eumshoppingmall.ui.quote.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.common.ResponsiveContentContainer
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteInquiryStatus
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun QuotePageScaffold(
    title: String,
    onBack: (() -> Unit)? = null,
    isCloseAction: Boolean = false,
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (Modifier) -> Unit,
) {
    Scaffold(
        topBar = {
            Box(Modifier.fillMaxWidth()) {
                Box(
                    Modifier.widthIn(max = 600.dp).fillMaxWidth().align(Alignment.Center),
                ) {
                    QuoteTopBar(title, onBack, isCloseAction)
                }
            }
        },
        bottomBar = {
            Box(Modifier.fillMaxWidth()) {
                Box(
                    Modifier.widthIn(max = 600.dp).fillMaxWidth().align(Alignment.Center),
                ) {
                    bottomBar()
                }
            }
        },
        containerColor = MaterialTheme.eumColors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        ResponsiveContentContainer(Modifier.fillMaxSize().padding(padding)) {
            content(Modifier.fillMaxSize())
        }
    }
}

@Composable
fun QuoteTopBar(title: String, onBack: (() -> Unit)?, isCloseAction: Boolean = false) {
    Row(
        Modifier.fillMaxWidth().statusBarsPadding().heightIn(min = 56.dp).padding(horizontal = EumSpacing.x8),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (onBack != null) {
            IconButton(onClick = onBack, modifier = Modifier.size(48.dp)) {
                if (isCloseAction) {
                    Icon(painterResource(R.drawable.ic_x), stringResource(R.string.common_close), tint = MaterialTheme.eumColors.textPrimary)
                } else {
                    Icon(painterResource(R.drawable.ic_arrow_left), stringResource(R.string.common_back), tint = MaterialTheme.eumColors.textPrimary)
                }
            }
        } else {
            Spacer(Modifier.size(48.dp))
        }
        Text(title, Modifier.weight(1f), style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)

        Spacer(Modifier.size(48.dp))
    }
}

@Composable
fun QuoteSectionTitle(text: String, required: Boolean = false, modifier: Modifier = Modifier) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(EumSpacing.x2)) {
        Text(text, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textPrimary)
        if (required) Text("*", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.danger)
    }
}

@Composable
fun QuoteFormTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isError: Boolean = false,
    helperText: String? = null,
    multiline: Boolean = false,
    showLabel: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    modifier: Modifier = Modifier,
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(EumSpacing.x6)) {
        if (showLabel) QuoteSectionTitle(label, required = true)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().heightIn(min = if (multiline) 96.dp else 48.dp)
                .semantics { contentDescription = label },
            placeholder = { Text(placeholder, style = MaterialTheme.typography.bodySmall) },
            singleLine = !multiline,
            minLines = if (multiline) 3 else 1,
            maxLines = if (multiline) 6 else 1,
            isError = isError,
            keyboardOptions = keyboardOptions,
            textStyle = MaterialTheme.typography.bodySmall,
            shape = RoundedCornerShape(8.dp),
        )
        helperText?.let { Text(it, style = MaterialTheme.typography.labelSmall, color = if (isError) MaterialTheme.eumColors.danger else MaterialTheme.eumColors.textTertiary) }
    }
}

@Composable
fun QuoteCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.eumColors.surface,
        border = BorderStroke(1.dp, MaterialTheme.eumColors.line),
    ) { Column(Modifier.padding(EumSpacing.x16), content = { content() }) }
}

@Composable
fun QuoteSummaryRow(label: String, value: String, modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12), verticalAlignment = Alignment.Top) {
        Text(label, modifier = Modifier.width(84.dp), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textTertiary)
        Text(value, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textPrimary)
    }
}

private class QuoteChipStyle(val labelRes: Int, val foreground: Color, val background: Color, val hasBorder: Boolean)

@Composable
fun QuoteStatusChip(status: QuoteInquiryStatus) {
    val style = when (status) {
        QuoteInquiryStatus.Received -> QuoteChipStyle(R.string.quote_status_received, MaterialTheme.eumColors.warning, MaterialTheme.eumColors.warningTint, hasBorder = false)
        QuoteInquiryStatus.Consulting -> QuoteChipStyle(R.string.quote_status_consulting, MaterialTheme.eumColors.textSecondary, Color.Transparent, hasBorder = true)
        QuoteInquiryStatus.EstimateSent -> QuoteChipStyle(R.string.quote_status_sent, MaterialTheme.eumColors.warning, MaterialTheme.eumColors.warningTint, hasBorder = false)
        QuoteInquiryStatus.Complete -> QuoteChipStyle(R.string.quote_status_complete, MaterialTheme.eumColors.success, MaterialTheme.eumColors.successTint, hasBorder = false)
        QuoteInquiryStatus.Cancelled -> QuoteChipStyle(R.string.quote_status_cancelled, MaterialTheme.eumColors.textTertiary, Color.Transparent, hasBorder = false)
    }
    val chipShape = RoundedCornerShape(4.dp)
    Text(
        stringResource(style.labelRes),
        Modifier
            .background(style.background, chipShape)
            .then(if (style.hasBorder) Modifier.border(1.dp, MaterialTheme.eumColors.lineControl, chipShape) else Modifier)
            .padding(horizontal = EumSpacing.x6, vertical = EumSpacing.x4),
        style = MaterialTheme.typography.labelSmall,
        color = style.foreground,
    )
}

@Composable
fun QuotePrimaryButton(label: String, onClick: () -> Unit, enabled: Boolean = true, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth().heightIn(min = 48.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.eumColors.textPrimary,
            contentColor = MaterialTheme.eumColors.inverse,
        ),
    ) { Text(label, style = MaterialTheme.typography.labelLarge) }
}

@Composable
fun QuoteSecondaryButton(label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().heightIn(min = 48.dp),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.eumColors.surface,
        border = BorderStroke(1.dp, MaterialTheme.eumColors.lineControl),
    ) {
        Box(contentAlignment = Alignment.Center) { Text(label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.eumColors.textPrimary) }
    }
}

@Composable
fun QuoteBottomActions(content: @Composable () -> Unit) {
    Column(
        Modifier.fillMaxWidth().background(MaterialTheme.eumColors.surface).navigationBarsPadding().imePadding().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x12),
        verticalArrangement = Arrangement.spacedBy(EumSpacing.x8),
    ) { content() }
}

@Composable
fun QuoteActionText(label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Text(label, modifier.heightIn(min = 48.dp).clickable(onClick = onClick).padding(horizontal = EumSpacing.x8, vertical = EumSpacing.x12), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textPrimary)
}
