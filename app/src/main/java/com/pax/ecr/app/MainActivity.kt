package com.pax.ecr.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.pax.ecr.app.ui.screen.ConfigScreen
import com.pax.ecr.app.ui.screen.MainScreen
import com.pax.ecr.app.ui.screen.ResponseScreen
import com.pax.ecr.app.ui.theme.PaxTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.charset.Charset
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.random.Random
import kotlin.time.DurationUnit
import kotlin.time.toDuration

var responseText by mutableStateOf("")
var config by mutableStateOf(Config.DEFAULT)
var configMenuVisible by mutableStateOf(false)
var lastTransactionId by mutableStateOf("")
var lastTransactionDatetime by mutableStateOf("")
var lastResponseTransactionId by mutableStateOf("")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideNavBar()
        config = restoreConfig()
        configMenuVisible = !config.isValid()
        setContent {
            PaxTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                    onClick = ::hideNavBar,
                ) {
                    if (responseText.isNotBlank()) {
                        ResponseScreen(response = responseText) {
                            responseText = ""
                        }
                    } else if (configMenuVisible) {
                        ConfigScreen(config, { configMenuVisible = false }) {
                            config = it
                            saveConfig(it)
                        }
                    } else {
                        MainScreen(
                            modifier = Modifier.fillMaxSize(),
                            handleAdminAction = ::handleAdminAction,
                            handleAction = ::handleAction,
                            onModalClose = ::hideNavBar,
                        )
                    }
                }
            }
        }
    }

    private fun restoreConfig() =
        getSharedPreferences("config", Context.MODE_PRIVATE).let {
            it.getString("config", null)?.let { configJson -> Json.decodeFromString<Config>(configJson) } ?: Config.DEFAULT
        }

    override fun onDestroy() {
        super.onDestroy()
        saveConfig(config)
    }

    private fun saveConfig(config: Config) {
        val sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("config", Json.encodeToString(config))
            apply()
        }
    }

    @Suppress("DEPRECATION")
    private fun hideNavBar() {
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

    private fun handleAdminAction(action: AdminAction) {
        hideNavBar()
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

            AdminAction.OPEN_CONFIG_MENU -> {
                openConfigMenu()
            }
        }
    }

    private fun openConfigMenu() {
        hideNavBar()
        configMenuVisible = true
    }

    private fun handleAction(action: Action) {
        hideNavBar()
        when (action) {
            Action.ADMIN -> sendAdminIntent(AdminAction.OPEN_ADMIN_MENU)
            Action.LOGIN -> sendMessageIntent(loginRequest())
            Action.LOGOUT -> sendMessageIntent(logout())
            Action.PURCHASE -> sendMessageIntent(payment())
            Action.PURCHASE_W_CASHBACK -> sendMessageIntent(paymentWithCashback())
            Action.REFUND -> sendMessageIntent(refund())
            Action.REVERSAL -> sendMessageIntent(reversal())
        }
    }

    private fun showThenHide() =
        CoroutineScope(Dispatchers.IO).launch {
            sendAdminIntent(AdminAction.MOVE_TO_FRONT)
            delay(5.toDuration(DurationUnit.SECONDS))
            sendAdminIntent(AdminAction.MOVE_TO_BACK)
        }

    private fun sendMessageIntent(data: ByteArray) {
        hideNavBar()
        Intent().also { intent ->
            intent.setAction("com.optomany.AxeptPro.intent.TERMINAL_NEXO_MESSAGE")
            intent.putExtra(Intent.EXTRA_TEXT, data)
            sendBroadcast(intent)
        }
    }

    private fun sendAdminIntent(adminAction: AdminAction) {
        hideNavBar()
        Intent().also { intent ->
            intent.setAction("com.optomany.AxeptPro.intent.TERMINAL_ADMIN")
            intent.putExtra(Intent.EXTRA_TEXT, adminAction.name)
            sendBroadcast(intent)
        }
    }

    private fun randomServiceId() = Random.nextInt(0, Int.MAX_VALUE)

    private fun randomTransactionId() =
        Random.nextInt(0, Int.MAX_VALUE).also {
            lastTransactionId = it.toString()
        }

    private fun now() = ZonedDateTime.now(ZoneId.of("Europe/Oslo")).toString()

    private fun transactionTimestamp() =
        now().also {
            lastTransactionDatetime = it
        }

    private fun loginRequest() =
        """
        <SaleToPOIRequest>
            <MessageHeader MessageCategory="Login" MessageClass="Service" MessageType="Request" POIID="${config.poiId}" ProtocolVersion="3.1" SaleID="${config.saleId}" ServiceID="${randomServiceId()}"/>
            <LoginRequest OperatorID="Cashier1" OperatorLanguage="en" ShiftNumber="2">
                <DateTime>${now()}</DateTime>
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
            <MessageHeader MessageCategory="Logout" MessageClass="Service" MessageType="Request" POIID="${config.poiId}" ProtocolVersion="3.1" SaleID="${config.saleId}" ServiceID="${randomServiceId()}"/>
            <LogoutRequest MaintenanceAllowed="true"/>
        </SaleToPOIRequest>
        """.trimIndent().toByteArray(Charset.defaultCharset())

    private fun payment() =
        """
        <SaleToPOIRequest>
            <MessageHeader MessageCategory="Payment" MessageClass="Service" MessageType="Request" POIID="${config.poiId}" ProtocolVersion="3.1" SaleID="${config.saleId}" ServiceID="${randomServiceId()}"/>
            <PaymentRequest>
                <SaleData TokenRequestedType="Customer">
                    <SaleTransactionID TimeStamp="${transactionTimestamp()}" TransactionID="${randomTransactionId()}"/>
                </SaleData>
                <PaymentTransaction>
                    <AmountsReq CashBackAmount="0" Currency="${config.currencyCode}"  RequestedAmount="100"/>
                </PaymentTransaction>
            </PaymentRequest>
        </SaleToPOIRequest>
        """.trimIndent().toByteArray(Charset.defaultCharset())

    private fun paymentWithCashback() =
        """
        <SaleToPOIRequest>
            <MessageHeader MessageCategory="Payment" MessageClass="Service" MessageType="Request" POIID="${config.poiId}" ProtocolVersion="3.1" SaleID="${config.saleId}" ServiceID="${randomServiceId()}"/>
            <PaymentRequest>
                <SaleData TokenRequestedType="Customer">
                    <SaleTransactionID TimeStamp="${transactionTimestamp()}" TransactionID="${randomTransactionId()}"/>
                </SaleData>
                <PaymentTransaction>
                    <AmountsReq CashBackAmount="50" Currency="${config.currencyCode}"  RequestedAmount="100"/>
                </PaymentTransaction>
            </PaymentRequest>
        </SaleToPOIRequest>
        """.trimIndent().toByteArray(Charset.defaultCharset())

    private fun refund() =
        """
        <SaleToPOIRequest>
            <MessageHeader MessageCategory="Payment" MessageClass="Service" MessageType="Request" POIID="${config.poiId}" ProtocolVersion="3.1" SaleID="${config.saleId}" ServiceID="${randomServiceId()}"/>
            <PaymentRequest>
                <SaleData TokenRequestedType="Customer">
                    <SaleTransactionID TimeStamp="${transactionTimestamp()}" TransactionID="${randomTransactionId()}"/>
                </SaleData>
                <PaymentTransaction>
                    <AmountsReq Currency="${config.currencyCode}"  RequestedAmount="100"/>
                </PaymentTransaction>
                <PaymentData PaymentType="Refund"/>
            </PaymentRequest>
        </SaleToPOIRequest>
        """.trimIndent().toByteArray(Charset.defaultCharset())

    private fun reversal() =
        """
        <SaleToPOIRequest>
            <MessageHeader MessageCategory="Reversal" MessageClass="Service" MessageType="Request" POIID="${config.poiId}" SaleID="${config.saleId}" ServiceID="${randomServiceId()}"/>
            <ReversalRequest ReversalReason="MerchantCancel">
                <OriginalPOITransaction POIID="${config.poiId}" SaleID="${config.saleId}">
                    <POITransactionID TimeStamp="$lastTransactionDatetime" TransactionID="$lastResponseTransactionId" />
                </OriginalPOITransaction>
            </ReversalRequest>
        </SaleToPOIRequest>
        """.trimIndent().toByteArray(Charset.defaultCharset())
}
