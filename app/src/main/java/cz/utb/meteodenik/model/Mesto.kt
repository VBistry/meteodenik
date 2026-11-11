package cz.utb.meteodenik.model

data class Mesto(
    val nazev: String,
    val teplota: Double
)

// Názvy měst jsou data, ne texty rozhraní – proto smějí být v kódu.
// V lekci 11 je nahradí odpověď z Open-Meteo API.
val UKAZKOVA_MESTA = listOf(
    Mesto("Zlín", 21.4),
    Mesto("Brno", 23.1),
    Mesto("Ostrava", 19.8)
)
