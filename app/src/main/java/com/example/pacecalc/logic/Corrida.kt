package com.example.pacecalc.logic

import kotlin.math.floor
import kotlin.math.roundToInt

/**
 * Representa uma corrida, calculando um dos três valores (distância, tempo ou pace)
 * com base nos outros dois.
 *
 * O construtor é privado. A criação de instâncias DEVE ser feita através das
 * funções de fábrica no 'companion object' para evitar ambiguidade.
 */
class Corrida private constructor(
    distanciaKm: Double?,
    tempoTotalMinutos: Double?,
    paceMinutosPorKm: Double?
) {
    val distanciaFinal: Double
    val tempoFinal: Double
    val paceFinal: Double

    init {
        when {
            // Caso 1: Calcula o Pace
            distanciaKm != null && tempoTotalMinutos != null -> {
                if (distanciaKm <= 0) throw IllegalArgumentException("A distância deve ser um valor positivo.")
                distanciaFinal = distanciaKm
                tempoFinal = tempoTotalMinutos
                paceFinal = tempoTotalMinutos / distanciaKm
            }
            // Caso 2: Calcula o Tempo
            distanciaKm != null && paceMinutosPorKm != null -> {
                if (distanciaKm <= 0) throw IllegalArgumentException("A distância deve ser um valor positivo.")
                distanciaFinal = distanciaKm
                paceFinal = paceMinutosPorKm
                tempoFinal = paceMinutosPorKm * distanciaKm
            }
            // Caso 3: Calcula a Distância
            tempoTotalMinutos != null && paceMinutosPorKm != null -> {
                if (paceMinutosPorKm <= 0) throw IllegalArgumentException("O pace deve ser um valor positivo.")
                tempoFinal = tempoTotalMinutos
                paceFinal = paceMinutosPorKm
                distanciaFinal = tempoTotalMinutos / paceMinutosPorKm
            }
            else -> throw IllegalArgumentException("Forneça exatamente dois dos três valores (distância, tempo ou pace).")
        }
    }

    /**
     * Objeto companheiro que contém as funções de fábrica (factory functions).
     * Esta é a maneira correta de criar objetos 'Corrida'.
     */
    companion object {
        /**
         * Cria uma instância de Corrida para calcular o PACE a partir de distância e tempo.
         */
        fun porDistanciaETempo(distanciaKm: Double, tempoMinutos: Int, tempoSegundos: Int): Corrida {
            return Corrida(
                distanciaKm = distanciaKm,
                tempoTotalMinutos = tempoMinutos + tempoSegundos / 60.0,
                paceMinutosPorKm = null
            )
        }

        /**
         * Cria uma instância de Corrida para calcular o TEMPO a partir de distância e pace.
         */
        fun porDistanciaEPace(distanciaKm: Double, paceMinutos: Int, paceSegundos: Int): Corrida {
            return Corrida(
                distanciaKm = distanciaKm,
                tempoTotalMinutos = null,
                paceMinutosPorKm = paceMinutos + paceSegundos / 60.0
            )
        }

        /**
         * Cria uma instância de Corrida para calcular a DISTÂNCIA a partir de tempo e pace.
         */
        fun porTempoEPace(tempoMinutos: Int, tempoSegundos: Int, paceMinutos: Int, paceSegundos: Int): Corrida {
            return Corrida(
                distanciaKm = null,
                tempoTotalMinutos = tempoMinutos + tempoSegundos / 60.0,
                paceMinutosPorKm = paceMinutos + paceSegundos / 60.0
            )
        }
    }

    // --- Funções de Formatação ---

    fun getPaceFormatado(): String = formatarDecimalParaTempo(paceFinal)
    fun getTempoTotalFormatado(): String = formatarDecimalParaTempo(tempoFinal)
    fun getDistanciaFormatada(): String = "%.2f km".format(distanciaFinal)

    private fun formatarDecimalParaTempo(valorDecimal: Double): String {
        val minutos = floor(valorDecimal).toInt()
        val segundos = ((valorDecimal - minutos) * 60).roundToInt()
        return if (segundos == 60) {
            "%02d:00".format(minutos + 1)
        } else {
            "%02d:%02d".format(minutos, segundos)
        }
    }
}