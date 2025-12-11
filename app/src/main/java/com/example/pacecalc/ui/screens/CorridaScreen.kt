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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pacecalc.R
import com.example.pacecalc.ui.components.*
import com.example.pacecalc.ui.theme.PaceCalcTheme
import com.example.pacecalc.ui.viewmodel.CorridaViewModel

@Composable
fun CorridaScreen(
    // Injeção do ViewModel (cria um novo ou recupera o existente)
    viewModel: CorridaViewModel = viewModel()
) {
    // Observa o estado do ViewModel. Sempre que o estado mudar, a UI redesenha.
    val uiState by viewModel.uiState.collectAsState()

    val focusManager = LocalFocusManager.current

    // Efeito Lateral: Quando um resultado é calculado com sucesso, esconde o teclado
    LaunchedEffect(uiState.resultado) {
        if (uiState.resultado != null) {
            focusManager.clearFocus()
        }
    }

    // Função auxiliar para limpar e esconder o teclado
    fun limparTudo() {
        viewModel.limpar()
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

        // 1. Distância
        InputField(
            value = uiState.distancia,
            onValueChange = viewModel::onDistanciaChange,
            label = stringResource(R.string.distancia_km),
            isDecimal = true,
            modifier = Modifier.fillMaxWidth(),
            imeAction = ImeAction.Next
        )

        // 2. Tempo
        TimeInputGroup(
            minutes = uiState.tempoMin,
            onMinutesChange = viewModel::onTempoMinChange,
            seconds = uiState.tempoSeg,
            onSecondsChange = viewModel::onTempoSegChange,
            label = "Tempo Total",
            secondsImeAction = ImeAction.Next
        )

        // 3. Pace
        TimeInputGroup(
            minutes = uiState.paceMin,
            onMinutesChange = viewModel::onPaceMinChange,
            seconds = uiState.paceSeg,
            onSecondsChange = viewModel::onPaceSegChange,
            label = "Pace Médio (/km)",
            secondsImeAction = ImeAction.Next
        )

        // 4. Velocidade
        InputField(
            value = uiState.velocidade,
            onValueChange = viewModel::onVelocidadeChange,
            label = "Velocidade Média (km/h)",
            isDecimal = true,
            modifier = Modifier.fillMaxWidth(),
            imeAction = ImeAction.Done,
            keyboardActions = KeyboardActions(
                onDone = { viewModel.calcular() }
            )
        )

        // Botões de Ação
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { limparTudo() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Limpar")
            }

            Button(
                onClick = { viewModel.calcular() },
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.calcular), style = MaterialTheme.typography.titleMedium)
            }
        }

        // Seção de Resultados e Erros
        uiState.erro?.let {
            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyLarge)
        }
        uiState.resultado?.let {
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