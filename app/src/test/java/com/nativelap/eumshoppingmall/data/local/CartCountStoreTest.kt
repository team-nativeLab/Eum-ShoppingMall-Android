package com.nativelap.eumshoppingmall.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class CartCountStoreTest {
    @get:Rule
    val temporaryFolder = TemporaryFolder()

    @Test
    fun cartItemCountIsZeroWhenNothingIsSaved() = runBlocking {
        val cartCountStore = createCartCountStore()

        assertEquals(0, cartCountStore.cartItemCount.first())
    }

    @Test
    fun cartItemCountEmitsSavedValue() = runBlocking {
        val cartCountStore = createCartCountStore()

        cartCountStore.saveCartItemCount(3)

        assertEquals(3, cartCountStore.cartItemCount.first())
    }

    @Test
    fun cartItemCountIsZeroWhenStoredFileIsCorrupted() = runBlocking {
        val storeFile = temporaryFolder.newFile("corrupted_cart.preferences_pb")
        storeFile.writeBytes(byteArrayOf(0x01, 0x02, 0x03))
        val cartCountStore = CartCountStore(createDataStore(storeFile))

        assertEquals(0, cartCountStore.cartItemCount.first())
    }

    @Test(expected = IllegalArgumentException::class)
    fun saveRejectsNegativeCount() = runBlocking {
        createCartCountStore().saveCartItemCount(-1)
    }

    private fun createCartCountStore(): CartCountStore = CartCountStore(createDataStore(temporaryFolder.newFile("cart_test.preferences_pb")))

    private fun createDataStore(storeFile: File): DataStore<Preferences> = PreferenceDataStoreFactory.create(produceFile = { storeFile })
}
