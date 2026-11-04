package cz.utb.meteodenik

data class Mesto(val nazev: String, val teplota: Double)   // 1 data class

object Konfigurace {                                       // 2 singleton
    const val ZAKLADNI_URL = "https://api.open-meteo.com/v1/forecast"
}

fun Double.naStupneCelsia(): String = "%.1f °C".format(this)  // 3 extension

fun vypisVse(mesta: List<Mesto>, akce: (Mesto) -> Unit) {   // 4 lambda
    mesta.forEach { akce(it) }
}

suspend fun pockejAVrat(): Int {                           // 5 suspend
    kotlinx.coroutines.delay(100)
    return 42
}