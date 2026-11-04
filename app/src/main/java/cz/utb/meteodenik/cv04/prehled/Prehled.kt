package cz.utb.meteodenik.cv04.prehled

// Krok 1: místo MestoJ s gettery/settery a nullable poli
data class Mesto(val nazev: String, val teplota: Double)

// Krok 2: formátování jako extension funkce nad Double + šablony řetězců
fun Double.naTeplotu(): String = "$this C"

fun Mesto.popis(): String = "$nazev: ${teplota.naTeplotu()}"

// Krok 3: místo tří Booleanů jeden sealed typ se čtyřmi stavy
sealed interface Stav {
    data object Nacita : Stav
    data object Chyba : Stav
    data object Prazdno : Stav
    data class Hotovo(val pocet: Int) : Stav
}

fun Stav.text(): String = when (this) {
    Stav.Nacita -> "Načítám…"
    Stav.Chyba -> "Chyba"
    Stav.Prazdno -> "Nic tu není"
    is Stav.Hotovo -> "Měst: $pocet"
}

// Krok 6: seznam měst jde do konstruktoru, je jen pro čtení
class Prehled(private val mesta: List<Mesto>) {

    val stav: Stav = if (mesta.isEmpty()) Stav.Prazdno else Stav.Hotovo(mesta.size)

    // Krok 4: jedno volání kolekčního API, prázdný seznam vrátí null
    fun nejteplejsi(): Mesto? = mesta.maxByOrNull { it.teplota }

    // Krok 5: žádné !!, for (i in ...) ani .get(i)
    fun vypis() {
        mesta.forEach { println(it.popis()) }
        println(stav.text())
        nejteplejsi()?.let { println("Nejtepleji: ${it.nazev} (${it.teplota.naTeplotu()})") }
    }
}

fun main() {
    Prehled(
        listOf(
            Mesto("Zlín", 21.4),
            Mesto("Brno", 23.0),
            Mesto("Otrokovice", 20.9),
        )
    ).vypis()
}
