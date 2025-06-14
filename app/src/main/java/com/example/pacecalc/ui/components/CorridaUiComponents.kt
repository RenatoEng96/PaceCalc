package com.example.pacecalc.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.pacecalc.R
import com.example.pacecalc.logic.Corrida

/**
 * Um campo de texto para entrada de valores numéricos.
 */
@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isDecimal: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            // Filtro aprimorado para entrada decimal
            if (isDecimal) {
                // Remove todos os caracteres que não sejam dígitos ou o primeiro ponto
                val originalText = value
                val newText = newValue.filter { it.isDigit() || it == '.' }
                // Permite a mudança apenas se o novo valor for vazio, numérico
                // ou um decimal válido (não contém mais de um ponto)
                if (newText.count { it == '.' } <= 1) {
                    onValueChange(newText)
                }
            } else {
                // Lógica original para números inteiros
                onValueChange(newValue.filter { it.isDigit() })
            }
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isDecimal) KeyboardType.Decimal else KeyboardType.Number
        ),
        singleLine = true,
        modifier = modifier
    )
}

/**
 * Um grupo de campos para entrada de tempo (minutos e segundos).
 */
@Composable
fun TimeInputGroup(
    minutes: String,
    onMinutesChange: (String) -> Unit,
    seconds: String,
    onSecondsChange: (String) -> Unit,
    label: String
) {
    Column {
        Text(label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            InputField(
                value = minutes,
                onValueChange = onMinutesChange,
                label = stringResource(R.string.minutos),
                modifier = Modifier.weight(1f)
            )
            Text(":", style = MaterialTheme.typography.headlineSmall)
            InputField(
                value = seconds,
                onValueChange = { if (it.length <= 2) onSecondsChange(it) }, // Limita a 2 dígitos
                label = stringResource(R.string.segundos),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * Um card para exibir os resultados da corrida de forma organizada.
 */
@Composable
fun ResultCard(corrida: Corrida) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.resumo_da_corrida),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Divider()
            Text("Distância: ${corrida.getDistanciaFormatada()}", style = MaterialTheme.typography.bodyLarge)
            Text("Tempo Total: ${corrida.getTempoTotalFormatado()}", style = MaterialTheme.typography.bodyLarge)
            Text("Pace Médio: ${corrida.getPaceFormatado()} /km", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

