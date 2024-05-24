package com.pax.ecr.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.xml.sax.InputSource
import org.xml.sax.SAXParseException
import java.io.StringReader
import java.nio.charset.Charset
import javax.xml.parsers.DocumentBuilderFactory

class NexoMessageIntentReceiver : BroadcastReceiver() {
    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        val nexoMessage = intent.extras?.getByteArray(Intent.EXTRA_TEXT)?.toString(Charset.defaultCharset())
        moveToForeground(context)
        lastResponseTransactionId = nexoMessage.extractPOITransactionID()
        responseText = nexoMessage.orEmpty()
    }

    private fun moveToForeground(context: Context) {
        Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        }.also { context.startActivity(it) }
    }

    private fun String?.extractPOITransactionID() =
        try {
            val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
            val inputSource = InputSource(StringReader(this))
            val document = documentBuilder.parse(inputSource)

            val poiTransactionIDNode = document.getElementsByTagName("POITransactionID")?.item(0)
            poiTransactionIDNode?.attributes?.getNamedItem("TransactionID")?.nodeValue.orEmpty()
        } catch (e: SAXParseException) {
            ""
        } catch (e: NullPointerException) {
            ""
        }
}
