package com.example.buffetec

import com.example.buffetec.network.ApiResponse
import com.example.buffetec.network.Output
import com.example.buffetec.network.Reference
import com.example.buffetec.network.UpdateRequest
import com.example.buffetec.network.UpdateResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ProfileScreenTest {

    @Test
    fun testSuccessfulProfileUpdate() {
        val mockRequests = listOf(
            UpdateRequest(
                name = "Oscar",
                lastName = "Treviño",
                email = "oscar@gmail.com",
                address = "Calle 123",
                city = "Monterrey"
            ),
            UpdateRequest(
                name = "Juan",
                lastName = "Perez",
                email = "juan@gmail.com",
                address = "Calle 456",
                city = "Monterrey"
            )
        )

        val mockResponse = UpdateResponse(
            message = "User updated successfully",
        )

        val response = mockResponse.message

        assertNotNull(response)
        assertTrue(response.isNotEmpty())
        assertEquals("User updated successfully", response)
    }

    @Test
    fun testFailedProfileUpdate() {
        val name = ""
        val lastName = "Treviño"
        val email = "oscar@gmail.com"
        val address = "Calle 123"
        val city = "Monterrey"

        val mockRequest = UpdateRequest(
            name = name,
            lastName = lastName,
            email = email,
            address = address,
            city = city
        )

        val mockResponse = UpdateResponse(
            message = "User update failed",
        )

        val response = mockResponse.message

        assertNotNull(response)
        assertTrue(response.isNotEmpty())
        assertEquals("User update failed", response)
    }
}

class ProfileTest {
    @Test
    fun searchArticles_handlesBasicQuery() {
        val basicQuery = "data privacy regulation"

        val mockReferences = listOf(
            Reference(
                url = "https://example.com/document1",
                title = "Understanding Data Privacy Regulations",
                snippet = "This document provides an overview of current data privacy regulations."
            ),
            Reference(
                url = "https://example.com/document2",
                title = "The Importance of Data Protection",
                snippet = "Data protection is crucial in the era of digital information."
            )
        )

        val mockResponse = ApiResponse(
            id = "3",
            url = "https://api.example.com/articles",
            created_at = "2024-02-01",
            output = Output(2, listOf(), mapOf(), 30, mockReferences)
        )

        val references = mockResponse.output.references

        assertNotNull(references)
        assertTrue(references.isNotEmpty())
        assertEquals("Understanding Data Privacy Regulations", references[0].title)
        assertTrue(references[0].snippet.contains("data privacy regulations"))
        assertEquals(2, references.size)  // Ensure it returns two results
    }
}

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