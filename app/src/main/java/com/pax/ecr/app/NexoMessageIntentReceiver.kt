package com.pax.ecr.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.nio.charset.Charset

class NexoMessageIntentReceiver : BroadcastReceiver() {
    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        val nexoMessage = intent.extras?.getByteArray(Intent.EXTRA_TEXT)?.toString(Charset.defaultCharset())
        moveToForeground(context)
        responseText = nexoMessage ?: ""
    }

    private fun moveToForeground(context: Context) {
        Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        }.also { context.startActivity(it) }
    }
}
