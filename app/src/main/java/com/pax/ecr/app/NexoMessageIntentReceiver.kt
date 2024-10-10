package com.pax.ecr.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.json.Json
import org.w3c.dom.Document
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import org.xml.sax.SAXParseException
import java.io.StringReader
import java.nio.charset.Charset
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class NexoMessageIntentReceiver : BroadcastReceiver() {
    private val json = Json { ignoreUnknownKeys = true }

    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        val nexoMessage = intent.extras?.getByteArray(Intent.EXTRA_TEXT)?.toString(Charset.defaultCharset())
        moveToForeground(context)
        lastResponseTransactionId = nexoMessage.extractPOITransactionID()
        lastTransactionDatetime = nexoMessage.extractPOITimeStamp()
        responseText = nexoMessage.orEmpty()
        receiptData = nexoMessage.extractCustomerReceipt()?.toReceiptData(receiptElements).orEmpty()
        if (nexoMessage.isLoginResponseFailure()) {
            Toast.makeText(
                context,
                "Login failed. Perhaps the POI is new? Try again in a few seconds.",
                Toast.LENGTH_LONG,
            ).show()
            selectedMode = null
        }
    }

    private fun moveToForeground(context: Context) {
        Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        }.also { context.startActivity(it) }
    }

    @OptIn(ExperimentalEncodingApi::class, ExperimentalSerializationApi::class)
    private fun String?.extractCustomerReceipt() =
        try {
            val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
            val inputSource = InputSource(StringReader(this))
            val document: Document = documentBuilder.parse(inputSource)

            val xPath = XPathFactory.newInstance().newXPath()
            val expression = "//PaymentReceipt[@DocumentQualifier='CustomerReceipt']/OutputContent/OutputText"
            val nodeList = xPath.evaluate(expression, document, XPathConstants.NODESET) as NodeList

            if (nodeList.length > 0) {
                val base64EncodedText = nodeList.item(0).textContent.trim()
                val decodedBytes = Base64.decode(base64EncodedText)
                json.decodeFromString<CardholderReceipt>(String(decodedBytes, Charsets.UTF_8))
            } else {
                null
            }
        } catch (e: SAXParseException) {
            null
        } catch (e: NullPointerException) {
            null
        } catch (e: MissingFieldException) {
            null
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
