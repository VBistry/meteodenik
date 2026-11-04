package cz.utb.meteodenik.cv04.prehled

class MestoJ {
    private var nazev: String? = null
    private var teplota: Double? = null

    fun getNazev(): String? {
        return nazev
    }

    fun setNazev(nazev: String?) {
        this.nazev = nazev
    }

    fun getTeplota(): Double? {
        return teplota
    }

    fun setTeplota(teplota: Double?) {
        this.teplota = teplota
    }

    fun popis(): String {
        if (nazev != null && teplota != null) {
            return nazev!! + ": " + teplota!! + " C"
        }
        return "neznámé"
    }
}

class Prehled {
    var nacita: Boolean = false
    var chyba: Boolean = false
    var prazdno: Boolean = false
    var mesta: MutableList<MestoJ> = mutableListOf()

    fun textStavu(): String {
        if (nacita) return "Načítám…"
        if (chyba) return "Chyba"
        if (prazdno) return "Nic tu není"
        return "Měst: " + mesta.size
    }

    fun nejteplejsi(): MestoJ? {
        var nej: MestoJ? = null
        for (i in 0 until mesta.size) {
            val m = mesta.get(i)
            if (m.getTeplota() != null) {
                if (nej == null) {
                    nej = m
                } else if (m.getTeplota()!! > nej.getTeplota()!!) {
                    nej = m
                }
            }
        }
        return nej
    }

    fun vypis() {
        for (i in 0 until mesta.size) {
            println(mesta.get(i).popis())
        }
        println(textStavu())
        val nej = nejteplejsi()
        if (nej != null) {
            println("Nejtepleji: " + nej.getNazev() + " (" + nej.getTeplota() + " C)")
        }
    }
}

fun main() {
    val p = Prehled()
    p.mesta.add(MestoJ().apply { setNazev("Zlín"); setTeplota(21.4) })
    p.mesta.add(MestoJ().apply { setNazev("Brno"); setTeplota(23.0) })
    p.mesta.add(MestoJ().apply { setNazev("Otrokovice"); setTeplota(20.9) })
    p.vypis()
}