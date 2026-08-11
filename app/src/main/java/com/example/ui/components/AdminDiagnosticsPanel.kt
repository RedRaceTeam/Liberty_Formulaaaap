package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ServiceBadge
import com.example.ui.theme.*

@Composable
fun AdminDiagnosticsPanel(
    services: List<ServiceBadge>,
    onToggleService: (String) -> Unit,
    onTriggerSafetyCar: () -> Unit,
    onTriggerYellowFlag: () -> Unit,
    onTriggerGreenFlag: () -> Unit,
    isSimulationRunning: Boolean,
    onToggleSimulation: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(F1Surface)
            .border(1.dp, F1BorderRed, RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        // Admin Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AdminPanelSettings,
                    contentDescription = null,
                    tint = F1NeonRed,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "PIT WALL ADMIN // SYSTEM DIAGNOSTICS & CONTROLS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = F1TextPrimary,
                    letterSpacing = 0.5.sp
                )
            }

            Text(
                text = "DIAGNOSTICS ACTIVE",
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace,
                color = F1GreenOnline
            )
        }

        // 1. System Status Badges Row (Railway, OpenF1 API, RSS Feed, Stream Proxy)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            services.forEach { service ->
                val statusColor = if (service.status) F1GreenOnline else F1NeonRed
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .testTag("admin_service_${service.name.lowercase().replace(' ', '_')}")
                        .clickable { onToggleService(service.name) },
                    color = F1TerminalDark,
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, statusColor.copy(alpha = 0.8f))
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(statusColor)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (service.status) "ONLINE" else "OFFLINE",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = statusColor
                            )
                        }
                        Text(
                            text = service.name,
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace,
                            color = F1TextPrimary,
                            maxLines = 1
                        )
                        Text(
                            text = "${service.latencyMs}ms",
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace,
                            color = F1TextSecondary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 2. Race Simulation Injector Controls (Safety Car, Yellow Flag, Green Flag)
        Column {
            Text(
                text = "RACE EVENT SIMULATOR INJECTOR",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = F1TextSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Safety Car Trigger Button
                Button(
                    onClick = onTriggerSafetyCar,
                    colors = ButtonDefaults.buttonColors(containerColor = F1YellowCaution),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("trigger_safety_car")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = Color.Black, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("SAFETY CAR", fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = Color.Black)
                    }
                }

                // Yellow Flag Trigger Button
                Button(
                    onClick = onTriggerYellowFlag,
                    colors = ButtonDefaults.buttonColors(containerColor = F1OrangeTyre),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("trigger_yellow_flag")
                ) {
                    Text("YELLOW S2", fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = Color.Black)
                }

                // Green Flag Trigger Button
                Button(
                    onClick = onTriggerGreenFlag,
                    colors = ButtonDefaults.buttonColors(containerColor = F1GreenOnline),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("trigger_green_flag")
                ) {
                    Text("GREEN FLAG", fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = Color.Black)
                }

                // Pause/Resume Simulation Ticks Button
                OutlinedButton(
                    onClick = onToggleSimulation,
                    border = BorderStroke(1.dp, F1Cyan),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.testTag("toggle_simulation_button")
                ) {
                    Icon(
                        imageVector = if (isSimulationRunning) Icons.Default.PlayArrow else Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = F1Cyan,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isSimulationRunning) "TICK ON" else "PAUSED",
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        color = F1Cyan
                    )
                }
            }
        }
    }
}
