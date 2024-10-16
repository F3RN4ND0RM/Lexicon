package com.example.buffetec.Components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.textFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buffetec.ui.theme.lexendFontFamily

@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    lexendFontFam: FontFamily,
    visualTrans: Boolean
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label, fontFamily = lexendFontFamily,fontWeight = FontWeight.ExtraLight,) },
        visualTransformation = if (!visualTrans) VisualTransformation.None else PasswordVisualTransformation(),

        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(0.dp, Color.White)), // Border settings
        textStyle = TextStyle(
            fontFamily = lexendFontFam, // Set the custom font family
            fontSize = 16.sp // Adjust font size as needed
        ),

    )
}

