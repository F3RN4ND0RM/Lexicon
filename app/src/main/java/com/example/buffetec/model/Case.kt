package com.example.lazycolumnexample.model

data class LegalCase(
    val caseNumber: String,
    val caseTitle: String,
    val status: String,
    val nextAction: String
)

fun getCases(): List<LegalCase> {
    return listOf(
        LegalCase("1234", "Divorcio", "En proceso", "Audiencia en 2 semanas"),
    LegalCase("5678", "Testamento", "Pendiente", "Falta documentación"),
    LegalCase("9101", "Contrato laboral", "Cerrado", "Caso finalizado"),
    LegalCase("1121", "Demanda civil", "En proceso", "Próxima reunión el 15 de octubre")
    )
}