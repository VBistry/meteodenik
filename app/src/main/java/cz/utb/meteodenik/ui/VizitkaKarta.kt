package cz.utb.meteodenik.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.utb.meteodenik.R
import cz.utb.meteodenik.ui.theme.MeteoDenikTheme

@Composable
fun VizitkaKarta(
    jmeno: String,
    obor: String,
    email: String,
    modifier: Modifier = Modifier
) {
    // Iniciály: první písmeno každého slova, nejvýš dvě ("Jana Nováková" -> "JN").
    val inicialy = jmeno.split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.take(1).uppercase() }
    val popisAvataru = stringResource(R.string.vizitka_avatar_popis, jmeno)

    Card(
        modifier = modifier.fillMaxWidth(),
        // Vlastní barva plochy karty – z tématu, ne natvrdo.
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)   // clip PŘED background, jinak se vybarví čtverec
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .semantics { contentDescription = popisAvataru },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = inicialy,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            // weight(1f): sloupec dostane jen zbytek šířky, takže dlouhý text nevyleze z karty.
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = jmeno,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,   // výraznější než dva řádky pod ním
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = obor,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = stringResource(R.string.vizitka_email_popis, email),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// Ukázková data pro náhledy – jsou to data, ne texty rozhraní.
private const val UKAZKA_JMENO = "Jana Nováková"
private const val UKAZKA_OBOR = "Informační technologie, 2. ročník"
private const val UKAZKA_EMAIL = "j_novakova@utb.cz"

@Preview(name = "Vizitka — světlá", showBackground = true)
@Composable
private fun VizitkaKartaSvetlaPreview() {
    MeteoDenikTheme {
        VizitkaKarta(UKAZKA_JMENO, UKAZKA_OBOR, UKAZKA_EMAIL, Modifier.padding(16.dp))
    }
}

@Preview(name = "Vizitka — tmavá", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun VizitkaKartaTmavaPreview() {
    MeteoDenikTheme {
        VizitkaKarta(UKAZKA_JMENO, UKAZKA_OBOR, UKAZKA_EMAIL, Modifier.padding(16.dp))
    }
}

@Preview(name = "Vizitka — písmo 1,5×", showBackground = true, fontScale = 1.5f)
@Composable
private fun VizitkaKartaVelkePismoPreview() {
    MeteoDenikTheme {
        VizitkaKarta(UKAZKA_JMENO, UKAZKA_OBOR, UKAZKA_EMAIL, Modifier.padding(16.dp))
    }
}
