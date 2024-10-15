import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.lazycolumnexample.model.getCases

@Composable
fun CaseDetail(caseId: String, navController: NavHostController) {
    val case = getCases().find { it.caseNumber == caseId }

    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Detalles del Caso #$case?.caseNumber",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.Black
            )
            Text(
                text = "Título: ${case?.caseTitle}",
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                color = Color.Black
            )
            Text(
                text = "Estado: ${case?.status}",
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                color = Color.Black
            )
            Text(
                text = "Próxima acción: ${case?.nextAction}",
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                color = Color.Black
            )
        }
    }
}