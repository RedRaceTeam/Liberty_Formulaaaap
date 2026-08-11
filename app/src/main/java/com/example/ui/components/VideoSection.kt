package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CameraSource
import com.example.model.TelemetryGauge
import com.example.ui.theme.*

@Composable
fun VideoSection(
    selectedCamera: CameraSource,
    onCameraSelected: (CameraSource) -> Unit,
    telemetry: TelemetryGauge,
    modifier: Modifier = Modifier
) {
    var showTelemetryOverlay by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(F1Surface)
            .border(1.dp, F1BorderRed, RoundedCornerShape(10.dp))
            .padding(8.dp)
    ) {
        // Camera Selector Tabs Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CameraSource.entries.forEach { camera ->
                    val isSelected = selectedCamera == camera
                    Surface(
                        modifier = Modifier
                            .testTag("cam_${camera.name.lowercase()}")
                            .clickable { onCameraSelected(camera) },
                        color = if (isSelected) F1NeonRed else F1TerminalDark,
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, if (isSelected) F1NeonRed else Color(0xFF2D3142))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (camera == CameraSource.DATA_MATRIX) Icons.Default.Analytics else Icons.Default.Videocam,
                                contentDescription = null,
                                tint = if (isSelected) Color.White else F1TextSecondary,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = camera.displayName,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontFamily = FontFamily.Monospace,
                                color = if (isSelected) Color.White else F1TextSecondary
                            )
                        }
                    }
                }
            }

            // HUD Toggle Button
            IconButton(
                onClick = { showTelemetryOverlay = !showTelemetryOverlay },
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = if (showTelemetryOverlay) Icons.Default.Layers else Icons.Default.LayersClear,
                    contentDescription = "Toggle Telemetry Overlay",
                    tint = if (showTelemetryOverlay) F1Cyan else F1TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Central Video Player View Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(F1TerminalDark)
                .border(1.dp, Color(0xFF2A2D3E), RoundedCornerShape(8.dp))
        ) {
            // Simulated High-Tech F1 Broadcast Canvas with dynamic speed vectors
            F1CameraCanvasStream(camera = selectedCamera, telemetry = telemetry)

            // Live Stream HUD Badge Overlay
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Black.copy(alpha = 0.75f))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(F1NeonRed)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${selectedCamera.badge} • LIVE STREAM • 8.4 Mbps",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Telemetry Overlay HUD (Speedometer, RPM bar, Gears, Brake/Throttle)
            Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                AnimatedVisibility(
                    visible = showTelemetryOverlay,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.88f))
                                )
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                    // Left telemetry: Gear, Speed & RPM bar
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Gear box
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(F1NeonRed.copy(alpha = 0.25f))
                                .border(1.dp, F1NeonRed, RoundedCornerShape(6.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "GEAR",
                                    fontSize = 8.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = F1TextSecondary
                                )
                                Text(
                                    text = "${telemetry.gear}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color.White
                                )
                            }
                        }

                        // Speed readout & RPM
                        Column {
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "${telemetry.speedKmh}",
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = F1Cyan
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "KM/H",
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = F1TextSecondary,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            }

                            // RPM indicator bar
                            val rpmPct = (telemetry.rpm.toFloat() / telemetry.maxRpm).coerceIn(0f, 1f)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                (1..12).forEach { step ->
                                    val active = (step.toFloat() / 12f) <= rpmPct
                                    val barColor = when {
                                        step > 10 -> F1PurpleSector
                                        step > 8 -> F1NeonRed
                                        else -> F1GreenOnline
                                    }
                                    Box(
                                        modifier = Modifier
                                            .width(8.dp)
                                            .height(6.dp)
                                            .clip(RoundedCornerShape(1.dp))
                                            .background(if (active) barColor else Color(0xFF222634))
                                    )
                                }
                                Text(
                                    text = " ${telemetry.rpm} RPM",
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = F1TextSecondary
                                )
                            }
                        }
                    }

                    // Right telemetry: DRS state, Throttle/Brake gauges
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // DRS Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (telemetry.drsOpen) F1GreenOnline else F1SurfaceVariant)
                                .border(1.dp, if (telemetry.drsOpen) F1GreenOnline else Color(0xFF33384B), RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = if (telemetry.drsOpen) "DRS OPEN" else "DRS AVAIL",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = if (telemetry.drsOpen) Color.Black else F1TextSecondary
                            )
                        }

                        // Throttle / Brake vertical meters
                        Column(horizontalAlignment = Alignment.End) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text("THR", fontSize = 8.sp, color = F1TextSecondary, fontFamily = FontFamily.Monospace)
                                Box(
                                    modifier = Modifier
                                        .width(50.dp)
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(Color(0xFF222634))
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .fillMaxWidth(telemetry.throttlePct)
                                            .background(F1GreenOnline)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text("BRK", fontSize = 8.sp, color = F1TextSecondary, fontFamily = FontFamily.Monospace)
                                Box(
                                    modifier = Modifier
                                        .width(50.dp)
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(Color(0xFF222634))
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .fillMaxWidth(telemetry.brakePct)
                                            .background(F1NeonRed)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
}

@Composable
private fun F1CameraCanvasStream(
    camera: CameraSource,
    telemetry: TelemetryGauge
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Dark track background grid
        val gridStep = 40f
        var x = 0f
        while (x < w) {
            drawLine(
                color = Color(0xFF1B1E2B),
                start = Offset(x, 0f),
                end = Offset(x, h),
                strokeWidth = 1f
            )
            x += gridStep
        }
        var y = 0f
        while (y < h) {
            drawLine(
                color = Color(0xFF1B1E2B),
                start = Offset(0f, y),
                end = Offset(w, y),
                strokeWidth = 1f
            )
            y += gridStep
        }

        // Draw track layout circuit silhouette
        val trackPath = Path().apply {
            moveTo(w * 0.15f, h * 0.7f)
            lineTo(w * 0.45f, h * 0.7f)
            cubicTo(w * 0.55f, h * 0.7f, w * 0.6f, h * 0.85f, w * 0.75f, h * 0.85f)
            cubicTo(w * 0.9f, h * 0.85f, w * 0.88f, h * 0.3f, w * 0.7f, h * 0.25f)
            lineTo(w * 0.35f, h * 0.25f)
            cubicTo(w * 0.2f, h * 0.25f, w * 0.05f, h * 0.45f, w * 0.15f, h * 0.7f)
        }

        drawPath(
            path = trackPath,
            color = Color(0xFF2A2E42),
            style = Stroke(width = 12f)
        )
        drawPath(
            path = trackPath,
            color = F1NeonRed.copy(alpha = 0.6f),
            style = Stroke(width = 2f)
        )

        // Draw animated telemetry car position dot on track circuit
        val carPosOffset = Offset(
            w * (0.15f + telemetry.lapProgress * 0.65f),
            h * (0.25f + Math.sin(telemetry.lapProgress * Math.PI * 2).toFloat() * 0.3f)
        )

        drawCircle(
            color = F1NeonRed,
            radius = 10f,
            center = carPosOffset
        )
        drawCircle(
            color = Color.White,
            radius = 5f,
            center = carPosOffset
        )

        // Draw HUD targeting crosshairs & corner telemetry markers
        drawCircle(
            color = F1Cyan.copy(alpha = 0.3f),
            radius = 35f,
            center = Offset(w * 0.5f, h * 0.5f),
            style = Stroke(width = 1.5f)
        )
        drawLine(
            color = F1Cyan.copy(alpha = 0.4f),
            start = Offset(w * 0.5f - 50f, h * 0.5f),
            end = Offset(w * 0.5f + 50f, h * 0.5f),
            strokeWidth = 1f
        )
        drawLine(
            color = F1Cyan.copy(alpha = 0.4f),
            start = Offset(w * 0.5f, h * 0.5f - 50f),
            end = Offset(w * 0.5f, h * 0.5f + 50f),
            strokeWidth = 1f
        )
    }
}
