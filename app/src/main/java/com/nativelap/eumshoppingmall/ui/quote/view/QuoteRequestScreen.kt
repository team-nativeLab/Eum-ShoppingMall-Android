package com.nativelap.eumshoppingmall.ui.quote.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductUiModel
import com.nativelap.eumshoppingmall.ui.quote.view.components.QuoteBottomActions
import com.nativelap.eumshoppingmall.ui.quote.view.components.QuoteFormTextField
import com.nativelap.eumshoppingmall.ui.quote.view.components.QuotePageScaffold
import com.nativelap.eumshoppingmall.ui.quote.view.components.QuotePrimaryButton
import com.nativelap.eumshoppingmall.ui.quote.view.components.QuoteSectionTitle
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteAttachmentUiModel
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteRequestEvent
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteRequestField
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteRequestType
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteRequestUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.EumTypographyTokens
import com.nativelap.eumshoppingmall.ui.theme.eumCheckboxColors
import com.nativelap.eumshoppingmall.ui.theme.eumColors

private const val MAX_REQUEST_DETAILS_LENGTH = 1_000

@Composable
fun QuoteRequestScreen(
    uiModel: QuoteRequestUiModel,
    onEvent: (QuoteRequestEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    QuotePageScaffold(
        title = stringResource(R.string.quote_page_title),
        onBack = { onEvent(QuoteRequestEvent.BackClicked) },
        bottomBar = {
            QuoteBottomActions {
                QuotePrimaryButton(
                    label = stringResource(R.string.quote_submit_label),
                    onClick = { onEvent(QuoteRequestEvent.SubmitClicked) },
                    enabled = uiModel.isSubmitEnabled,
                )
            }
        },
    ) { contentModifier ->
        LazyColumn(
            modifier = contentModifier.fillMaxSize().padding(horizontal = EumSpacing.x20),
            contentPadding = PaddingValues(top = EumSpacing.x12, bottom = EumSpacing.x24),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x20),
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
                    Text(
                        stringResource(R.string.quote_custom_order_english).uppercase(),
                        style = EumTypographyTokens.overline,
                        color = MaterialTheme.eumColors.textTertiary,
                    )
                    Text(
                        stringResource(R.string.quote_safety_solution_lines),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.eumColors.textPrimary,
                    )

                    Spacer(Modifier.height(EumSpacing.x8))

                    QuoteProgressGuide()
                }
            }
            item {
                QuoteSectionTitle(stringResource(R.string.quote_interested_products), required = true)

                Spacer(Modifier.height(EumSpacing.x8))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.eumColors.surface,
                    border = BorderStroke(1.dp, MaterialTheme.eumColors.line),
                ) {
                    Column(Modifier.padding(EumSpacing.x16), verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text(stringResource(R.string.quote_inquiry_product_count, uiModel.products.size), modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleLarge)
                            Text(
                                stringResource(R.string.quote_product_add),
                                modifier = Modifier.heightIn(min = 48.dp).clickable { onEvent(QuoteRequestEvent.AddProductClicked) }.padding(horizontal = EumSpacing.x8, vertical = EumSpacing.x12),
                                style = MaterialTheme.typography.labelSmall,
                            )
                        }
                        uiModel.products.forEach { product -> QuoteProductRow(product) { onEvent(QuoteRequestEvent.ProductRemoved(product.id)) } }
                    }
                }
            }
            item {
                QuoteSectionTitle(stringResource(R.string.quote_inquiry_type), required = true)
                Spacer(Modifier.height(EumSpacing.x8))

                FlowRow(
                    modifier = Modifier.fillMaxWidth().selectableGroup(),
                    horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12),
                ) {
                    QuoteRequestType.entries.forEach { type ->
                        val selected = type == uiModel.requestType
                        Row(
                            modifier = Modifier.heightIn(min = 48.dp)
                                .selectable(selected = selected, role = Role.RadioButton) {
                                    onEvent(QuoteRequestEvent.RequestTypeSelected(type))
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                        ) {
                            QuoteRadioMark(selected)
                            Text(stringResource(type.labelRes), style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
            item {
                QuoteCardNotice(stringResource(R.string.quote_required_fields_notice))
            }
            item {
                QuoteFormTextField(
                    label = stringResource(R.string.quote_company_name),
                    value = uiModel.companyName,
                    onValueChange = { onEvent(QuoteRequestEvent.FieldChanged(QuoteRequestField.Company, it)) },
                    placeholder = stringResource(R.string.quote_company_placeholder),
                    isError = QuoteRequestField.Company in uiModel.errors,
                )
            }
            item {
                QuoteFormTextField(
                    label = stringResource(R.string.quote_contact_name),
                    value = uiModel.contactName,
                    onValueChange = { onEvent(QuoteRequestEvent.FieldChanged(QuoteRequestField.ContactName, it)) },
                    placeholder = stringResource(R.string.quote_contact_placeholder),
                    isError = QuoteRequestField.ContactName in uiModel.errors,
                )
            }
            item {
                QuoteFormTextField(
                    label = stringResource(R.string.quote_phone_number),
                    value = uiModel.phoneNumber,
                    onValueChange = { onEvent(QuoteRequestEvent.FieldChanged(QuoteRequestField.Phone, it)) },
                    placeholder = stringResource(R.string.quote_phone_placeholder),
                    isError = QuoteRequestField.Phone in uiModel.errors,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                )
            }
            item {
                QuoteFormTextField(
                    label = stringResource(R.string.quote_email_address),
                    value = uiModel.email,
                    onValueChange = { onEvent(QuoteRequestEvent.FieldChanged(QuoteRequestField.Email, it)) },
                    placeholder = stringResource(R.string.quote_email_placeholder),
                    isError = QuoteRequestField.Email in uiModel.errors,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                )
            }
            item {
                QuoteSectionTitle(stringResource(R.string.quote_delivery_date), required = true)

                Spacer(Modifier.height(EumSpacing.x6))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    QuoteFormTextField(
                        label = stringResource(R.string.quote_delivery_date),
                        value = uiModel.preferredDeliveryDate,
                        onValueChange = { onEvent(QuoteRequestEvent.FieldChanged(QuoteRequestField.DeliveryDate, it)) },
                        placeholder = stringResource(R.string.quote_due_date_placeholder),
                        isError = QuoteRequestField.DeliveryDate in uiModel.errors,
                        helperText = if (QuoteRequestField.DeliveryDate in uiModel.errors) {
                            stringResource(R.string.quote_delivery_date_invalid)
                        } else {
                            null
                        },
                        modifier = Modifier.weight(1f),
                        showLabel = false,
                    )
                    IconButton(onClick = { onEvent(QuoteRequestEvent.DatePickerClicked) }, modifier = Modifier.size(48.dp)) {
                        Icon(painterResource(R.drawable.ic_calendar), stringResource(R.string.quote_delivery_date))
                    }
                }
            }
            item {
                QuoteFormTextField(
                    label = stringResource(R.string.quote_delivery_area_label),
                    value = uiModel.deliveryAddress,
                    onValueChange = { onEvent(QuoteRequestEvent.FieldChanged(QuoteRequestField.DeliveryAddress, it)) },
                    placeholder = stringResource(R.string.quote_delivery_address_placeholder),
                    isError = QuoteRequestField.DeliveryAddress in uiModel.errors,
                )
            }
            item {
                QuoteFormTextField(
                    label = stringResource(R.string.quote_details_label),
                    value = uiModel.requestDetails,
                    onValueChange = {
                        onEvent(
                            QuoteRequestEvent.FieldChanged(
                                QuoteRequestField.Details,
                                it.take(MAX_REQUEST_DETAILS_LENGTH),
                            ),
                        )
                    },
                    placeholder = stringResource(R.string.quote_details_placeholder),
                    multiline = true,
                    helperText = "${uiModel.requestDetails.length}/$MAX_REQUEST_DETAILS_LENGTH",
                    isError = QuoteRequestField.Details in uiModel.errors,
                )
            }
            item {
                Surface(
                    onClick = { onEvent(QuoteRequestEvent.AddAttachmentClicked) },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 96.dp).dashedBorder(MaterialTheme.eumColors.lineControl, 12.dp, 1.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.eumColors.background,
                ) {
                    Column(
                        Modifier.padding(EumSpacing.x12),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                            Icon(painterResource(R.drawable.ic_paperclip), null, modifier = Modifier.size(18.dp))
                            Text(stringResource(R.string.quote_attachment_add), style = MaterialTheme.typography.labelMedium)
                        }
                        Text(stringResource(R.string.quote_attachment_guidance), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
                    }
                }
            }
            if (uiModel.attachments.isNotEmpty()) {
                items(uiModel.attachments, key = { it.id }) { attachment ->
                    QuoteAttachmentRow(attachment) { onEvent(QuoteRequestEvent.AttachmentRemoved(attachment.id)) }
                }
            }
            item {
                Row(
                    Modifier.fillMaxWidth().heightIn(min = 48.dp).clickable { onEvent(QuoteRequestEvent.PrivacyConsentChanged(!uiModel.isPrivacyConsentChecked)) },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(checked = uiModel.isPrivacyConsentChecked, colors = eumCheckboxColors(), onCheckedChange = { onEvent(QuoteRequestEvent.PrivacyConsentChanged(it)) })
                    Text(stringResource(R.string.quote_privacy_consent), modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelSmall)
                    Text(
                        stringResource(R.string.quote_privacy_details),
                        modifier = Modifier.heightIn(min = 48.dp)
                            .clickable(role = Role.Button) { onEvent(QuoteRequestEvent.PrivacyDetailsClicked) }
                            .padding(horizontal = EumSpacing.x8),
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
            if (!uiModel.isSubmitEnabled) {
                item {
                    Text(
                        stringResource(R.string.quote_submit_disabled_notice),
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.eumColors.textTertiary,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
private fun QuoteProductRow(product: CatalogProductUiModel, onRemove: () -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Surface(Modifier.size(48.dp), shape = RoundedCornerShape(6.dp), color = MaterialTheme.eumColors.surfaceSecondary) {
            val imageResId = product.imageResId
            if (imageResId == null) {
                Icon(
                    painterResource(R.drawable.ic_image),
                    product.imageDescription ?: product.name,
                    modifier = Modifier.padding(EumSpacing.x12),
                    tint = MaterialTheme.eumColors.placeholderGlyph,
                )
            } else {
                Image(
                    painter = painterResource(imageResId),
                    contentDescription = product.imageDescription ?: product.name,
                    modifier = Modifier.clip(RoundedCornerShape(6.dp)),
                    contentScale = ContentScale.Crop,
                )
            }
        }
        Column(Modifier.weight(1f).padding(horizontal = EumSpacing.x8)) {
            Text(product.name, style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
            product.subtitle?.let { Text(it, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary, maxLines = 1, overflow = TextOverflow.Ellipsis) }
        }
        IconButton(onClick = onRemove, modifier = Modifier.size(48.dp)) {
            Icon(painterResource(R.drawable.ic_x), stringResource(R.string.quote_product_remove, product.name), modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun QuoteProgressGuide() {
    val progressSteps =
        listOf(
            R.string.quote_progress_step_received,
            R.string.quote_progress_step_consulting,
            R.string.quote_progress_step_estimate,
            R.string.quote_progress_step_production,
        )
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.eumColors.surface,
        border = BorderStroke(1.dp, MaterialTheme.eumColors.line),
    ) {
        // 접근성 설명은 quote_breadcrumb 하나로 읽힌다. 단계 문구를 바꾸면 quote_progress_step_*과 함께 고친다.
        val breadcrumbDescription = stringResource(R.string.quote_breadcrumb)
        // 좁은 폭·큰 글자에서는 단계 단위로 다음 줄에 내려가 라벨이 한 글자씩 깨지지 않는다.
        FlowRow(
            Modifier.fillMaxWidth().heightIn(min = 40.dp).padding(horizontal = EumSpacing.x12, vertical = EumSpacing.x8)
                .clearAndSetSemantics { contentDescription = breadcrumbDescription },
            horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x4, Alignment.CenterVertically),
            itemVerticalAlignment = Alignment.CenterVertically,
        ) {
            progressSteps.forEachIndexed { index, stepLabelRes ->
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                    if (index > 0) {
                        Icon(
                            painterResource(R.drawable.ic_chevron_right),
                            null,
                            modifier = Modifier.size(12.dp),
                            tint = MaterialTheme.eumColors.textTertiary,
                        )
                    }
                    Text(
                        stringResource(stepLabelRes),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.eumColors.textSecondary,
                    )
                }
            }
        }
    }
}

// Figma 라디오 14dp. 안쪽 점 6dp·테두리 1dp는 Figma에서 확인하지 못한 임시값이며, 터치 영역은 부모 Row(48dp)가 담당한다.
@Composable
private fun QuoteRadioMark(isSelected: Boolean) {
    val markColor = if (isSelected) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.lineControl
    Box(
        modifier = Modifier.size(14.dp).border(1.dp, markColor, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        if (isSelected) Box(Modifier.size(6.dp).background(markColor, CircleShape))
    }
}

@Composable
private fun QuoteCardNotice(text: String) {
    Row(
        Modifier.fillMaxWidth()
            .background(MaterialTheme.eumColors.surface, RoundedCornerShape(8.dp))
            .border(1.dp, MaterialTheme.eumColors.lineControl, RoundedCornerShape(8.dp))
            .padding(horizontal = EumSpacing.x12, vertical = EumSpacing.x8),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(painterResource(R.drawable.ic_info), null, tint = MaterialTheme.eumColors.textSecondary, modifier = Modifier.size(16.dp))
        Text(text, Modifier.padding(start = EumSpacing.x8), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textSecondary)
    }
}

@Composable
private fun QuoteAttachmentRow(attachment: QuoteAttachmentUiModel, onRemove: () -> Unit) {
    Row(Modifier.fillMaxWidth().heightIn(min = 48.dp).padding(horizontal = EumSpacing.x8), verticalAlignment = Alignment.CenterVertically) {
        Icon(painterResource(R.drawable.ic_paperclip), null, modifier = Modifier.size(16.dp))
        Text("${attachment.fileName} · ${attachment.sizeLabel}", Modifier.weight(1f).padding(start = EumSpacing.x8), style = MaterialTheme.typography.labelSmall, maxLines = 1)
        IconButton(onClick = onRemove, modifier = Modifier.size(48.dp)) {
            Icon(painterResource(R.drawable.ic_x), stringResource(R.string.quote_attachment_remove, attachment.fileName), modifier = Modifier.size(16.dp))
        }
    }
}

// BorderStroke는 점선을 지원하지 않아 콘텐츠 위에 직접 그린다.
private fun Modifier.dashedBorder(color: Color, cornerRadius: Dp, strokeWidth: Dp): Modifier = drawWithContent {
    drawContent()
    val strokePx = strokeWidth.toPx()
    drawRoundRect(
        color = color,
        topLeft = Offset(strokePx / 2, strokePx / 2),
        size = Size(size.width - strokePx, size.height - strokePx),
        cornerRadius = CornerRadius(cornerRadius.toPx()),
        style = Stroke(width = strokePx, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f))),
    )
}

private val QuoteRequestType.labelRes: Int
    get() = when (this) {
        QuoteRequestType.CustomManufacturing -> R.string.quote_custom_manufacturing
        QuoteRequestType.BulkPurchase -> R.string.quote_bulk_purchase
        QuoteRequestType.RegularDelivery -> R.string.quote_regular_delivery
        QuoteRequestType.Other -> R.string.quote_other
    }
