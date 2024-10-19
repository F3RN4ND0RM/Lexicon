package com.example.buffetec

import com.example.buffetec.network.ApiResponse
import com.example.buffetec.network.Output
import com.example.buffetec.network.Reference
import org.junit.Assert.*
import org.junit.Test

class SearchUnitTest {

    @Test
    fun searchArticles_returnsReferences() {
        val mockReferences = listOf(
            Reference(url = "https://example.com/document1",
                title = "Document Title 1",
                snippet = "This is a snippet of the first document content."),
            Reference(url = "https://example.com/document2",
                title = "Document Title 2",
                snippet = "This is a snippet of the second document content.")
        )
        val mockResponse = ApiResponse(
            id = "1",
            url = "https://api.example.com/biblioteca",
            created_at = "2024-01-01",
            output = Output(10, listOf(), mapOf(), 50, mockReferences)
        )

        val references = mockResponse.output.references

        assertNotNull(references)
        assertEquals(2, references.size)  
        assertEquals("Document Title 1", references[0].title)
    }

}
