package com.nativelap.eumshoppingmall.ui.catalog

import com.nativelap.eumshoppingmall.data.local.CartLine
import com.nativelap.eumshoppingmall.ui.home.HomeProductUiModel

// 옵션 없는 담기에 쓰는 기본 옵션 id. 상세 화면의 옵션 없는 담기와 같은 값이다.
const val CATALOG_DEFAULT_OPTION_ID = "default"

// 카드에서 바로 담을 수 있는 상품은 가격이 확정되고, 품절이 아니며, 옵션 없음이 명시된 상품이다.
// hasOptions가 null이면 옵션 정보가 없는 것이므로 담지 않는다.
fun canQuickAddToCart(product: CatalogProductUiModel): Boolean {
    val isPurchasable = product.price is CatalogPriceUiModel.Available
    val hasNoOptions = product.hasOptions == false
    return isPurchasable && !product.isSoldOut && hasNoOptions
}

// 카드 담기 버튼은 호출 측이 담기 동작을 넘긴 경우에만 보인다(담기 동작이 없는 화면은 넘기지 않는다).
fun quickCartButtonVisible(product: CatalogProductUiModel, hasQuickCartAction: Boolean): Boolean = hasQuickCartAction && canQuickAddToCart(product)

// 로그인 여부는 호출 측(Route)이 확인한다. 이 함수는 담을 줄 한 개만 만든다.
fun resolveQuickCartLine(product: CatalogProductUiModel, defaultOptionLabel: String): CartLine? {
    if (!canQuickAddToCart(product)) return null
    val price = product.price as? CatalogPriceUiModel.Available ?: return null
    return CartLine(
        productId = product.id,
        productName = product.name,
        optionId = CATALOG_DEFAULT_OPTION_ID,
        optionLabel = defaultOptionLabel,
        quantity = 1,
        optionExtraWon = 0L,
        productPriceLabel = price.currentLabel,
        isPriceUndisclosed = false,
        productUnitPriceWon = price.unitPriceWon,
    )
}

// 담기 한 번이 끝나기 전의 연타를 막는다. 상세의 닫기 게이트와 같은 방식이다.
internal class QuickCartGate {
    private var isAdding = false

    /** 담기를 시작할 수 있으면 true. 이미 담는 중이면 false. */
    fun tryEnter(): Boolean {
        if (isAdding) return false
        isAdding = true
        return true
    }

    fun exit() {
        isAdding = false
    }
}

// 홈 카드 담기 가능 여부. 목록 카드와 같은 조건이다: 가격 확정, 품절 아님, 옵션 없음이 명시됨.
fun canQuickAddToHomeProduct(product: HomeProductUiModel): Boolean = !product.isPurchaseUnavailable && product.priceLabel != null && !product.isSoldOut && product.hasOptions == false

// 홈 카드 담기 버튼은 담기 동작을 넘긴 경우에만 보인다.
fun homeQuickCartButtonVisible(product: HomeProductUiModel, hasQuickCartAction: Boolean): Boolean = hasQuickCartAction && canQuickAddToHomeProduct(product)

// 홈 카드에서 담을 줄 한 개. 가격 확정 조건을 통과한 상품만 만든다.
fun resolveHomeQuickCartLine(product: HomeProductUiModel, defaultOptionLabel: String): CartLine? {
    if (!canQuickAddToHomeProduct(product)) return null
    val priceLabel = product.priceLabel ?: return null
    return CartLine(
        productId = product.id,
        productName = product.name,
        optionId = CATALOG_DEFAULT_OPTION_ID,
        optionLabel = defaultOptionLabel,
        quantity = 1,
        optionExtraWon = 0L,
        productPriceLabel = priceLabel,
        isPriceUndisclosed = false,
        productUnitPriceWon = product.unitPriceWon,
    )
}

// 담기 버튼을 눌렀을 때의 다음 동작. 비로그인이면 담지 않고 로그인 화면으로 간다.
enum class QuickCartDecision {
    Add,
    RequireLogin,
}

fun decideQuickCart(isLoggedIn: Boolean): QuickCartDecision = if (isLoggedIn) QuickCartDecision.Add else QuickCartDecision.RequireLogin

// 담을 수 없는 일반 상품의 사유. 가격 미표시 상품은 사유 칩 없이 버튼만 없다.
enum class QuickCartBlockReason {
    SoldOut,
    OptionRequired,
}

fun quickCartBlockReason(product: CatalogProductUiModel): QuickCartBlockReason? {
    if (product.price !is CatalogPriceUiModel.Available) return null
    if (canQuickAddToCart(product)) return null
    return blockReasonOf(isSoldOut = product.isSoldOut, hasOptions = product.hasOptions)
}

fun homeQuickCartBlockReason(product: HomeProductUiModel): QuickCartBlockReason? {
    if (product.isPurchaseUnavailable || product.priceLabel == null) return null
    if (canQuickAddToHomeProduct(product)) return null
    return blockReasonOf(isSoldOut = product.isSoldOut, hasOptions = product.hasOptions)
}

// 품절이 옵션 필요보다 먼저 보인다.
private fun blockReasonOf(isSoldOut: Boolean, hasOptions: Boolean?): QuickCartBlockReason? = when {
    isSoldOut -> QuickCartBlockReason.SoldOut
    hasOptions == true -> QuickCartBlockReason.OptionRequired
    else -> null
}
