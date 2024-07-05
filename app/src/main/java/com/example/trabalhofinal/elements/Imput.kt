package com.example.trabalhofinal.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StylishInputField(value: TextFieldValue, onValueChange: (TextFieldValue) -> Unit, label: String, placeholder: String) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(corner = CornerSize(16.dp)),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                focusedLabelColor = MaterialTheme.colorScheme.secondary,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@OptIn( ExperimentalMaterial3Api::class)
@Composable
fun DecimalInputField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    placeholder: String
) {
    val decimalPattern = "^(\\d+)?(\\.\\d{0,2})?\$".toRegex()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                if (it.text.matches(decimalPattern)) {
                    onValueChange(it)
                } else {
                    val filteredText = it.text.filter { char -> char.isDigit() || char == '.' }
                    val dotIndex = filteredText.indexOf('.')
                    val newText = if (dotIndex >= 0 && filteredText.length - dotIndex > 3) {
                        filteredText.substring(0, dotIndex + 3)
                    } else {
                        filteredText
                    }
                    onValueChange(TextFieldValue(newText, TextRange(newText.length)))
                }
            },
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(corner = CornerSize(16.dp)),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                focusedLabelColor = MaterialTheme.colorScheme.secondary,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            )
        )
    }
}


fun formatCpf(cpf: String): String {
    val digitsOnly = cpf.filter { it.isDigit() }
    return when (digitsOnly.length) {
        in 1..3 -> digitsOnly
        in 4..6 -> digitsOnly.replaceFirst(Regex("(\\d{3})(\\d+)"), "$1.$2")
        in 7..9 -> digitsOnly.replaceFirst(Regex("(\\d{3})(\\d{3})(\\d+)"), "$1.$2.$3")
        in 10..11 -> digitsOnly.replaceFirst(Regex("(\\d{3})(\\d{3})(\\d{3})(\\d+)"), "$1.$2.$3-$4")
        else -> digitsOnly.take(11).replaceFirst(Regex("(\\d{3})(\\d{3})(\\d{3})(\\d{2})"), "$1.$2.$3-$4")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CpfInputField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    placeholder: String
) {
    val maxLength = 14

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                val formattedText = formatCpf(it.text)
                if (formattedText.length <= maxLength) {
                    onValueChange(TextFieldValue(formattedText, TextRange(formattedText.length)))
                }
            },
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(corner = CornerSize(16.dp)),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                focusedLabelColor = MaterialTheme.colorScheme.secondary,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )
    }
}

fun formatPhoneNumber(phone: String): String {
    val digitsOnly = phone.filter { it.isDigit() }
    return when (digitsOnly.length) {
        in 1..2 -> "($digitsOnly"
        in 3..6 -> digitsOnly.replaceFirst(Regex("(\\d{2})(\\d+)"), "($1) $2")
        in 7..10 -> digitsOnly.replaceFirst(Regex("(\\d{2})(\\d{4})(\\d+)"), "($1) $2-$3")
        in 11..12 -> digitsOnly.replaceFirst(Regex("(\\d{2})(\\d{5})(\\d+)"), "($1) $2-$3")
        else -> digitsOnly.take(11).replaceFirst(Regex("(\\d{2})(\\d{5})(\\d{4})"), "($1) $2-$3")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneInputField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    placeholder: String
) {
    val maxLength = 15

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                val formattedText = formatPhoneNumber(it.text)
                if (formattedText.length <= maxLength) {
                    onValueChange(TextFieldValue(formattedText, TextRange(formattedText.length)))
                }
            },
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(corner = CornerSize(16.dp)),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                focusedLabelColor = MaterialTheme.colorScheme.secondary,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )
    }
}

