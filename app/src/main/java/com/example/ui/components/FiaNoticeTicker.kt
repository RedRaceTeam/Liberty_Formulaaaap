package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Terminal
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
import com.example.model.FiaCategory
import com.example.model.FiaNotice
import com.example.ui.theme.*

@Composable
fun FiaNoticeTicker(
    notices: List<FiaNotice>,
    onAcknowledgeNotice: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(F1TerminalDark)
            .border(1.dp, F1BorderRed, RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        // Ticker Title Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Terminal,
                    contentDescription = null,
                    tint = F1YellowCaution,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "FIA RACE CONTROL OFFICIAL TICKER",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = F1TextPrimary,
                    letterSpacing = 0.5.sp
                )
            }

            Text(
                text = "${notices.size} NOTICES",
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                color = F1TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Notice Terminal Feed
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(115.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(notices, key = { it.id }) { notice ->
                val categoryColor = when (notice.category) {
                    FiaCategory.INVESTIGATION -> F1YellowCaution
                    FiaCategory.PENALTY -> F1NeonRed
                    FiaCategory.SAFETY_CAR -> F1NeonRed
                    FiaCategory.FLAG -> F1OrangeTyre
                    FiaCategory.DRS -> F1GreenOnline
                    FiaCategory.INFO -> F1Cyan
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("fia_notice_${notice.id}"),
                    color = F1Surface,
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(
                        1.dp,
                        if (notice.acknowledged) Color(0xFF262A38) else categoryColor.copy(alpha = 0.8f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = notice.timestamp,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                color = F1TextSecondary
                            )

                            // Category Tag
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(categoryColor.copy(alpha = 0.2f))
                                    .border(1.dp, categoryColor, RoundedCornerShape(3.dp))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = notice.category.name,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = categoryColor
                                )
                            }

                            Text(
                                text = notice.message,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                color = if (notice.acknowledged) F1TextSecondary else F1TextPrimary,
                                maxLines = 1
                            )
                        }

                        if (!notice.acknowledged) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Acknowledge Notice",
                                tint = F1GreenOnline,
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable { onAcknowledgeNotice(notice.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}
