package com.nativelap.eumshoppingmall.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class CartCountStoreTest {
    @get:Rule
    val temporaryFolder = TemporaryFolder()

    @Test
    fun cartIsEmptyWhenNothingIsSaved() = runBlocking {
        val cartStore = createCartStore()

        assertTrue(cartStore.cartLines.first().isEmpty())
    }

    @Test
    fun savedLinesAreReadBackWithSpecialCharacters() = runBlocking {
        val cartStore = createCartStore()

        cartStore.addCartLines(listOf(line(productName = "안전모 | ABS\n특수%문자", optionLabel = "사이즈 L (+₩3,000)", quantity = 2)))

        val saved = cartStore.cartLines.first()
        assertEquals(1, saved.size)
        assertEquals("안전모 | ABS\n특수%문자", saved.single().productName)
        assertEquals("사이즈 L (+₩3,000)", saved.single().optionLabel)
        assertEquals(2, saved.single().quantity)
    }

    @Test
    fun addingSameProductAndOptionMergesQuantity() = runBlocking {
        val cartStore = createCartStore()
        cartStore.addCartLines(listOf(line(quantity = 2)))

        cartStore.addCartLines(listOf(line(quantity = 3)))

        assertEquals(listOf(5), cartStore.cartLines.first().map { it.quantity })
    }

    @Test
    fun corruptedStoreFileReadsAsEmptyCart() = runBlocking {
        val storeFile = temporaryFolder.newFile("corrupted_cart.preferences_pb")
        storeFile.writeBytes(byteArrayOf(0x01, 0x02, 0x03))
        val cartStore = CartCountStore(createDataStore(storeFile))

        assertTrue(cartStore.cartLines.first().isEmpty())
    }

    @Test
    fun addingLinesKeepsUndecodableStoredLinesOnDisk() = runBlocking {
        val storeFile = temporaryFolder.newFile("raw_cart.preferences_pb")
        val dataStore = createDataStore(storeFile)
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("cart_lines")] = "broken|line"
        }
        val cartStore = CartCountStore(dataStore)

        cartStore.addCartLines(listOf(line(productName = "추가")))

        val raw = dataStore.data.first()[stringPreferencesKey("cart_lines")].orEmpty()
        assertTrue(raw.lines().first() == "broken|line")
        assertEquals(1, cartStore.cartLines.first().size)
    }

    @Test
    fun quantityUpdateAndRemovalKeepBadgeEqualToStoredQuantitySum() = runBlocking {
        val cartStore = createCartStore()
        cartStore.addCartLines(listOf(line(quantity = 2), line(productName = "부츠", quantity = 1).copy(productId = "boots")))

        cartStore.updateCartLineQuantity("helmet/size-l", 4)
        assertEquals(5, cartStore.cartLines.first().totalQuantity())

        cartStore.removeCartLines(setOf("boots/size-l"))
        assertEquals(4, cartStore.cartLines.first().totalQuantity())
    }

    @Test
    fun quantityBelowOneIsStoredAsOne() = runBlocking {
        val cartStore = createCartStore()
        cartStore.addCartLines(listOf(line(quantity = 3)))

        cartStore.updateCartLineQuantity("helmet/size-l", 0)

        assertEquals(1, cartStore.cartLines.first().single().quantity)
    }

    @Test
    fun unknownLineIdIsIgnoredByQuantityUpdateAndRemoval() = runBlocking {
        val cartStore = createCartStore()
        cartStore.addCartLines(listOf(line(quantity = 2)))

        cartStore.updateCartLineQuantity("missing/none", 9)
        cartStore.removeCartLines(setOf("missing/none"))

        val lines = cartStore.cartLines.first()
        assertEquals(listOf(2), lines.map { it.quantity })
        assertEquals(2, lines.totalQuantity())
    }

    @Test
    fun linesAndBadgeSumSurviveReopeningTheStoreFile() = runBlocking {
        val storeFile = temporaryFolder.newFile("persist_cart.preferences_pb")
        val firstJob = Job()
        // 첫 저장 객체의 범위를 명시적으로 끝내야 같은 파일을 다시 열 수 있다.
        val firstStore = CartCountStore(
            PreferenceDataStoreFactory.create(scope = CoroutineScope(Dispatchers.IO + firstJob), produceFile = { storeFile }),
        )
        firstStore.addCartLines(listOf(line(quantity = 2)))
        firstStore.updateCartLineQuantity("helmet/size-l", 3)
        firstJob.cancelAndJoin()

        val reopened = CartCountStore(createDataStore(storeFile))
        val lines = reopened.cartLines.first()

        assertEquals(1, lines.size)
        assertEquals(3, lines.totalQuantity())
    }

    private fun line(
        productName: String = "안전모",
        optionLabel: String = "사이즈 L",
        quantity: Int = 1,
    ): CartLine = CartLine(
        productId = "helmet",
        productName = productName,
        optionId = "size-l",
        optionLabel = optionLabel,
        quantity = quantity,
        optionExtraWon = 3_000L,
        productPriceLabel = "₩18,000",
        isQuoteRequired = false,
    )

    private fun createCartStore(): CartCountStore = CartCountStore(createDataStore(temporaryFolder.newFile("cart_test.preferences_pb")))

    private fun createDataStore(storeFile: File): DataStore<Preferences> = PreferenceDataStoreFactory.create(produceFile = { storeFile })
}
