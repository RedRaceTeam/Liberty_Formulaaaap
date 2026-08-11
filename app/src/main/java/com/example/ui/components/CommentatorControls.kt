package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AudioSource
import com.example.ui.theme.*

@Composable
fun CommentatorControls(
    selectedAudio: AudioSource,
    onAudioSourceSelected: (AudioSource) -> Unit,
    audioSyncDelay: Float,
    onAdjustAudioSync: (Float) -> Unit,
    isMicActive: Boolean,
    onToggleMic: () -> Unit,
    commentatorNote: String,
    onNoteChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(F1Surface)
            .border(1.dp, Color(0xFF2C3042), RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        // Collapsible Header Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(if (isMicActive) F1NeonRed else Color(0xFF33384B)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.GraphicEq,
                        contentDescription = "Mic EQ",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }

                Text(
                    text = "COMMENTATOR AUDIO ENGINE & SYNC CONTROLS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = F1TextPrimary,
                    letterSpacing = 0.5.sp
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Live Audio Sync Badge
                Surface(
                    color = F1TerminalDark,
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, F1Cyan.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "SYNC: ${if (audioSyncDelay >= 0) "+${String.format("%.2f", audioSyncDelay)}" else String.format("%.2f", audioSyncDelay)}s",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = F1Cyan,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                IconButton(
                    onClick = { isExpanded = !isExpanded },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Toggle Commentator Panel",
                        tint = F1TextSecondary
                    )
                }
            }
        }

        // Collapsible Content
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Divider(color = Color(0xFF262938))

                // 1. Audio Source Switcher
                Column {
                    Text(
                        text = "AUDIO FEED SELECTOR",
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
                        AudioSource.entries.forEach { source ->
                            val isSelected = selectedAudio == source
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("audio_${source.name.lowercase()}")
                                    .clickable { onAudioSourceSelected(source) },
                                color = if (isSelected) F1NeonRed.copy(alpha = 0.2f) else F1TerminalDark,
                                shape = RoundedCornerShape(6.dp),
                                border = BorderStroke(1.dp, if (isSelected) F1NeonRed else Color(0xFF2E3345))
                            ) {
                                Column(
                                    modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = source.displayName,
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        fontFamily = FontFamily.Monospace,
                                        color = if (isSelected) F1NeonRed else F1TextPrimary
                                    )
                                    Text(
                                        text = source.frequency,
                                        fontSize = 8.sp,
                                        fontFamily = FontFamily.Monospace,
                                        color = F1TextSecondary
                                    )
                                }
                            }
                        }
                    }
                }

                // 2. Audio Sync Delay Adjustment Controls (±0.5s & Slider)
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "AUDIO / VIDEO LATENCY SYNC DELAY",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = F1TextSecondary
                        )
                        Text(
                            text = "OFF-SET: ${if (audioSyncDelay >= 0) "+${String.format("%.2f", audioSyncDelay)}" else String.format("%.2f", audioSyncDelay)} sec",
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = F1Cyan
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // -0.5s button
                        Button(
                            onClick = { onAdjustAudioSync(-0.50f) },
                            colors = ButtonDefaults.buttonColors(containerColor = F1TerminalDark),
                            border = BorderStroke(1.dp, Color(0xFF35394C)),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.testTag("sync_minus_500ms")
                        ) {
                            Text("-0.5s", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = F1Cyan)
                        }

                        // -0.1s button
                        Button(
                            onClick = { onAdjustAudioSync(-0.10f) },
                            colors = ButtonDefaults.buttonColors(containerColor = F1TerminalDark),
                            border = BorderStroke(1.dp, Color(0xFF35394C)),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.testTag("sync_minus_100ms")
                        ) {
                            Text("-0.1s", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = F1Cyan)
                        }

                        // Slider bar
                        Slider(
                            value = audioSyncDelay,
                            onValueChange = { newValue -> onAdjustAudioSync(newValue - audioSyncDelay) },
                            valueRange = -2.0f..2.0f,
                            colors = SliderDefaults.colors(
                                thumbColor = F1NeonRed,
                                activeTrackColor = F1NeonRed,
                                inactiveTrackColor = Color(0xFF2A2D3E)
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        // +0.1s button
                        Button(
                            onClick = { onAdjustAudioSync(0.10f) },
                            colors = ButtonDefaults.buttonColors(containerColor = F1TerminalDark),
                            border = BorderStroke(1.dp, Color(0xFF35394C)),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.testTag("sync_plus_100ms")
                        ) {
                            Text("+0.1s", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = F1Cyan)
                        }

                        // +0.5s button
                        Button(
                            onClick = { onAdjustAudioSync(0.50f) },
                            colors = ButtonDefaults.buttonColors(containerColor = F1TerminalDark),
                            border = BorderStroke(1.dp, Color(0xFF35394C)),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.testTag("sync_plus_500ms")
                        ) {
                            Text("+0.5s", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = F1Cyan)
                        }
                    }
                }

                // 3. Mic Cue Light & Commentator Quick Notes
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // PTT Broadcast On-Air Mic Button
                    Button(
                        onClick = onToggleMic,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isMicActive) F1NeonRed else F1TerminalDark
                        ),
                        border = BorderStroke(1.dp, if (isMicActive) F1NeonRed else Color(0xFF383C52)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .testTag("toggle_mic_button")
                            .height(42.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isMicActive) Icons.Default.Mic else Icons.Default.MicOff,
                                contentDescription = "Mic State",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isMicActive) "ON AIR // MIC LIVE" else "MIC MUTED",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = Color.White
                            )
                        }
                    }

                    // Quick Notepad TextField
                    OutlinedTextField(
                        value = commentatorNote,
                        onValueChange = onNoteChange,
                        placeholder = { Text("Commentator telemetry notes...", fontSize = 10.sp, color = F1TextSecondary) },
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                            .testTag("commentator_notes_input"),
                        textStyle = LocalTextStyle.current.copy(fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = F1TextPrimary),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = F1TerminalDark,
                            unfocusedContainerColor = F1TerminalDark,
                            focusedBorderColor = F1Cyan,
                            unfocusedBorderColor = Color(0xFF2B2F40)
                        ),
                        singleLine = true
                    )
                }
            }
        }
    }
}
