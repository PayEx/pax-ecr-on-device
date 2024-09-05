package com.pax.ecr.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import org.w3c.dom.Document
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
        lastTransactionDatetime = nexoMessage.extractPOITimeStamp()
        responseText = nexoMessage.orEmpty()
        purchaseMenuVisible = false
        if (nexoMessage.isLoginResponseFailure()) {
            Toast.makeText(
                context,
                "Login failed",
                Toast.LENGTH_LONG,
            ).show()
        } else if (nexoMessage.isLoginResponseSuccess()) {
            isLoggedIn = true
        }
    }

    private fun moveToForeground(context: Context) {
        Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        }.also { context.startActivity(it) }
    }

    private fun String?.extractPOITransactionID() =
        extractCommon {
            val poiTransactionIDNode = it.getElementsByTagName("POITransactionID")?.item(0)
            poiTransactionIDNode?.attributes?.getNamedItem("TransactionID")?.nodeValue.orEmpty()
        }

    private fun String?.extractPOITimeStamp() =
        extractCommon {
            val poiTimeStampNode = it.getElementsByTagName("POITransactionID")?.item(0)
            poiTimeStampNode?.attributes?.getNamedItem("TimeStamp")?.nodeValue.orEmpty()
        }

    private fun String?.isLoginResponseFailure() =
        extractCommon {
            it.getElementsByTagName("LoginResponse")?.item(0)
                ?.childNodes?.item(0)
                ?.attributes?.getNamedItem("Result")
                ?.nodeValue ?: ""
        }.let { it != "Success" && it.isNotBlank() }

    private fun String?.isLoginResponseSuccess() =
        extractCommon {
            it.getElementsByTagName("LoginResponse")?.item(0)
                ?.childNodes?.item(0)
                ?.attributes?.getNamedItem("Result")
                ?.nodeValue ?: ""
        }.let { it == "Success" }

    private fun String?.extractCommon(extraction: (Document) -> String) =
        try {
            val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
            val inputSource = InputSource(StringReader(this))
            extraction(documentBuilder.parse(inputSource))
        } catch (e: SAXParseException) {
            ""
        } catch (e: NullPointerException) {
            ""
        }
}
