package com.pax.ecr.app.ui.screen

import AdminMenu
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.pax.ecr.app.Action
import com.pax.ecr.app.AdminAction
import com.pax.ecr.app.ui.screen.common.Footer
import com.pax.ecr.app.ui.screen.common.ModalBottomSheet
import com.pax.ecr.app.ui.screen.payment.TransactionSelectScreen

@Composable
fun PaymentModeScreen(
    handleAdminAction: (AdminAction) -> Unit,
    onModalClose: () -> Unit,
    modifier: Modifier = Modifier,
    handleAction: (Action) -> Unit,
) {
    var isOpen by remember {
        mutableStateOf(false)
    }
    Column(modifier, verticalArrangement = Arrangement.SpaceBetween) {
        TransactionSelectScreen(modifier = Modifier.fillMaxHeight(.9f), actionHandler = handleAction)
        Footer(isOpen) { isOpen = !isOpen }
    }
    if (isOpen) {
        ModalBottomSheet(onClose = {
            onModalClose()
            isOpen = !isOpen
        }) {
            AdminMenu { action ->
                isOpen = false
                handleAdminAction(action)
            }
        }
    }
}
