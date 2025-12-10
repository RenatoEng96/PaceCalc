package com.example.pacecalc.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pacecalc.R
import com.example.pacecalc.logic.Corrida
import com.example.pacecalc.ui.components.*
import com.example.pacecalc.ui.theme.PaceCalcTheme

@Composable
fun CorridaScreen() {
    // --- Gerenciamento de Estado ---
    var distancia by remember { mutableStateOf("") }

    var tempoMin by remember { mutableStateOf("") }
    var tempoSeg by remember { mutableStateOf("") }

    var paceMin by remember { mutableStateOf("") }
    var paceSeg by remember { mutableStateOf("") }

    var velocidade by remember { mutableStateOf("") }

    var resultado by remember { mutableStateOf<Corrida?>(null) }
    var erro by remember { mutableStateOf<String?>(null) }

    // Gerenciador de Foco para esconder teclado
    val focusManager = LocalFocusManager.current

    // --- Lógica de Cálculo ---
    fun calcular() {
        resultado = null
        erro = null

        try {
            val dist = distancia.toDoubleOrNull()
            val tMin = tempoMin.toIntOrNull()
            val tSeg = tempoSeg.toIntOrNull()
            val pMin = paceMin.toIntOrNull()
            val pSeg = paceSeg.toIntOrNull()
            val vel = velocidade.toDoubleOrNull()

            resultado = Corrida.calcular(
                distanciaKm = dist,
                tempoMinutos = tMin,
                tempoSegundos = tSeg,
                paceMinutos = pMin,
                paceSegundos = pSeg,
                velocidadeKmH = vel
            )
            // Fecha o teclado se der sucesso
            focusManager.clearFocus()

        } catch (e: IllegalArgumentException) {
            erro = e.message
        } catch (e: Exception) {
            erro = "Ocorreu um erro inesperado."
        }
    }

    // --- Nova Função: Limpar Campos ---
    fun limpar() {
        distancia = ""
        tempoMin = ""
        tempoSeg = ""
        paceMin = ""
        paceSeg = ""
        velocidade = ""
        resultado = null
        erro = null
        focusManager.clearFocus()
    }

    // --- UI Layout ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(stringResource(id = R.string.app_name), style = MaterialTheme.typography.headlineMedium)

        // Seção de Entradas

        // 1. Distância
        InputField(
            value = distancia,
            onValueChange = { distancia = it },
            label = stringResource(R.string.distancia_km),
            isDecimal = true,
            modifier = Modifier.fillMaxWidth(),
            imeAction = ImeAction.Next
        )

        // 2. Tempo
        TimeInputGroup(
            minutes = tempoMin,
            onMinutesChange = { tempoMin = it },
            seconds = tempoSeg,
            onSecondsChange = { tempoSeg = it },
            label = "Tempo Total",
            secondsImeAction = ImeAction.Next
        )

        // 3. Pace
        TimeInputGroup(
            minutes = paceMin,
            onMinutesChange = { paceMin = it },
            seconds = paceSeg,
            onSecondsChange = { paceSeg = it },
            label = "Pace Médio (/km)",
            secondsImeAction = ImeAction.Next
        )

        // 4. Velocidade
        InputField(
            value = velocidade,
            onValueChange = { velocidade = it },
            label = "Velocidade Média (km/h)",
            isDecimal = true,
            modifier = Modifier.fillMaxWidth(),
            imeAction = ImeAction.Done,
            keyboardActions = KeyboardActions(
                onDone = { calcular() }
            )
        )

        // Botões de Ação (Alterado para incluir Limpar)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Botão Secundário: Limpar
            OutlinedButton(
                onClick = { limpar() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Limpar")
            }

            // Botão Primário: Calcular
            Button(
                onClick = { calcular() },
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.calcular), style = MaterialTheme.typography.titleMedium)
            }
        }

        // Seção de Resultados
        erro?.let {
            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyLarge)
        }
        resultado?.let {
            ResultCard(corrida = it)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CorridaScreenPreview() {
    PaceCalcTheme {
        CorridaScreen()
    }
}