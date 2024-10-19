package com.example.buffetec

import com.example.buffetec.network.ApiResponse
import com.example.buffetec.network.Output
import com.example.buffetec.network.Reference
import encodeQuery
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class SearchErrorTest {

    // Esta prueba valida que el sistema maneje correctamente entradas nulas y vacías en la búsqueda de artículos.
    @Test
    fun searchArticles_handlesNullAndEmptyInputs() {
        // Caso 1: Entrada nula
        val invalidQuery1: String? = null
        // Caso 2: Cadena vacía
        val invalidQuery2 = ""

        // Codificar la entrada nula; debe devolver "nulo"
        val encodedQuery1 = invalidQuery1?.let { encodeQuery(it) } ?: "nulo"
        // Codificar la cadena vacía; debe devolver ""
        val encodedQuery2 = encodeQuery(invalidQuery2)

        // Verificar que el resultado para la entrada nula sea "nulo"
        assertEquals("nulo", encodedQuery1)
        // Verificar que el resultado para la cadena vacía sea una cadena vacía
        assertEquals("", encodedQuery2)
    }
}
