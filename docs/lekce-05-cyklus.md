# Lekce 05 – tabulka životního cyklu

Měřeno na emulátoru Pixel 8, Android 17 (API 37), filtr Logcatu `package:mine tag:Cyklus`.

| Scénář | Můj předpoklad | Skutečnost z Logcatu | Proč se to liší |
|---|---|---|---|
| 1. Start aplikace z ikony | onCreate -> onStart -> onResume | onCreate (savedInstanceState = null) -> onStart -> onResume | Neliší se. Instance vzniká poprvé, systém nemá žádný uložený stav, proto je Bundle null. |
| 2. Rotace displeje | onPause -> onStop (aktivita se jen překreslí) | onPause -> onStop -> onDestroy (isFinishing = false) -> onCreate (savedInstanceState = Bundle[…], ne null) -> onStart -> onResume | Rotace je změna konfigurace. Systém starou instanci zničí (isFinishing = false, nekončí ji uživatel) a vytvoří novou, aby načetla zdroje pro nové rozměry. Stav staré instance předá novému onCreate v Bundle. |
| 3. Stisk Home | onPause -> onStop -> onDestroy | onPause -> onStop | Home aktivitu neukončuje, jen ji schová. Instance zůstává v paměti ve stavu Stopped, proto onDestroy nepřijde. |
| 4. Návrat do aplikace | onCreate -> onStart -> onResume | onRestart -> onStart -> onResume | Vrací se tatáž instance, která po Home zůstala v paměti. Proto onRestart místo onCreate (stejné PID 6228 jako při startu). |
| 5. Stisk Zpět na MainActivity | onPause -> onStop (Android 12+, bez onDestroy) | onPause -> onStop -> onDestroy (isFinishing = true) | Skripta uvádějí, že od Androidu 12 systém kořenovou aktivitu spuštěnou z ikony při Zpět jen přesune na pozadí. Na tomto emulátoru (Android 17, targetSdk 37) se ale aktivita ukončila, ověřeno tlačítkem i gestem Zpět. isFinishing = true ukazuje, že ji ukončil uživatel, ne systém. |
| 6. adb shell am kill na pozadí + návrat | onPause -> onStop -> onDestroy, po návratu onCreate | Před kill: onPause -> onStop. Po návratu: onCreate (savedInstanceState = Bundle[…], ne null) -> onStart -> onResume. onDestroy nepřišel, isFinishing se tedy nevypsal. | am kill zabije celý proces. Kód staré instance už nemá kde běžet, proto onDestroy nikdy nepřijde. Po návratu běží nový proces (PID 6535 místo 6228). Bundle není null, protože systém stav uložil už při odchodu na pozadí (po onStop). |

## Další pozorování

- **Otevření detailu:** MainActivity onPause -> DetailMestaActivity onCreate -> onStart -> onResume -> MainActivity onStop. Callbacky obou aktivit se prokládají. Stará obrazovka se zastaví až poté, co je nová vidět.
- **Zpět z detailu:** DetailMestaActivity onPause -> MainActivity onRestart -> onStart -> onResume -> DetailMestaActivity onStop -> onDestroy (isFinishing = true).
- **Panel sdílení:** jen DetailMestaActivity onPause, po zavření onResume. Detail je pod panelem pořád vidět, proto nepřijde onStop.
- **Broadcast:** na popředí dorazí (Receiver onReceive mesto=Zlín). Po Home (onStop -> unregisterReceiver) už nedorazí.
