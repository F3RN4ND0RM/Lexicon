package com.example.buffetec

import com.example.buffetec.network.ApiResponse
import com.example.buffetec.network.Output
import com.example.buffetec.network.Reference
import encodeQuery
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class SearchErrorTest {

    @Test
    fun searchArticles_handlesNullAndEmptyInputs() {
        val invalidQuery1: String? = null
        val invalidQuery2 = ""

        val encodedQuery1 = invalidQuery1?.let { encodeQuery(it) } ?: "nulo"
        val encodedQuery2 = encodeQuery(invalidQuery2)

        assertEquals("nulo", encodedQuery1)
        assertEquals("", encodedQuery2)
    }
}
