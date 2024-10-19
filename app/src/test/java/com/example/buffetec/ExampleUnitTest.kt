package com.example.buffetec

import com.example.buffetec.network.ApiResponse
import com.example.buffetec.network.Output
import com.example.buffetec.network.Reference
import org.junit.Assert.*
import org.junit.Test

class SearchUnitTest {

    @Test
    fun searchArticles_returnsReferences() {
        val referenciasMock = listOf(
            Reference(
                url = "https://example.com/document1",
                title = "Título del Documento 1",
                snippet = "Este es un fragmento del contenido del primer documento."
            ),
            Reference(
                url = "https://example.com/document2",
                title = "Título del Documento 2",
                snippet = "Este es un fragmento del contenido del segundo documento."
            )
        )

        val respuestaMock = ApiResponse(
            id = "1",
            url = "https://api.example.com/biblioteca",
            created_at = "2024-01-01",
            output = Output(10, listOf(), mapOf(), 50, referenciasMock)
        )

        val referencias = respuestaMock.output.references

        assertNotNull(referencias)
        assertEquals(2, referencias.size)
        assertEquals("Título del Documento 1", referencias[0].title)
    }
}
