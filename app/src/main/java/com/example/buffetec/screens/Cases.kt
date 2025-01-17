import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

data class LegalCase(
    val caseNumber: String,
    val caseTitle: String,
    val status: String,
    val nextAction: String
)

@Composable
fun Cases(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val cases = listOf(
        LegalCase("1234", "Divorcio", "En proceso", "Audiencia en 2 semanas"),
        LegalCase("5678", "Testamento", "Pendiente", "Falta documentación"),
        LegalCase("9101", "Contrato laboral", "Cerrado", "Caso finalizado"),
        LegalCase("1121", "Demanda civil", "En proceso", "Próxima reunión el 15 de octubre")
    )

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Casos Legales",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cases) { case ->
                    CaseCard(
                        legalCase = case,
                        onClick = {
                            navController.navigate("case_detail/${case.caseNumber}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CaseCard(legalCase: LegalCase, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, shape = RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().background(Color.White),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Caso #${legalCase.caseNumber}",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.Black
            )
            Text(
                text = legalCase.caseTitle,
                fontSize = 18.sp,
                color = Color.Black
            )
            Text(
                text = "Estado: ${legalCase.status}",
                fontSize = 16.sp,
                color = Color.Gray
            )
            Text(
                text = "Próxima acción: ${legalCase.nextAction}",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
    }
}