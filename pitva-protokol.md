# Lekce 04: Dekompilační protokol (cvičení 4.1)

Zdroj: `app/src/main/java/cz/utb/meteodenik/cv04/pitva/Pitva.kt`
Postup: Build › Assemble Project → Tools › Kotlin › Show Kotlin Bytecode › Decompile

## Tabulka konstrukcí

| # | Co jsem napsal (Kotlin) | Vygenerovaný člen (přesný podpis z dekompilátu) | Kolik řádků v Javě ručně |
|---|---|---|---|
| 1 | `data class Zapis(val mesto: String, val teplota: Double, val poznamka: String = "")` (5 řádků) | `public final Zapis copy(@NotNull String mesto, double teplota, @NotNull String poznamka)` a `public static Zapis copy$default(Zapis var0, String var1, double var2, String var4, int var5, Object var6)`; dále `component1()`–`component3()`, `toString()`, `hashCode()`, `equals(@Nullable Object other)` | cca 70 |
| 2 | `object Prevody { const val ABSOLUTNI_NULA = -273.15; fun naFahrenheita(...) }` | `public static final Prevody INSTANCE = new Prevody();` + `private Prevody()` + `public static final double ABSOLUTNI_NULA = -273.15;` | cca 10 |
| 3 | `fun Double.zaokrouhlenoNaDesetiny(): Double` | `public static final double zaokrouhlenoNaDesetiny(double $this$zaokrouhlenoNaDesetiny)` (ve třídě `PitvaKt`) | cca 5 |
| 4 | `fun prumer(zapisy: List<Zapis>, vyber: (Zapis) -> Double): Double` | `public static final double prumer(@NotNull List zapisy, @NotNull Function1 vyber)` | cca 15 |
| 5 | `suspend fun nactiPoZdrzeni(zapisy: List<Zapis>): List<Zapis>` | `@Nullable public static final Object nactiPoZdrzeni(@NotNull List zapisy, @NotNull Continuation $completion)` | cca 40 (callbacky / stavový automat) |

## Odpovědi

**1. Kolik metod má dekompilovaná třída `Zapis` a které nemají ve zdroji žádný náznak?**
Třída `Zapis` má 11 metod: `getMesto`, `getTeplota`, `getPoznamka`, `component1`, `component2`, `component3`, `copy`, `copy$default`, `toString`, `hashCode` a `equals`. K tomu má 2 konstruktory. Gettery jsou naznačené klíčovým slovem `val`. Ostatních osm metod (`component1–3`, `copy`, `copy$default`, `toString`, `hashCode`, `equals`) ve zdroji vůbec nevidím. Vygeneroval je kompilátor jen kvůli slovu `data`.

**2. Proč má `Zapis` konstruktor navíc s `int` a `DefaultConstructorMarker`?**
Souvisí to s výchozí hodnotou `poznamka: String = ""`. Java výchozí argumenty nezná, a proto kompilátor vytvoří syntetický konstruktor. Ten v `int` (bitové masce) dostane informaci, které argumenty volající nevyplnil. Když je nastaven bit `4` (třetí parametr), dosadí se `""` a pak se zavolá normální konstruktor. `DefaultConstructorMarker` je jen značka, aby se tento konstruktor nepletl s běžným.

**3. Jaký je první parametr `zaokrouhlenoNaDesetiny` a je funkce polymorfní?**
Prvním parametrem je `double $this$zaokrouhlenoNaDesetiny`, tedy hodnota, na které funkci volám (příjemce). Extension funkce je ve skutečnosti obyčejná statická metoda ve třídě `PitvaKt`, třídu `Double` nijak nemění. Proto není polymorfní: rozhoduje se staticky podle deklarovaného typu a v potomkovi ji nelze přepsat (override).

**4. Jaký typ má druhý parametr `prumer` a co to říká o funkci jako hodnotě?**
Druhý parametr má typ `Function1`, což je rozhraní z knihovny Kotlinu s jedinou metodou `invoke`. Lambda `(Zapis) -> Double` se na JVM předává jako objekt, který toto rozhraní implementuje. V těle je to vidět na volání `vyber.invoke(var6)`. Funkce jako hodnota je tedy na JVM obyčejný objekt a číslo v názvu (`Function1`) udává počet parametrů.

**5. Co se stalo s návratovým typem `nactiPoZdrzeni` a proč je v dekompilátu `switch`?**
Místo `List<Zapis>` vrací funkce `Object` a má navíc parametr `Continuation`. Vrací totiž buď skutečný výsledek, nebo speciální značku `COROUTINE_SUSPENDED`, když se funkce uspala (u `delay`). `switch ($continuation.label)` je stavový automat: `label` si pamatuje, kde funkce skončila, a po probuzení se pokračuje správnou větví (`case 1`). Lokální proměnné se mezi tím ukládají do `Continuation` (`L$0`).