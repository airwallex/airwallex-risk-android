package com.airwallex.risk

import kotlinx.serialization.json.Json
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals

class UUIDSerializerTest {

    @Test
    fun `test serialization`() {
        val uuid = UUID.randomUUID()
        val stringValue = Json.encodeToString(UUIDSerializer, uuid)
        val decodedUuid = Json.decodeFromString(UUIDSerializer, stringValue)
        assertEquals(uuid, decodedUuid)
    }
}