package com.pax.ecr.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.pax.ecr.app.NexoMessages.loginRequest
import com.pax.ecr.app.NexoMessages.logout
import com.pax.ecr.app.NexoMessages.payment
import com.pax.ecr.app.NexoMessages.paymentWithCashback
import com.pax.ecr.app.NexoMessages.refund
import com.pax.ecr.app.NexoMessages.reversal
import com.pax.ecr.app.ui.screen.ModeSelectorScreen
import com.pax.ecr.app.ui.screen.PaymentModeScreen
import com.pax.ecr.app.ui.screen.config.ConfigScreen
import com.pax.ecr.app.ui.screen.config.ResponseScreen
import com.pax.ecr.app.ui.screen.restaurant.RestaurantScreen
import com.pax.ecr.app.ui.screen.retail.RetailerScreen
import com.pax.ecr.app.ui.theme.PaxTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.math.BigDecimal
import kotlin.time.DurationUnit
import kotlin.time.toDuration

var responseText by mutableStateOf("")
var config by mutableStateOf(Config.DEFAULT)
var configMenuVisible by mutableStateOf(false)
var lastTransactionId by mutableStateOf("")
var lastTransactionDatetime by mutableStateOf("")
var lastResponseTransactionId by mutableStateOf("")
var selectedMode by mutableStateOf<Mode?>(null)

enum class Mode {
    PAYMENT_APPLICATION,
    RESTAURANT,
    RETAIL,
}

const val MODE_BUNDLE_KEY = "mode"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideNavBar()
        config = restoreConfig()
        configMenuVisible = !config.isValid()
        selectedMode = savedInstanceState?.getString(MODE_BUNDLE_KEY)?.let { Mode.valueOf(it) }

        setContent {
            PaxTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                    onClick = ::hideNavBar,
                ) {
                    if (config.responseScreenEnabled && responseText.isNotBlank()) {
                        ResponseScreen(response = responseText) {
                            responseText = ""
                        }
                    } else if (configMenuVisible) {
                        ConfigScreen(config, { configMenuVisible = false }) {
                            config = it
                            saveConfig(it)
                        }
                    } else {
                        when (selectedMode) {
                            Mode.PAYMENT_APPLICATION ->
                                PaymentModeScreen(
                                    modifier = Modifier.fillMaxSize(),
                                    handleAdminAction = ::handleAdminAction,
                                    handleAction = ::handleAction,
                                    onModalClose = ::hideNavBar,
                                )

                            Mode.RESTAURANT ->
                                RestaurantScreen(onBack = ::handleModeBack) {
                                    sendMessageIntent(payment(it))
                                }
                            Mode.RETAIL ->
                                RetailerScreen(onBack = ::handleModeBack) {
                                    sendMessageIntent(payment(it))
                                }
                            null ->
                                ModeSelectorScreen(modifier = Modifier.fillMaxHeight(.93f), modeSelector = ::handleModeSelected)
                        }
                    }
                }
            }
        }
    }

    private fun handleModeBack() {
        selectedMode = null
        hideNavBar()
    }

    private fun handleModeSelected(mode: Mode) {
        handleAction(Action.LOGIN)
        selectedMode = mode
    }

    override fun onSaveInstanceState(outState: Bundle) {
        selectedMode?.let { outState.putString(MODE_BUNDLE_KEY, it.name) }
        super.onSaveInstanceState(outState)
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
            AdminAction.BROADCAST_CONFIG -> {
                throw UnsupportedOperationException()
            }

            AdminAction.MODE_SELECTOR -> {
                selectedMode = null
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
            Action.PURCHASE -> sendMessageIntent(payment(amount = BigDecimal.TEN))
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
            intent.setAction("com.swedbankpay.payment.intent.TERMINAL_NEXO_MESSAGE")
            intent.putExtra(Intent.EXTRA_TEXT, data)
            sendBroadcast(intent)
        }
    }

    private fun sendAdminIntent(adminAction: AdminAction) {
        hideNavBar()
        Intent().also { intent ->
            intent.setAction("com.swedbankpay.payment.intent.TERMINAL_ADMIN")
            intent.putExtra(Intent.EXTRA_TEXT, adminAction.name)
            sendBroadcast(intent)
        }
    }
}
