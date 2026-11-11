package cz.utb.meteodenik

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ZaznamUlozenReceiver(
    private val naZaznam: (String) -> Unit
) : BroadcastReceiver() {

    companion object {
        const val ACTION_ZAZNAM_ULOZEN = "cz.utb.meteodenik.action.ZAZNAM_ULOZEN"
        const val EXTRA_MESTO = "cz.utb.meteodenik.extra.MESTO"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val mesto = intent.getStringExtra(EXTRA_MESTO) ?: return
        Log.d("Cyklus", "Receiver  onReceive  mesto=$mesto")
        naZaznam(mesto)
    }
}
