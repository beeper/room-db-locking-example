package com.beeper.escaperoom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import com.beeper.escaperoom.db.ExampleViewModel
import com.beeper.escaperoom.ui.theme.EscapeRoomTheme

class MainActivity : ComponentActivity() {

    private val viewModel: ExampleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EscapeRoomTheme {
                val isTransactionOpen = viewModel.isTransactionOpen.collectAsState()
                val flowableState = viewModel.flowableState.collectAsState()
                val suspendableState = viewModel.suspendableState.collectAsState()
                val flowableExecutionTime = viewModel.flowableExecutionTime.collectAsState()
                val suspendableExecutionTime = viewModel.suspendableExecutionTime.collectAsState()

                Screen(
                    isTransactionOpen.value,
                    flowableState.value.first,
                    flowableState.value.second,
                    suspendableState.value.first,
                    suspendableState.value.second,
                    flowableExecutionTime.value,
                    suspendableExecutionTime.value,
                    viewModel::setTransactionState,
                    viewModel::requestFlowableValue,
                    viewModel::requestSuspendableValue
                )
            }
        }
    }
}