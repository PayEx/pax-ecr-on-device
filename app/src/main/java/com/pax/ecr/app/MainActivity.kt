package com.pax.ecr.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.pax.ecr.app.ui.screen.MainScreen
import com.pax.ecr.app.ui.screen.ResponseScreen
import com.pax.ecr.app.ui.theme.PaxTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.nio.charset.Charset
import kotlin.random.Random
import kotlin.time.DurationUnit
import kotlin.time.toDuration

var responseText by mutableStateOf("")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PaxTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    if (responseText.isNotBlank()) {
                        ResponseScreen(response = responseText) {
                            responseText = ""
                        }
                    } else {
                        MainScreen(
                            modifier = Modifier.fillMaxSize(),
                            handleAdminAction = ::handleAdminAction,
                            handleAction = ::handleAction,
                        )
                    }
                }
            }
        }
    }

    private fun handleAdminAction(action: AdminAction) {
        when (action) {
            AdminAction.OPEN_ADMIN_MENU -> {
                sendAdminIntent(AdminAction.OPEN_ADMIN_MENU)
            }

            AdminAction.MOVE_TO_FRONT -> {
                sendAdminIntent(AdminAction.MOVE_TO_FRONT)
            }

            AdminAction.MOVE_TO_BACK -> {
                sendAdminIntent(AdminAction.MOVE_TO_BACK)
            }

            AdminAction.TEMPORARY_SHOW -> {
                showThenHide()
            }
        }
    }

    private fun handleAction(action: Action) =
        when (action) {
            Action.ADMIN -> sendAdminIntent(AdminAction.OPEN_ADMIN_MENU)
            Action.LOGIN -> sendMessageIntent(loginRequest())
            Action.LOGOUT -> sendMessageIntent(logout())
            Action.PURCHASE -> sendMessageIntent(payment())
            Action.PURCHASE_W_CASHBACK -> sendMessageIntent(paymentWithCashback())
            Action.REFUND -> sendMessageIntent(refund())
            Action.REVERSAL -> sendMessageIntent(reversal())
        }

    private fun showThenHide() =
        CoroutineScope(Dispatchers.IO).launch {
            sendAdminIntent(AdminAction.MOVE_TO_FRONT)
            delay(5.toDuration(DurationUnit.SECONDS))
            sendAdminIntent(AdminAction.MOVE_TO_BACK)
        }

    private fun sendMessageIntent(data: ByteArray) {
        Intent().also { intent ->
            intent.setAction("com.optomany.AxeptPro.intent.TERMINAL_NEXO_MESSAGE")
            intent.putExtra(Intent.EXTRA_TEXT, data)
            sendBroadcast(intent)
        }
    }

    private fun sendAdminIntent(adminAction: AdminAction) {
        Intent().also { intent ->
            intent.setAction("com.optomany.AxeptPro.intent.TERMINAL_ADMIN")
            intent.putExtra(Intent.EXTRA_TEXT, adminAction.name)
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

    private fun paymentWithCashback() =
        """
        <SaleToPOIRequest>
            <MessageHeader MessageCategory="Payment" MessageClass="Service" MessageType="Request" POIID="klev" ProtocolVersion="3.1" SaleID="ECR1" ServiceID="${randomServiceId()}"/>
            <PaymentRequest>
                <SaleData TokenRequestedType="Customer">
                    <SaleTransactionID TimeStamp="2024-02-29T12:10:55.389697+01:00" TransactionID="2536476465"/>
                </SaleData>
                <PaymentTransaction>
                    <AmountsReq CashBackAmount="50" Currency="SEK" RequestedAmount="100"/>
                </PaymentTransaction>
            </PaymentRequest>
        </SaleToPOIRequest>
        """.trimIndent().toByteArray(Charset.defaultCharset())

    private fun refund() =
        """
        <SaleToPOIRequest>
            <MessageHeader MessageCategory="Payment" MessageClass="Service" MessageType="Request" POIID="1995" ProtocolVersion="3.1" SaleID="ECR1" ServiceID="${randomServiceId()}"/>
            <PaymentRequest>
                <SaleData TokenRequestedType="Customer">
                    <SaleTransactionID TimeStamp="2024-05-03T13:02:39.187273+02:00" TransactionID="2487784444"/>
                </SaleData>
                <PaymentTransaction>
                    <AmountsReq Currency="SEK" RequestedAmount="100"/>
                </PaymentTransaction>
                <PaymentData PaymentType="Refund"/>
            </PaymentRequest>
        </SaleToPOIRequest>
        """.trimIndent().toByteArray(Charset.defaultCharset())

    private fun reversal() =
        """
        <SaleToPOIRequest>
            <MessageHeader MessageCategory="Reversal" MessageClass="Service" MessageType="Request" POIID="1995" SaleID="ECR1" ServiceID="${randomServiceId()}"/>
            <ReversalRequest ReversalReason="CustCancel">
                <OriginalPOITransaction POIID="1995" SaleID="ECR1">
                    <POITransactionID TimeStamp="null" TransactionID="null"/>
                </OriginalPOITransaction>
            </ReversalRequest>
        </SaleToPOIRequest>
        """.trimIndent().toByteArray(Charset.defaultCharset())
}
