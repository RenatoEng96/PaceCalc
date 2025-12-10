package com.example.pacecalc.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.pacecalc.R
import com.example.pacecalc.logic.Corrida

/**
 * Um campo de texto para entrada de valores numéricos com suporte a ações de teclado (Next/Done).
 */
@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isDecimal: Boolean = false,
    imeAction: ImeAction = ImeAction.Next, // Padrão é ir para o próximo
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (isDecimal) {
                val newText = newValue.filter { it.isDigit() || it == '.' }
                if (newText.count { it == '.' } <= 1) {
                    onValueChange(newText)
                }
            } else {
                onValueChange(newValue.filter { it.isDigit() })
            }
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isDecimal) KeyboardType.Decimal else KeyboardType.Number,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions,
        singleLine = true,
        modifier = modifier
    )
}

/**
 * Um grupo de campos para entrada de tempo.
 * O campo de Minutos sempre avança (Next).
 * O campo de Segundos aceita configuração (pode ser Next ou Done).
 */
@Composable
fun TimeInputGroup(
    minutes: String,
    onMinutesChange: (String) -> Unit,
    seconds: String,
    onSecondsChange: (String) -> Unit,
    label: String,
    secondsImeAction: ImeAction = ImeAction.Next,
    secondsKeyboardActions: KeyboardActions = KeyboardActions.Default
) {
    Column {
        Text(label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Minutos: Sempre ImeAction.Next (vai para segundos automaticamente)
            InputField(
                value = minutes,
                onValueChange = onMinutesChange,
                label = stringResource(R.string.minutos),
                modifier = Modifier.weight(1f),
                imeAction = ImeAction.Next
            )
            Text(":", style = MaterialTheme.typography.headlineSmall)
            // Segundos: Configurável
            InputField(
                value = seconds,
                onValueChange = { if (it.length <= 2) onSecondsChange(it) },
                label = stringResource(R.string.segundos),
                modifier = Modifier.weight(1f),
                imeAction = secondsImeAction,
                keyboardActions = secondsKeyboardActions
            )
        }
    }
}

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
            Text("Velocidade: ${corrida.getVelocidadeFormatada()}", style = MaterialTheme.typography.bodyLarge)
        }
    }
}