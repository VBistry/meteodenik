package cz.utb.meteodenik.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.utb.meteodenik.R

// Cvičení 7.6 – opravená verze. Rozbor všech čtyř chyb je v docs/lekce-07-debug.md.
@Composable
fun RozbityMestaSeznam(modifier: Modifier = Modifier) {
    // Oprava A: obyčejná `var novyNazev = ""` se při každé rekompozici vytvořila znovu
    // a Compose její změnu nesledoval. Teď je to sledovaný stav, který přežije i otočení.
    var novyNazev by rememberSaveable { mutableStateOf("") }
    var mesta by remember { mutableStateOf(listOf("Zlín", "Brno", "Ostrava", "Olomouc")) }

    Column(modifier = modifier.padding(16.dp)) {
        OutlinedTextField(
            value = novyNazev,
            onValueChange = { novyNazev = it },
            label = { Text(stringResource(R.string.mesto_pole_label)) },   // Oprava D
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = {
            val nazev = novyNazev.trim()
            // Klíčem seznamu je název, proto stejný název podruhé nepřidáme (jinak pád na duplicitním klíči).
            if (nazev.isNotEmpty() && nazev !in mesta) {
                mesta = mesta + nazev
                novyNazev = ""
            }
        }) {
            Text(stringResource(R.string.mesto_pridat))                       // Oprava D
        }
        Text(
            text = stringResource(R.string.debug_pocet_mest, mesta.size),     // Oprava D
            style = MaterialTheme.typography.labelLarge,
            // Oprava B: pořadí modifikátorů. Nejdřív vnější mezera, pak barva, pak vnitřní odsazení,
            // takže barevná ploška je kolem textu a text v ní „dýchá“.
            modifier = Modifier
                .padding(vertical = 8.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(horizontal = 12.dp, vertical = 6.dp)
        )
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // Oprava C: key = { it } váže stav `rozbaleno` na město, ne na pozici v seznamu.
            items(items = mesta, key = { nazev -> nazev }) { nazev ->
                var rozbaleno by remember { mutableStateOf(false) }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { rozbaleno = !rozbaleno }
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = nazev, modifier = Modifier.weight(1f))
                            TextButton(onClick = { mesta = mesta - nazev }) {
                                Text(stringResource(R.string.mesto_smazat))   // Oprava D
                            }
                        }
                        if (rozbaleno) {
                            Text(
                                text = stringResource(R.string.debug_teplota_pozdeji),   // Oprava D
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}
