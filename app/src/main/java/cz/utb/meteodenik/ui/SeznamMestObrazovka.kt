package cz.utb.meteodenik.ui

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.utb.meteodenik.DetailMestaActivity
import cz.utb.meteodenik.R
import cz.utb.meteodenik.ZaznamUlozenReceiver

@Composable
fun SeznamMestObrazovka() {
    val context = LocalContext.current          // Context pro startActivity – na začátku funkce

    Scaffold { insety ->
        Column(modifier = Modifier.padding(insety).padding(16.dp)) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineSmall
            )
            Button(onClick = {
                val intent = Intent(context, DetailMestaActivity::class.java).apply {
                    putExtra(DetailMestaActivity.EXTRA_MESTO, "Zlín")
                    putExtra(DetailMestaActivity.EXTRA_TEPLOTA, 21.4)
                }
                context.startActivity(intent)
            }) {
                Text(stringResource(R.string.otevri_detail))
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
