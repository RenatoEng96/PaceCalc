package com.example.pacecalc.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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

    var resultado by remember { mutableStateOf<Corrida?>(null) }
    var erro by remember { mutableStateOf<String?>(null) }

    // --- Lógica de Cálculo ---
    fun calcular() {
        // Limpa resultados anteriores
        resultado = null
        erro = null

        // Converte valores de String para numérico, tratando nulo como zero.
        val dist = distancia.toDoubleOrNull()
        val tMin = tempoMin.toIntOrNull()
        val tSeg = tempoSeg.toIntOrNull()
        val pMin = paceMin.toIntOrNull()
        val pSeg = paceSeg.toIntOrNull()

        // Valida se os campos foram preenchidos para permitir o cálculo
        val hasDist = dist != null
        val hasTempo = tMin != null || tSeg != null
        val hasPace = pMin != null || pSeg != null

        try {
            // **MUDANÇA AQUI: Usando as funções de fábrica em vez dos construtores.**
            resultado = when {
                hasDist && hasTempo -> Corrida.porDistanciaETempo(dist!!, tMin ?: 0, tSeg ?: 0)
                hasDist && hasPace -> Corrida.porDistanciaEPace(dist!!, pMin ?: 0, pSeg ?: 0)
                hasTempo && hasPace -> Corrida.porTempoEPace(tMin ?: 0, tSeg ?: 0, pMin ?: 0, pSeg ?: 0)
                else -> {
                    erro = "Preencha pelo menos dois dos três campos."
                    null
                }
            }
        } catch (e: Exception) {
            erro = e.message ?: "Ocorreu um erro desconhecido."
        }
    }

    // --- UI Layout ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // Permite rolagem em telas menores
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(stringResource(id = R.string.app_name), style = MaterialTheme.typography.headlineMedium)

        // Seção de Entradas
        InputField(
            value = distancia,
            onValueChange = { distancia = it },
            label = stringResource(R.string.distancia_km),
            isDecimal = true,
            modifier = Modifier.fillMaxWidth()
        )
        TimeInputGroup(
            minutes = tempoMin,
            onMinutesChange = { tempoMin = it },
            seconds = tempoSeg,
            onSecondsChange = { tempoSeg = it },
            label = "Tempo Total"
        )
        TimeInputGroup(
            minutes = paceMin,
            onMinutesChange = { paceMin = it },
            seconds = paceSeg,
            onSecondsChange = { paceSeg = it },
            label = "Pace Médio (/km)"
        )

        // Botão de Ação
        Button(
            onClick = { calcular() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.calcular), style = MaterialTheme.typography.titleMedium)
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