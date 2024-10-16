import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top: Back Button
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.Start)
                    .background(Color.LightGray, shape = CircleShape)
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.DarkGray
                )
            }
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Número de Caso #${case?.caseNumber}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "Descripción: ${case?.caseTitle}",
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp,
                        color = Color.DarkGray
                    )
                    Text(
                        text = "Estado: ${case?.status}",
                        fontWeight = FontWeight.Light,
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Próxima Acción: ${case?.nextAction}",
                        fontWeight = FontWeight.Light,
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}