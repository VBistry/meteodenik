package cz.utb.meteodenik

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun bmiObrazovka(modifier: Modifier = Modifier) {
    var vyska by remember { mutableStateOf("") }
    var vaha by remember { mutableStateOf("") }
    var bmi by remember { mutableStateOf<Double?>(null) }
    var chyba by remember { mutableStateOf(false) }

    Column(modifier.padding(20.dp)) {
        OutlinedTextField(
            value = vyska, onValueChange = { vyska = it },
            label = { Text(stringResource(R.string.bmi_vyska)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        OutlinedTextField(
            value = vaha, onValueChange = { vaha = it },
            label = { Text(stringResource(R.string.bmi_vaha)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        Button(onClick = {
            val v = vyska.toDoubleOrNull()?.div(100)
            val m = vaha.toDoubleOrNull()
            if (v != null && m != null && v > 0) {
                bmi = m / (v * v)
                chyba = false
            } else {
                bmi = null
                chyba = true
            }
        }) {
            Text(stringResource(R.string.bmi_vypocet))
        }

        if (chyba) {
            Text(stringResource(R.string.bmi_chyba))
        }
        val vysledek = bmi
        if (vysledek != null) {
            Text(stringResource(R.string.bmi_vysledek, vysledek))
        }
    }
}

