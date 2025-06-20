package com.beeper.escaperoom

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beeper.escaperoom.ui.theme.EscapeRoomTheme

@Composable
fun Screen(
    isTransactionOpen: Boolean = true,
    flowableRequested: Boolean = false,
    flowableReady: Boolean = false,
    suspendableRequested: Boolean = false,
    suspendableReady: Boolean = false,
    flowableExecutionTime: Long? = null,
    suspendableExecutionTime: Long? = null,
    toggleTransactionState: (Boolean) -> Unit = {},
    requestFlowableValue: () -> Unit = {},
    requestSuspendableValue: () -> Unit = {}
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(innerPadding)
        ) {
            // Scrollable content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 140.dp), // Space for buttons at bottom
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Transaction Control Section
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(16.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Database Transaction", fontWeight = FontWeight.Bold)
                        Switch(
                            checked = isTransactionOpen,
                            onCheckedChange = toggleTransactionState
                        )
                    }
                    AnimatedVisibility(visible = isTransactionOpen) {
                        Text(
                            "Transaction is open",
                            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Flowable Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF1E3A8A).copy(alpha = 0.1f),
                                    Color(0xFF3B82F6).copy(alpha = 0.05f)
                                )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🌊 Flow-based Operations",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E3A8A),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        if (flowableRequested && !flowableReady) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp))
                        }
                    }
                    // Execution time display for flowable
                    if (flowableReady && flowableExecutionTime != null) {
                        Text(
                            text = "Finished in ${flowableExecutionTime}ms",
                            fontSize = 12.sp,
                            color = Color(0xFF1E3A8A).copy(alpha = 0.7f),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Flowable Value Requested Status
                    AnimatedVisibility(
                        visible = flowableRequested,
                        enter = fadeIn(animationSpec = tween(500)) + scaleIn(
                            animationSpec = tween(
                                500
                            )
                        )
                    ) {
                        StatusCard(
                            emoji = "📡",
                            title = "Flow Invoked",
                            subtitle = "Asking Room for 1 row of data via a Flow",
                            backgroundColor = listOf(
                                Color(0xFF2196F3),
                                Color(0xFF21CBF3)
                            ),
                            isAnimated = flowableRequested && !flowableReady
                        )
                    }

                    if (flowableRequested) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Flowable Value Ready Status
                    AnimatedVisibility(
                        visible = flowableReady,
                        enter = fadeIn(animationSpec = tween(500)) + scaleIn(
                            animationSpec = tween(
                                500
                            )
                        )
                    ) {
                        StatusCard(
                            emoji = "✅",
                            title = "Value Ready",
                            subtitle = "Data received successfully via Flow!",
                            backgroundColor = listOf(
                                Color(0xFF4CAF50),
                                Color(0xFF8BC34A)
                            ),
                            isAnimated = false
                        )
                    }

                }

                Spacer(modifier = Modifier.height(24.dp))

                // Suspendable Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF7C2D12).copy(alpha = 0.1f),
                                    Color(0xFFEA580C).copy(alpha = 0.05f)
                                )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = "⚡ Simple Suspend Function",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF7C2D12),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    // Execution time display for suspendable
                    if (suspendableReady && suspendableExecutionTime != null) {
                        Text(
                            text = "Finished in ${suspendableExecutionTime}ms",
                            fontSize = 12.sp,
                            color = Color(0xFF7C2D12).copy(alpha = 0.7f),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Suspendable Value Requested Status
                    AnimatedVisibility(
                        visible = suspendableRequested,
                        enter = fadeIn(animationSpec = tween(500)) + scaleIn(
                            animationSpec = tween(
                                500
                            )
                        )
                    ) {
                        StatusCard(
                            emoji = "⚡",
                            title = "POJO Invoked",
                            subtitle = "Asking Room for 1 row of data via suspend function",
                            backgroundColor = listOf(
                                Color(0xFFEA580C),
                                Color(0xFFF97316)
                            ),
                            isAnimated = suspendableRequested && !suspendableReady
                        )
                    }

                    if (suspendableRequested) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Suspendable Value Ready Status
                    AnimatedVisibility(
                        visible = suspendableReady,
                        enter = fadeIn(animationSpec = tween(500)) + scaleIn(
                            animationSpec = tween(
                                500
                            )
                        )
                    ) {
                        StatusCard(
                            emoji = "✅",
                            title = "Value Ready",
                            subtitle = "Data received successfully via suspend!",
                            backgroundColor = listOf(
                                Color(0xFF4CAF50),
                                Color(0xFF8BC34A)
                            ),
                            isAnimated = false
                        )
                    }

                }

                // Close the scrollable Column here
            }

            // Fixed buttons at the bottom (now in Box scope)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.0f),
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                                MaterialTheme.colorScheme.surface
                            )
                        )
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = requestFlowableValue,
                    enabled = !flowableRequested || flowableReady,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Invoke Flow Function")
                }

                Button(
                    onClick = requestSuspendableValue,
                    enabled = !suspendableRequested || suspendableReady,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Invoke Suspend Function")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScreenPreview() {
    EscapeRoomTheme {
        Screen()
    }
}
