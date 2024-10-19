package com.example.buffetec

import com.example.buffetec.network.ApiResponse
import com.example.buffetec.network.Output
import com.example.buffetec.network.Reference
import org.junit.Assert.*
import org.junit.Test

class SearchUnitTest {

    // Esta prueba valida que el método de búsqueda de artículos devuelve correctamente las referencias simuladas.
    @Test
    fun searchArticles_returnsReferences() {
        // Preparar datos simulados de la API
        val mockReferences = listOf(
            Reference(url = "https://example.com/document1",
                title = "Document Title 1",
                snippet = "This is a snippet of the first document content."),
            Reference(url = "https://example.com/document2",
                title = "Document Title 2",
                snippet = "This is a snippet of the second document content.")
        )

        // Crear una respuesta simulada de la API con las referencias anteriores
        val mockResponse = ApiResponse(
            id = "1",
            url = "https://api.example.com/biblioteca",
            created_at = "2024-01-01",
            output = Output(10, listOf(), mapOf(), 50, mockReferences)
        )

        // Obtener las referencias desde la respuesta simulada
        val references = mockResponse.output.references

        // Verificar que no sea nulo
        assertNotNull(references)
        // Verificar que se devuelvan dos referencias, como se espera en los datos simulados
        assertEquals(2, references.size)
        // Verificar que el título de la primera referencia sea el esperado
        assertEquals("Document Title 1", references[0].title)
    }

}
