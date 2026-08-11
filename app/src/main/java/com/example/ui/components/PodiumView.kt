package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.model.DriverTiming
import com.example.ui.theme.*

@Composable
fun PodiumView(
    topThreeDrivers: List<DriverTiming>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(F1Surface)
            .border(1.dp, Color(0xFF2E3245), RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        // Header Title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = F1YellowCaution,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "PIT WALL PODIUM LEADERS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = F1TextPrimary,
                    letterSpacing = 0.5.sp
                )
            }

            Text(
                text = "P1 • P2 • P3",
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                color = F1Cyan
            )
        }

        // 3-Podium Driver Cards Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            topThreeDrivers.take(3).forEachIndexed { index, driver ->
                val (podiumRank, rankColor, rankBg) = when (index) {
                    0 -> Triple("P1 LEADER", F1YellowCaution, Color(0xFF332B00))
                    1 -> Triple("P2 +${driver.gap}", F1TextPrimary, Color(0xFF262938))
                    else -> Triple("P3 +${driver.gap}", F1OrangeTyre, Color(0xFF2D2010))
                }

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .testTag("podium_card_p${index + 1}"),
                    color = F1TerminalDark,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, driver.teamColor.copy(alpha = 0.8f))
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        // Left Team Vertical Accent Stripe
                        Box(
                            modifier = Modifier
                                .width(5.dp)
                                .fillMaxHeight()
                                .align(Alignment.CenterStart)
                                .background(driver.teamColor)
                        )

                        Column(
                            modifier = Modifier
                                .padding(start = 10.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                        ) {
                            // Rank Badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(rankBg)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = podiumRank,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = rankColor
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            // Car Number & Driver Code
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "#${driver.carNumber}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = driver.teamColor
                                )
                                Text(
                                    text = driver.driverCode,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color.White
                                )
                            }

                            Text(
                                text = driver.driverName,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                color = F1TextSecondary,
                                maxLines = 1
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Team & Tyre compound badge
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = driver.teamName.take(12),
                                    fontSize = 8.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = F1TextSecondary
                                )

                                // Tyre compound indicator ring
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(driver.tyre.color)
                                        .padding(1.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = driver.tyre.code,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.Black
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
