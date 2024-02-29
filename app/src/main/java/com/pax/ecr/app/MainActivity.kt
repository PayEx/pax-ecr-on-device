package com.pax.ecr.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.pax.ecr.app.ui.theme.PaxecrondeviceTheme
import java.nio.charset.Charset
import kotlin.random.Random

var responseText by mutableStateOf("The response will be displayed here")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PaxecrondeviceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Column {
                        Column(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .weight(2f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Button(onClick = { sendIntent(loginRequest()) }) {
                                Text(text = "Login")
                            }
                            Button(onClick = { sendIntent(payment()) }) {
                                Text(text = "Payment")
                            }
                            Button(onClick = { sendIntent(logout()) }) {
                                Text(text = "Logout")
                            }
                        }
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .weight(1f),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(text = responseText)
                        }
                    }
                }
            }
        }
    }

    private fun sendIntent(data: ByteArray) {
        Intent().also { intent ->
            intent.setAction("com.optomany.AxeptPro.intent.TERMINAL_NEXO_MESSAGE")
            intent.putExtra(Intent.EXTRA_TEXT, data)
            sendBroadcast(intent)
        }
    }

    private fun randomServiceId() = Random.nextInt(0, Int.MAX_VALUE)

    private fun loginRequest() =
        """
        <SaleToPOIRequest>
            <MessageHeader MessageCategory="Login" MessageClass="Service" MessageType="Request" POIID="klev" ProtocolVersion="3.1" SaleID="ECR1" ServiceID="${randomServiceId()}"/>
            <LoginRequest OperatorID="Cashier1" OperatorLanguage="en" ShiftNumber="2">
                <DateTime>2024-02-29T09:46:44.024097+01:00</DateTime>
                <SaleSoftware ApplicationName="TestScripts" CertificationCode="ECTS2PS001" ProviderIdentification="swedbankpay" SoftwareVersion="1.0"/>
                <SaleTerminalData TerminalEnvironment="Attended">
                    <SaleCapabilities>PrinterReceipt CashierStatus CashierError CashierDisplay CashierInput</SaleCapabilities>
                    <SaleProfile/>
                </SaleTerminalData>
            </LoginRequest>
        </SaleToPOIRequest>
        """.trimIndent().toByteArray(Charset.defaultCharset())

    private fun logout() =
        """
        <SaleToPOIRequest>
            <MessageHeader MessageCategory="Logout" MessageClass="Service" MessageType="Request" POIID="klev" ProtocolVersion="3.1" SaleID="ECR1" ServiceID="${randomServiceId()}"/>
            <LogoutRequest MaintenanceAllowed="true"/>
        </SaleToPOIRequest>
        """.trimIndent().toByteArray(Charset.defaultCharset())

    private fun payment() =
        """
        <SaleToPOIRequest>
            <MessageHeader MessageCategory="Payment" MessageClass="Service" MessageType="Request" POIID="klev" ProtocolVersion="3.1" SaleID="ECR1" ServiceID="${randomServiceId()}"/>
            <PaymentRequest>
                <SaleData TokenRequestedType="Customer">
                    <SaleTransactionID TimeStamp="2024-02-29T12:10:55.389697+01:00" TransactionID="2536476465"/>
                </SaleData>
                <PaymentTransaction>
                    <AmountsReq CashBackAmount="0" Currency="SEK" RequestedAmount="100"/>
                </PaymentTransaction>
            </PaymentRequest>
        </SaleToPOIRequest>
        """.trimIndent().toByteArray(Charset.defaultCharset())
}
