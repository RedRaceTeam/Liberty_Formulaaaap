package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.model.DriverTiming
import com.example.model.SectorStatus
import com.example.ui.theme.*

@Composable
fun LiveTimingTable(
    drivers: List<DriverTiming>,
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
        // Table Title Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Leaderboard,
                    contentDescription = null,
                    tint = F1Cyan,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "LIVE TELEMETRY TIMING MATRIX",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = F1TextPrimary,
                    letterSpacing = 0.5.sp
                )
            }

            Text(
                text = "REAL-TIME OPENF1",
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace,
                color = F1GreenOnline
            )
        }

        // Table Column Headers: POS | DRIVER | GAP | TYRE | S1 | S2 | S3
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(F1TerminalDark)
                .padding(horizontal = 6.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("POS", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = F1TextSecondary, fontFamily = FontFamily.Monospace, modifier = Modifier.width(30.dp))
            Text("DRIVER", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = F1TextSecondary, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(1.3f))
            Text("GAP", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = F1TextSecondary, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(1.0f))
            Text("TYRE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = F1TextSecondary, fontFamily = FontFamily.Monospace, modifier = Modifier.width(36.dp))
            Text("S1", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = F1TextSecondary, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(0.9f))
            Text("S2", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = F1TextSecondary, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(0.9f))
            Text("S3", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = F1TextSecondary, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(0.9f))
            Text("BEST", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = F1TextSecondary, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(1.1f))
        }

        Divider(color = Color(0xFF26293A))

        // Live Timing Rows List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(drivers, key = { it.carNumber }) { driver ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("timing_row_${driver.driverCode.lowercase()}"),
                    color = if (driver.pos % 2 == 0) F1TerminalDark else F1Surface,
                    shape = RoundedCornerShape(2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 6.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // POS
                        Text(
                            text = "${driver.pos}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = if (driver.pos <= 3) F1YellowCaution else F1TextPrimary,
                            modifier = Modifier.width(30.dp)
                        )

                        // DRIVER (Team Stripe + Code)
                        Row(
                            modifier = Modifier.weight(1.3f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(3.dp)
                                    .height(14.dp)
                                    .background(driver.teamColor)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = driver.driverCode,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "#${driver.carNumber}",
                                fontSize = 8.sp,
                                fontFamily = FontFamily.Monospace,
                                color = F1TextSecondary
                            )
                        }

                        // GAP
                        Text(
                            text = driver.gap,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            color = if (driver.gap == "LEADER") F1YellowCaution else F1TextPrimary,
                            modifier = Modifier.weight(1.0f)
                        )

                        // TYRE Compound Ring Badge
                        Box(
                            modifier = Modifier.width(36.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clip(CircleShape)
                                        .background(driver.tyre.color),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = driver.tyre.code,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.Black
                                    )
                                }
                                Text(
                                    text = " ${driver.tyreAgeLaps}",
                                    fontSize = 8.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = F1TextSecondary
                                )
                            }
                        }

                        // S1
                        SectorCell(
                            time = driver.s1,
                            status = driver.s1Status,
                            modifier = Modifier.weight(0.9f)
                        )

                        // S2
                        SectorCell(
                            time = driver.s2,
                            status = driver.s2Status,
                            modifier = Modifier.weight(0.9f)
                        )

                        // S3
                        SectorCell(
                            time = driver.s3,
                            status = driver.s3Status,
                            modifier = Modifier.weight(0.9f)
                        )

                        // BEST LAP
                        Text(
                            text = driver.bestLap,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = F1TextPrimary,
                            modifier = Modifier.weight(1.1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectorCell(
    time: String,
    status: SectorStatus,
    modifier: Modifier = Modifier
) {
    val textColor = when (status) {
        SectorStatus.PURPLE -> F1PurpleSector
        SectorStatus.GREEN -> F1GreenOnline
        SectorStatus.YELLOW -> F1YellowCaution
        SectorStatus.NONE -> F1TextPrimary
    }

    Text(
        text = time,
        fontSize = 10.sp,
        fontFamily = FontFamily.Monospace,
        fontWeight = if (status == SectorStatus.PURPLE || status == SectorStatus.GREEN) FontWeight.Bold else FontWeight.Normal,
        color = textColor,
        modifier = modifier
    )
}
