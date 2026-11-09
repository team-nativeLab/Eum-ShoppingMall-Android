package com.nativelap.eumshoppingmall.navigation

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.elementDescriptors
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * 프로세스 종료 후 back stack 복원에 필요한 NavKey 직렬화 왕복을 확인한다.
 *
 * 실제 저장 형식(SavedState Bundle)은 JVM 단위 테스트에서 만들 수 없어, kotlinx.serialization core만 쓰는 순차 목록 형식으로
 * 생성된 serializer의 encode→decode 왕복을 검증한다. JSON 등 별도 format dependency를 추가하지 않기 위한 선택이다.
 */
class EumDestinationSerializationTest {
    private val nestedLoginDestination = EumDestination.Login(
        pendingDestination = EumDestination.QuoteInquiryDetail(inquiryId = "inquiry-1"),
    )

    private val sampleDestinations: List<EumDestination> = listOf(
        EumDestination.Home,
        EumDestination.Categories,
        EumDestination.Search,
        EumDestination.Cart,
        EumDestination.MyPage,
        EumDestination.Drawer,
        EumDestination.ProductList(),
        EumDestination.ProductList(categoryId = "protective"),
        EumDestination.SearchResults(query = "안전모"),
        EumDestination.ProductDetail(productId = "p1"),
        EumDestination.CheckoutAddress,
        EumDestination.CheckoutPayment,
        EumDestination.OrderComplete,
        EumDestination.QuoteRequest(),
        EumDestination.QuoteRequest(productId = "p1"),
        EumDestination.QuoteProductPicker,
        EumDestination.QuoteComplete,
        EumDestination.QuoteHistory,
        EumDestination.QuoteInquiryDetail(inquiryId = "inquiry-1"),
        EumDestination.OrderHistory,
        EumDestination.Favorites,
        EumDestination.RecentProducts,
        EumDestination.Coupons,
        EumDestination.Points,
        EumDestination.InquiryHistory,
        EumDestination.CustomerSupport,
        EumDestination.NoticeList,
        EumDestination.EventList,
        EumDestination.CompanyIntro,
        EumDestination.Login(),
        nestedLoginDestination,
        EumDestination.Login(pendingDestination = EumDestination.Login(pendingDestination = EumDestination.Coupons)),
    )

    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun samplesCoverEverySealedSubtype() {
        // sealed serializer의 두 번째 요소가 하위 타입 목록이다. 새 목적지를 추가하고 샘플을 빠뜨리면 여기서 실패한다.
        val declaredSerialNames = EumDestination.serializer().descriptor
            .getElementDescriptor(1)
            .elementDescriptors
            .map { subtypeDescriptor -> subtypeDescriptor.serialName }
            .toSet()
        val sampledSerialNames = sampleDestinations
            .map { destination -> EumDestination.serializer().findSerialNameOf(destination) }
            .toSet()

        assertEquals(declaredSerialNames, sampledSerialNames)
    }

    @Test
    fun everyDestinationRoundTripsThroughSealedSerializer() {
        sampleDestinations.forEach { destination ->
            val encodedValues = encodeToList(EumDestination.serializer(), destination)
            val decodedDestination = decodeFromList(EumDestination.serializer(), encodedValues)

            assertEquals(destination, decodedDestination)
        }
    }

    @Test
    fun nestedLoginKeepsPendingDestination() {
        val encodedValues = encodeToList(EumDestination.Login.serializer(), nestedLoginDestination)
        val decodedLogin = decodeFromList(EumDestination.Login.serializer(), encodedValues)

        assertEquals(EumDestination.QuoteInquiryDetail(inquiryId = "inquiry-1"), decodedLogin.pendingDestination)
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun kotlinx.serialization.KSerializer<EumDestination>.findSerialNameOf(destination: EumDestination): String {
        val serialNameEncoder = SerialNameCaptureEncoder()
        serialize(serialNameEncoder, destination)
        return serialNameEncoder.capturedSerialName ?: error("직렬화 이름을 찾지 못했습니다: $destination")
    }

    private fun <T> encodeToList(serializer: SerializationStrategy<T>, value: T): List<Any?> {
        val listEncoder = SequentialListEncoder()
        listEncoder.encodeSerializableValue(serializer, value)
        return listEncoder.encodedValues
    }

    private fun <T> decodeFromList(deserializer: DeserializationStrategy<T>, encodedValues: List<Any?>): T = SequentialListDecoder(ArrayDeque(encodedValues)).decodeSerializableValue(deserializer)
}

/** 모든 원시 값과 null 표시를 순서대로 목록에 쌓는 테스트 전용 encoder. */
@OptIn(ExperimentalSerializationApi::class)
private class SequentialListEncoder : AbstractEncoder() {
    val encodedValues = mutableListOf<Any?>()

    override val serializersModule: SerializersModule = EmptySerializersModule()

    override fun encodeValue(value: Any) {
        encodedValues.add(value)
    }

    override fun encodeNull() {
        encodedValues.add(NULL_MARK)
    }

    override fun encodeNotNullMark() {
        encodedValues.add(NOT_NULL_MARK)
    }
}

/** [SequentialListEncoder]가 쌓은 순서 그대로 읽는 테스트 전용 decoder. */
@OptIn(ExperimentalSerializationApi::class)
private class SequentialListDecoder(
    private val remainingValues: ArrayDeque<Any?>,
    private val elementCount: Int = 0,
) : AbstractDecoder() {
    private var elementIndex = 0

    override val serializersModule: SerializersModule = EmptySerializersModule()

    override fun decodeValue(): Any = remainingValues.removeFirst() ?: error("null 값은 decodeNotNullMark로 먼저 확인해야 합니다.")

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        if (elementIndex == elementCount) return CompositeDecoder.DECODE_DONE
        return elementIndex++
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder = SequentialListDecoder(remainingValues, descriptor.elementsCount)

    override fun decodeSequentially(): Boolean = true

    override fun decodeNotNullMark(): Boolean = remainingValues.removeFirst() == NOT_NULL_MARK

    override fun decodeNull(): Nothing? = null
}

/** sealed serializer가 처음 기록하는 하위 타입 이름만 붙잡는 encoder. */
@OptIn(ExperimentalSerializationApi::class)
private class SerialNameCaptureEncoder : AbstractEncoder() {
    var capturedSerialName: String? = null

    override val serializersModule: SerializersModule = EmptySerializersModule()

    override fun encodeValue(value: Any) {
        if (capturedSerialName == null && value is String) {
            capturedSerialName = value
        }
    }

    override fun encodeNull() = Unit
}

private const val NULL_MARK = "<null>"
private const val NOT_NULL_MARK = "<not-null>"
