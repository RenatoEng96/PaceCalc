package com.example.pacecalc.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pacecalc.logic.Corrida
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Representa o estado da UI num único objeto.
 */
data class CorridaUiState(
    val distancia: String = "",
    val tempoMin: String = "",
    val tempoSeg: String = "",
    val paceMin: String = "",
    val paceSeg: String = "",
    val velocidade: String = "",
    val resultado: Corrida? = null,
    val erro: String? = null
)

class CorridaViewModel : ViewModel() {

    // O estado é privado para escrita (_uiState) e público apenas para leitura (uiState)
    private val _uiState = MutableStateFlow(CorridaUiState())
    val uiState: StateFlow<CorridaUiState> = _uiState.asStateFlow()

    // --- Funções de Atualização de Campos ---
    fun onDistanciaChange(novoValor: String) {
        _uiState.update { it.copy(distancia = novoValor) }
    }

    fun onTempoMinChange(novoValor: String) {
        _uiState.update { it.copy(tempoMin = novoValor) }
    }

    fun onTempoSegChange(novoValor: String) {
        _uiState.update { it.copy(tempoSeg = novoValor) }
    }

    fun onPaceMinChange(novoValor: String) {
        _uiState.update { it.copy(paceMin = novoValor) }
    }

    fun onPaceSegChange(novoValor: String) {
        _uiState.update { it.copy(paceSeg = novoValor) }
    }

    fun onVelocidadeChange(novoValor: String) {
        _uiState.update { it.copy(velocidade = novoValor) }
    }

    // --- Lógica de Negócio ---

    fun calcular() {
        // Reseta erros e resultados anteriores antes de calcular
        _uiState.update { it.copy(resultado = null, erro = null) }

        val currentState = _uiState.value

        try {
            val dist = currentState.distancia.toDoubleOrNull()
            val tMin = currentState.tempoMin.toIntOrNull()
            val tSeg = currentState.tempoSeg.toIntOrNull()
            val pMin = currentState.paceMin.toIntOrNull()
            val pSeg = currentState.paceSeg.toIntOrNull()
            val vel = currentState.velocidade.toDoubleOrNull()

            val resultadoCalculado = Corrida.calcular(
                distanciaKm = dist,
                tempoMinutos = tMin,
                tempoSegundos = tSeg,
                paceMinutos = pMin,
                paceSegundos = pSeg,
                velocidadeKmH = vel
            )

            // Sucesso: Atualiza o estado com o resultado
            _uiState.update { it.copy(resultado = resultadoCalculado) }

        } catch (e: IllegalArgumentException) {
            _uiState.update { it.copy(erro = e.message) }
        } catch (e: Exception) {
            _uiState.update { it.copy(erro = "Ocorreu um erro inesperado.") }
        }
    }

    fun limpar() {
        // Reseta o estado para o inicial (vazio)
        _uiState.value = CorridaUiState()
    }
}