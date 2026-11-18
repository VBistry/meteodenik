package cz.utb.meteodenik.ui

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.utb.meteodenik.DetailMestaActivity
import cz.utb.meteodenik.R
import cz.utb.meteodenik.ZaznamUlozenReceiver
import cz.utb.meteodenik.model.UKAZKOVA_MESTA

@Composable
fun SeznamMestObrazovka() {
    val context = LocalContext.current          // Context pro startActivity – na začátku funkce

    Scaffold { insety ->
        Column(modifier = Modifier.padding(insety).padding(16.dp)) {
            val pocet = UKAZKOVA_MESTA.size
            Text(
                text = stringResource(
                    R.string.seznam_titulek_pocet,
                    pluralStringResource(R.plurals.pocet_zaznamu, pocet, pocet)
                ),
                style = MaterialTheme.typography.headlineSmall
            )
            PolohaKarta(onPolohaPovolena = {})
            UKAZKOVA_MESTA.forEach { mesto ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val intent = Intent(context, DetailMestaActivity::class.java).apply {
                                putExtra(DetailMestaActivity.EXTRA_MESTO, mesto.nazev)
                                putExtra(DetailMestaActivity.EXTRA_TEPLOTA, mesto.teplota)
                            }
                            context.startActivity(intent)
                        }
                        .padding(16.dp)
                ) {
                    Text(mesto.nazev)
                    Text(stringResource(R.string.teplota_format, mesto.teplota))
                }
            }
            Button(onClick = {
                context.sendBroadcast(
                    Intent(ZaznamUlozenReceiver.ACTION_ZAZNAM_ULOZEN).apply {
                        setPackage(context.packageName)
                        putExtra(ZaznamUlozenReceiver.EXTRA_MESTO, "Zlín")
                    }
                )
            }) {
                Text(stringResource(R.string.poslat_zpravu))
            }
        }
    }
}
