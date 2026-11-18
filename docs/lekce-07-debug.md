# Lekce 07 – debug: čtyři chyby v jedné obrazovce (cvičení 7.6)

Opravený kód je v `app/src/main/java/cz/utb/meteodenik/ui/RozbityMestaSeznam.kt`.
Všechny čtyři příznaky jsem nejdřív zopakoval na emulátoru (Pixel 8, Android 17) s původním kódem, pak ověřil opravu.

| Chyba | Vinný řádek | Proč se to děje | Oprava |
|---|---|---|---|
| **A** – do pole nejde psát, Přidat nic nepřidá | `var novyNazev = ""` | Obyčejná lokální proměnná. Compose ji nesleduje, takže zápis v `onValueChange` nevyvolá rekompozici, a i kdyby, při rekompozici se proměnná vytvoří znovu jako `""`. Pole ukazuje jen `value`, a ta je pořád prázdná. Tlačítko Přidat čte tutéž proměnnou, vidí `""` a podmínka `isNotBlank()` neprojde. | `var novyNazev by rememberSaveable { mutableStateOf("") }` – sledovaný stav, který přežije rekompozici i otočení. |
| **B** – ploška „Počet měst“ těsně obepíná text, okolo je 8 dp nevybarvené mezery | `.padding(8.dp).background(...)` | Modifikátory se aplikují zleva doprava. `padding` před `background` udělá vnější mezeru a pozadí se kreslí až uvnitř, přesně kolem textu. | `.padding(vertical = 8.dp).clip(RoundedCornerShape(8.dp)).background(...).padding(horizontal = 12.dp, vertical = 6.dp)` – vnější mezera, pak barva, pak vnitřní odsazení. |
| **C** – po smazání Zlína je rozbalená Olomouc místo Ostravy | `items(mesta) { … }` bez `key` | Bez klíče se stav `remember` uvnitř položky váže na **pozici** v seznamu. Ostrava byla 3. položka. Po smazání Zlína se všechno posune o jednu nahoru a na 3. pozici je Olomouc, která „zdědí“ stav `rozbaleno = true`. | `items(items = mesta, key = { nazev -> nazev })` – stav se váže na město. Protože klíčem je název, Přidat nově nepřidá stejný název podruhé (jinak pád `Key "Brno" was already used`). Správné řešení je vlastní `id`, jako má `Mesto` v `MestaScreen.kt`. |
| **D** – texty v uvozovkách přímo v kódu | `Text("Název města")`, `Text("Přidat")`, `"Počet měst: ${mesta.size}"`, `Text("Smazat")`, `"Teplotu doplníme v lekci 11"` | Lint hlídá natvrdo zapsané texty jen v XML rozvrženích, ne v Compose, takže Studio nic nehlásí. Aplikace ale nejde přeložit a porušuje pravidlo z lekce 06. | Všechny texty přes `stringResource(...)`: `mesto_pole_label`, `mesto_pridat`, `debug_pocet_mest` (s parametrem `%1$d`), `mesto_smazat`, `debug_teplota_pozdeji`, česky i v `values-en`. |

## Bonus: architektonická vada

I po opravě je celá obrazovka **jedna funkce, která drží stav i kreslí UI**. Proto ji nejde dát do `@Preview` se čtyřmi konkrétními městy a nejde ji otestovat bez klikání. Správně by se rozdělila jako `MestaObrazovka` / `MestaObsah` v `MestaScreen.kt`: stavová funkce drží `novyNazev` a `mesta`, bezstavová dostane hodnoty a lambdy (`onNovyNazevChange`, `onPridat`, `onSmazat`) jako parametry. Stav `rozbaleno` by pak patřil do stavové funkce (například jako množina rozbalených id), ne do každé položky zvlášť.
