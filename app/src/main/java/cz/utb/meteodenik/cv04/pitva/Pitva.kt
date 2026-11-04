package cz.utb.meteodenik.cv04.pitva

import kotlin.time.Duration.Companion.milliseconds

data class Zapis(
    val mesto: String,
    val teplota: Double,
    val poznamka: String = "",
)

object Prevody {
    const val ABSOLUTNI_NULA = -273.15
    fun naFahrenheita(celsia: Double): Double = celsia * 9 / 5 + 32
}

fun Double.zaokrouhlenoNaDesetiny(): Double = Math.round(this * 10) / 10.0

fun prumer(zapisy: List<Zapis>, vyber: (Zapis) -> Double): Double =
    if (zapisy.isEmpty()) 0.0 else zapisy.sumOf(vyber) / zapisy.size

suspend fun nactiPoZdrzeni(zapisy: List<Zapis>): List<Zapis> {
    kotlinx.coroutines.delay(50.milliseconds)
    return zapisy
}