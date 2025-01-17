import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.example.buffetec.network.ApiResponse
import com.example.buffetec.network.Output
import com.example.buffetec.network.Reference
import com.example.buffetec.network.RetrofitClient
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.BorderStroke
import com.example.buffetec.Components.ButtonComponent
import com.example.buffetec.ui.theme.lexendFontFamily
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Biblioteca(navHostController: NavHostController) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var references by remember { mutableStateOf<List<Reference>>(emptyList()) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Initialize TextToSpeech
    var tts: TextToSpeech? by remember { mutableStateOf(null) }
    DisposableEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status != TextToSpeech.ERROR) {
                tts?.language = Locale("es", "MX")  // Set to Spanish
            }
        }
        onDispose {
            tts?.shutdown()
        }
    }

    // State for pop-up visibility
    var showPopup by remember { mutableStateOf(false) }
    var currentSnippet by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Biblioteca Digital",
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontFamily = lexendFontFamily,
            style = TextStyle( fontWeight = FontWeight.ExtraLight, fontSize = 30.sp)
        )


        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Buscar") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar")
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(10.dp))
                .padding(vertical = 2.dp, horizontal = 5.dp),
            textStyle = TextStyle(
                fontSize = 14.sp,
                color = Color.Black
            ),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Black,
                containerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        ButtonComponent(
            label = "Buscar",
            onClick = {
                coroutineScope.launch {
                    val result = searchArticles(searchText.text, context)
                    references = result.output.references  // Assign the references from the response
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // LazyColumn to display references
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(references) { reference ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .clickable {
                            // When clicked, show pop-up with the snippet
                            currentSnippet = reference.snippet
                            showPopup = true
                        },
                    border = BorderStroke(1.dp, Color(0xFFD7DBDD)),
                    colors = CardDefaults.cardColors(containerColor = Color.White),

                    shape = RoundedCornerShape(10.dp)

                ) {
                    Column(
                        Modifier.padding(30.dp)
                    ) {
                        Text(
                            text = reference.title,
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = reference.url,
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                        )
                    }
                }
            }
        }

        // Pop-up window for snippet display
        if (showPopup) {
            AlertDialog(
                onDismissRequest = { showPopup = false },
                confirmButton = {
                    Button(
                        onClick = {
                            val snippetToRead = currentSnippet.take(200)  // Read the first 200 characters of the snippet
                            tts?.speak(snippetToRead, TextToSpeech.QUEUE_FLUSH, null, null)  // Read the snippet
                        }
                    ) {
                        Text("Leer Primeras Líneas")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            tts?.stop()  // Stop the TextToSpeech when the dialog is closed
                            showPopup = false
                        }
                    ) {
                        Text("Cerrar")
                    }
                },
                title = { Text("Información del Documento") },
                text = { Text(currentSnippet) }
            )
        }
    }
}

fun encodeQuery(query: String): String {
    return URLEncoder.encode(query, StandardCharsets.UTF_8.toString())
}

suspend fun searchArticles(query: String, context: Context): ApiResponse {
    return withContext(Dispatchers.IO) {
        try {
            val encodedQuery = encodeQuery(query)
            val response = RetrofitClient.apiService.searchArticles(encodedQuery).execute()

            if (response.isSuccessful) {
                return@withContext response.body() ?: ApiResponse("", "", "", Output(0, emptyList(), emptyMap(), 0, emptyList()))
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error al buscar artículos", Toast.LENGTH_LONG).show()
                }
                ApiResponse("", "", "", Output(0, emptyList(), emptyMap(), 0, emptyList()))
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error de conexión", Toast.LENGTH_LONG).show()
            }
            ApiResponse("", "", "", Output(0, emptyList(), emptyMap(), 0, emptyList()))
        }
    }
}
