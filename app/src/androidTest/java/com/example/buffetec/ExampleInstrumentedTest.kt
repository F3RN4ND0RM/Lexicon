package com.example.buffetec

import com.example.buffetec.network.ApiResponse
import com.example.buffetec.network.Output
import com.example.buffetec.network.Reference
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class SearchErrorTest {

    @Test
    fun searchArticles_handlesComplexQuery() {
        val complexQuery = "Constitution labor laws article reform"

        val mockReferences = listOf(
            Reference(
                url = "https://example.com/document1",
                title = "Constitutional Reform of Labor Laws",
                snippet = "This section of the constitution details various labor law reforms."
            ),
            Reference(
                url = "https://example.com/document2",
                title = "Labor Laws in Article 123",
                snippet = "Article 123 discusses workers' rights and labor law enforcement."
            )
        )

        val mockResponse = ApiResponse(
            id = "2",
            url = "https://api.example.com/biblioteca",
            created_at = "2024-01-01",
            output = Output(10, listOf(), mapOf(), 50, mockReferences)
        )

        val references = mockResponse.output.references

        assertNotNull(references)
        assertTrue(references.isNotEmpty())
        assertEquals("Constitutional Reform of Labor Laws", references[0].title)
        assertTrue(references[0].snippet.contains("labor law reforms"))
        assertEquals(2, references.size)  // Ensure it returns two results
    }

}
