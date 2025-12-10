package com.example.pacecalc.logic

import kotlin.math.floor
import kotlin.math.roundToInt

/**
 * Representa uma corrida.
 * O sistema agora trabalha com 4 grandezas possíveis (Distância, Tempo, Pace, Velocidade),
 * mas matematicamente Pace e Velocidade representam a mesma dimensão (Intensidade).
 *
 * A classe calcula os valores faltantes desde que sejam fornecidos dados de duas dimensões distintas:
 * 1. Distância
 * 2. Tempo
 * 3. Intensidade (Pace OU Velocidade)
 */
class Corrida private constructor(
    val distanciaFinal: Double,
    val tempoFinal: Double,
    val paceFinal: Double,
    val velocidadeFinal: Double
) {

    companion object {
        /**
         * Tenta criar uma instância de Corrida baseada em parâmetros opcionais.
         * É necessário fornecer dados para pelo menos duas das três dimensões (Distância, Tempo, Intensidade).
         */
        fun calcular(
            distanciaKm: Double?,
            tempoMinutos: Int?,
            tempoSegundos: Int?,
            paceMinutos: Int?,
            paceSegundos: Int?,
            velocidadeKmH: Double?
        ): Corrida {
            // 1. Normalizar Entradas
            val d = if (distanciaKm != null && distanciaKm > 0) distanciaKm else null

            val t: Double? = if ((tempoMinutos != null || tempoSegundos != null)) {
                val total = (tempoMinutos ?: 0) + (tempoSegundos ?: 0) / 60.0
                if (total > 0) total else null
            } else null

            // Determina a "Intensidade" (Pace) baseada no Pace OU na Velocidade fornecida
            var p: Double? = if ((paceMinutos != null || paceSegundos != null)) {
                val total = (paceMinutos ?: 0) + (paceSegundos ?: 0) / 60.0
                if (total > 0) total else null
            } else null

            // Se não tem Pace, mas tem Velocidade, converte Velocidade para Pace
            // Fórmula: Pace (min/km) = 60 / Velocidade (km/h)
            if (p == null && velocidadeKmH != null && velocidadeKmH > 0) {
                p = 60.0 / velocidadeKmH
            }

            // 2. Calcular o que falta (Baseado em Distância, Tempo e Pace)
            val dFinal: Double
            val tFinal: Double
            val pFinal: Double

            when {
                // Caso 1: Distância e Tempo -> Calcula Pace (e Velocidade)
                d != null && t != null -> {
                    dFinal = d
                    tFinal = t
                    pFinal = t / d
                }
                // Caso 2: Distância e Pace (ou Velocidade convertida) -> Calcula Tempo
                d != null && p != null -> {
                    dFinal = d
                    pFinal = p
                    tFinal = p * d
                }
                // Caso 3: Tempo e Pace (ou Velocidade convertida) -> Calcula Distância
                t != null && p != null -> {
                    tFinal = t
                    pFinal = p
                    dFinal = t / p
                }
                else -> {
                    throw IllegalArgumentException("Forneça valores para pelo menos dois campos distintos (ex: Distância e Tempo, ou Tempo e Velocidade).")
                }
            }

            // 3. Calcula a Velocidade Final baseada no Pace final calculado
            val vFinal = 60.0 / pFinal

            return Corrida(dFinal, tFinal, pFinal, vFinal)
        }
    }

    // --- Funções de Formatação ---

    fun getPaceFormatado(): String = formatarDecimalParaTempo(paceFinal)

    fun getTempoTotalFormatado(): String = formatarDecimalParaTempo(tempoFinal)

    fun getDistanciaFormatada(): String = "%.2f km".format(distanciaFinal)

    fun getVelocidadeFormatada(): String = "%.2f km/h".format(velocidadeFinal)

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