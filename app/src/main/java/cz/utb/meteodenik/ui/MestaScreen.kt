package cz.utb.meteodenik.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.utb.meteodenik.R
import cz.utb.meteodenik.model.Mesto
import cz.utb.meteodenik.ui.theme.MeteoDenikTheme

@Composable
fun MestoKarta(
    mesto: Mesto,
    onSmazat: () -> Unit,   // cvičení 7.2: karta neví, jak se maže – jen to ohlásí
    modifier: Modifier = Modifier
) {
    val popisSmazani = stringResource(R.string.mesto_smazat_popis, mesto.nazev)
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .semantics { contentDescription = mesto.nazev },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = mesto.nazev.take(1).uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = mesto.nazev,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = if (mesto.teplotaC != null) {
                        stringResource(R.string.mesto_teplota, mesto.teplotaC)
                    } else {
                        stringResource(R.string.mesto_teplota_neznama)
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            // TextButton má cíl dotyku aspoň 48 dp; čtečka přečte „Smazat město Zlín“.
            TextButton(
                onClick = onSmazat,
                modifier = Modifier.semantics { contentDescription = popisSmazani }
            ) {
                Text(text = stringResource(R.string.mesto_smazat))
            }
        }
    }
}

internal val ukazkovaMesta = listOf(
    Mesto(id = 1, nazev = "Zlín", teplotaC = 18.5),
    Mesto(id = 2, nazev = "Brno", teplotaC = 21.0),
    Mesto(id = 3, nazev = "Uherské Hradiště", teplotaC = null)
)

@Preview(name = "Karta — světlá", showBackground = true)
@Preview(name = "Karta — tmavá", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MestoKartaPreview() {
    MeteoDenikTheme {
        MestoKarta(mesto = ukazkovaMesta.first(), onSmazat = {}, modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun PridatMestoRadek(
    nazev: String,
    jeChyba: Boolean,
    onNazevChange: (String) -> Unit,
    onPridat: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = nazev,
            onValueChange = onNazevChange,
            label = { Text(stringResource(R.string.mesto_pole_label)) },
            singleLine = true,
            isError = jeChyba,
            supportingText = if (jeChyba) {
                { Text(stringResource(R.string.mesto_duplikat)) }
            } else {
                null
            },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Button(
            onClick = onPridat,
            enabled = nazev.isNotBlank()
        ) {
            Text(text = stringResource(R.string.mesto_pridat))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PridatMestoRadekPreview() {
    MeteoDenikTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            PridatMestoRadek(nazev = "", jeChyba = false, onNazevChange = {}, onPridat = {})
            Spacer(modifier = Modifier.height(16.dp))
            PridatMestoRadek(nazev = "Zlín", jeChyba = false, onNazevChange = {}, onPridat = {})
        }
    }
}

@Composable
fun MestaSeznam(
    mesta: List<Mesto>,
    onSmazatMesto: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (mesta.isEmpty()) {
        Text(
            text = stringResource(R.string.mesta_prazdno),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = modifier.padding(vertical = 32.dp)
        )
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = mesta,
                key = { mesto -> mesto.id }
            ) { mesto ->
                // id doplní až seznam – karta zná jen „smaž mě“.
                MestoKarta(mesto = mesto, onSmazat = { onSmazatMesto(mesto.id) })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)   // TopAppBar je v Material 3 stále experimentální
@Composable
fun MestaObsah(
    mesta: List<Mesto>,
    novyNazev: String,
    jeDuplikat: Boolean,
    onNovyNazevChange: (String) -> Unit,
    onPridatMesto: () -> Unit,
    onSmazatMesto: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.mesta_titulek)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            PridatMestoRadek(
                nazev = novyNazev,
                jeChyba = jeDuplikat,
                onNazevChange = onNovyNazevChange,
                onPridat = onPridatMesto
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            // Počet měst: plurál vybere tvar (1 město, 3 města, 5 měst).
            Text(
                text = pluralStringResource(R.plurals.mesta_pocet, mesta.size, mesta.size),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            MestaSeznam(mesta = mesta, onSmazatMesto = onSmazatMesto)
        }
    }
}

@Composable
fun MestaObrazovka(modifier: Modifier = Modifier) {
    // Jediné místo v celé obrazovce, kde bydlí stav.
    // V lekci 08 převezme seznam měst ViewModel; komponenty pod ní se nezmění.
    var novyNazev by rememberSaveable { mutableStateOf("") }
    var mesta by remember { mutableStateOf(ukazkovaMesta) }
    var jeDuplikat by rememberSaveable { mutableStateOf(false) }

    MestaObsah(
        mesta = mesta,
        novyNazev = novyNazev,
        jeDuplikat = jeDuplikat,
        onNovyNazevChange = {
            novyNazev = it
            jeDuplikat = false   // chybu ruší každá změna textu
        },
        onPridatMesto = {
            val nazev = novyNazev.trim()
            // Duplikát bez ohledu na velikost písmen a mezery okolo.
            val uzExistuje = mesta.any { it.nazev.trim().equals(nazev, ignoreCase = true) }
            when {
                nazev.isEmpty() -> Unit
                uzExistuje -> jeDuplikat = true
                else -> {
                    mesta = mesta + Mesto(
                        id = (mesta.maxOfOrNull { it.id } ?: 0) + 1,
                        nazev = nazev,
                        teplotaC = null
                    )
                    novyNazev = ""
                }
            }
        },
        onSmazatMesto = { id ->
            mesta = mesta.filterNot { it.id == id }   // nový seznam, ne remove() na starém
        },
        modifier = modifier
    )
}

@Preview(name = "Obrazovka — světlá", showBackground = true)
@Preview(name = "Obrazovka — tmavá", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MestaObsahPreview() {
    MeteoDenikTheme {
        MestaObsah(
            mesta = ukazkovaMesta,
            novyNazev = "Otrokovice",
            jeDuplikat = false,
            onNovyNazevChange = {},
            onPridatMesto = {},
            onSmazatMesto = {}
        )
    }
}

@Preview(name = "Obrazovka — duplicitní město", showBackground = true)
@Composable
private fun MestaObsahChybaPreview() {
    MeteoDenikTheme {
        MestaObsah(
            mesta = ukazkovaMesta,
            novyNazev = "zlín ",
            jeDuplikat = true,
            onNovyNazevChange = {},
            onPridatMesto = {},
            onSmazatMesto = {}
        )
    }
}

@Preview(name = "Obrazovka — prázdný seznam", showBackground = true)
@Composable
private fun MestaObsahPrazdnaPreview() {
    MeteoDenikTheme {
        MestaObsah(
            mesta = emptyList(),
            novyNazev = "",
            jeDuplikat = false,
            onNovyNazevChange = {},
            onPridatMesto = {},
            onSmazatMesto = {}
        )
    }
}
