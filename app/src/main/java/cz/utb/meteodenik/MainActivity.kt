package cz.utb.meteodenik

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.utb.meteodenik.ui.theme.MeteoDenikTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MeteoDenikTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    bmiObrazovka(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MeteoObrazovka(modifier: Modifier = Modifier) {
    var zapsano by remember { mutableStateOf(false) }

    Column(modifier.padding(20.dp)) {
        Text(
            text = if (zapsano)
                stringResource(R.string.text_po_kliknuti)
            else
                stringResource(R.string.uvodni_text),
            color = MaterialTheme.colorScheme.primary
        )

        Button(onClick = { zapsano = true }) {
            Text(text = stringResource(R.string.akce_zmen_text))
        }
    }
}

@Preview(showBackground = true)
@Composable //pro test viewcka
fun MeteoObrazovkaPreview() {
    MeteoDenikTheme {
        MeteoObrazovka()
    }
}