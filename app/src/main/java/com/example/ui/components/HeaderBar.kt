package com.example.ui.components

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserRole
import com.example.ui.theme.*

@Composable
fun HeaderBar(
    currentRole: UserRole,
    onRoleSelected: (UserRole) -> Unit,
    grandPrixTitle: String,
    currentLap: Int,
    totalLaps: Int,
    raceCondition: String,
    trackTemp: Int,
    airTemp: Int,
    rainPct: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(F1BorderRed, Color(0xFF303444), F1BorderRed)
                ),
                shape = RoundedCornerShape(0.dp)
            ),
        color = F1Surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // App Branding & Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(F1NeonRed, Color(0xFF800000))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = "F1 Pit Wall Logo",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "LIBERTY FORMULA",
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 15.sp,
                            color = F1TextPrimary,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(F1NeonRed)
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "LIVE",
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp,
                                color = Color.White
                            )
                        }
                    }
                    Text(
                        text = "PIT WALL CONTROL // STREAMING ENGINE",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        color = F1TextSecondary
                    )
                }
            }

            // Race Status & Weather Telemetry Info
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                // Grand Prix Badge
                Surface(
                    color = F1SurfaceVariant,
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, Color(0xFF2E3244))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Flag,
                            contentDescription = null,
                            tint = F1YellowCaution,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$grandPrixTitle • LAP $currentLap/$totalLaps",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = F1TextPrimary
                        )
                    }
                }

                // Condition Badge (Green Flag / SC / Yellow)
                val conditionBg = when {
                    raceCondition.contains("SAFETY") -> F1YellowCaution
                    raceCondition.contains("YELLOW") -> F1OrangeTyre
                    else -> F1GreenOnline
                }
                Surface(
                    color = conditionBg.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, conditionBg)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(conditionBg)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = raceCondition,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Monospace,
                            color = conditionBg
                        )
                    }
                }

                // Weather Telemetry (Track/Air/Rain)
                Surface(
                    color = F1TerminalDark,
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, Color(0xFF2A2D3C))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Thermostat, contentDescription = null, tint = F1Cyan, modifier = Modifier.size(12.dp))
                            Text(" ${trackTemp}°C TRK", fontSize = 10.sp, color = F1TextSecondary, fontFamily = FontFamily.Monospace)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.WaterDrop, contentDescription = null, tint = F1Cyan, modifier = Modifier.size(12.dp))
                            Text(" ${rainPct}% RAIN", fontSize = 10.sp, color = F1TextSecondary, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }

            // Role Selector Buttons
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(F1TerminalDark)
                    .border(1.dp, Color(0xFF2A2E3D), RoundedCornerShape(8.dp))
                    .padding(3.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                UserRole.entries.forEach { role ->
                    val isSelected = currentRole == role
                    val activeBgColor by animateColorAsState(
                        if (isSelected) F1NeonRed else Color.Transparent, label = "RoleBg"
                    )
                    val activeTextColor by animateColorAsState(
                        if (isSelected) Color.White else F1TextSecondary, label = "RoleText"
                    )

                    Row(
                        modifier = Modifier
                            .testTag("role_${role.name.lowercase()}")
                            .clip(RoundedCornerShape(6.dp))
                            .background(activeBgColor)
                            .clickable { onRoleSelected(role) }
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val roleIcon = when (role) {
                            UserRole.SPECTATOR -> Icons.Default.Visibility
                            UserRole.COMMENTATOR -> Icons.Default.Mic
                            UserRole.ADMIN -> Icons.Default.AdminPanelSettings
                        }
                        Icon(
                            imageVector = roleIcon,
                            contentDescription = role.name,
                            tint = activeTextColor,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = role.name,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontFamily = FontFamily.Monospace,
                            color = activeTextColor
                        )
                    }
                }
            }
        }
    }
}
