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
import androidx.compose.material.icons.filled.RssFeed
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
import com.example.model.NewsArticle
import com.example.model.NewsCategory
import com.example.ui.theme.*

@Composable
fun NewsHub(
    articles: List<NewsArticle>,
    selectedFilter: NewsCategory,
    onFilterSelected: (NewsCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedArticleForModal by remember { mutableStateOf<NewsArticle?>(null) }

    val filteredArticles = remember(articles, selectedFilter) {
        if (selectedFilter == NewsCategory.ALL) articles
        else articles.filter { it.category == selectedFilter }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(F1Surface)
            .border(1.dp, Color(0xFF2E3245), RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        // Header & Filter Buttons Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.RssFeed,
                    contentDescription = null,
                    tint = F1NeonRed,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "REDRACE NEWS HUB // LIVE RSS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = F1TextPrimary,
                    letterSpacing = 0.5.sp
                )
            }

            // Filter Buttons
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                NewsCategory.entries.forEach { category ->
                    val isSelected = selectedFilter == category
                    Surface(
                        modifier = Modifier
                            .testTag("news_filter_${category.name.lowercase()}")
                            .clickable { onFilterSelected(category) },
                        color = if (isSelected) F1NeonRed else F1TerminalDark,
                        shape = RoundedCornerShape(4.dp),
                        border = BorderStroke(1.dp, if (isSelected) F1NeonRed else Color(0xFF2D3244))
                    ) {
                        Text(
                            text = category.name,
                            fontSize = 9.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontFamily = FontFamily.Monospace,
                            color = if (isSelected) Color.White else F1TextSecondary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // RSS News Article List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(filteredArticles, key = { it.id }) { article ->
                val categoryColor = when (article.category) {
                    NewsCategory.URGENT -> F1NeonRed
                    NewsCategory.ANALYSIS -> F1Cyan
                    NewsCategory.TECHNICAL -> F1YellowCaution
                    NewsCategory.ALL -> F1TextSecondary
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("news_article_${article.id}")
                        .clickable { selectedArticleForModal = article },
                    color = F1TerminalDark,
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, Color(0xFF2B2F42))
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(categoryColor.copy(alpha = 0.2f))
                                        .border(1.dp, categoryColor, RoundedCornerShape(3.dp))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = article.category.name,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        color = categoryColor
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = article.source,
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = F1TextSecondary
                                )
                            }

                            Text(
                                text = article.timeAgo,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                color = F1TextSecondary
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = article.title,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = Color.White,
                            maxLines = 1
                        )

                        Text(
                            text = article.summary,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            color = F1TextSecondary,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }

    // Modal Dialog for Article Reading
    selectedArticleForModal?.let { article ->
        AlertDialog(
            onDismissRequest = { selectedArticleForModal = null },
            confirmButton = {
                TextButton(onClick = { selectedArticleForModal = null }) {
                    Text("CLOSE", color = F1Cyan, fontFamily = FontFamily.Monospace)
                }
            },
            title = {
                Text(
                    text = article.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "${article.source} • ${article.timeAgo} • By ${article.author}",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        color = F1Cyan
                    )
                    Text(
                        text = article.summary,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        color = F1TextPrimary
                    )
                }
            },
            containerColor = F1Surface,
            titleContentColor = Color.White
        )
    }
}
