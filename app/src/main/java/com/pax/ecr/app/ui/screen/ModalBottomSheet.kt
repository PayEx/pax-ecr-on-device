package com.pax.ecr.app.ui.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheet(
    onClose: () -> Unit,
    content: @Composable () -> Unit,
) {
    val modalBottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = modalBottomSheetState,
    ) {
        content()
    }
}
