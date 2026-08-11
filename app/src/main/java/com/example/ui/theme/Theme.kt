package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = F1NeonRed,
    onPrimary = Color.White,
    primaryContainer = F1BorderRed,
    secondary = F1Cyan,
    onSecondary = Color.Black,
    tertiary = F1YellowCaution,
    background = F1Background,
    onBackground = F1TextPrimary,
    surface = F1Surface,
    onSurface = F1TextPrimary,
    surfaceVariant = F1SurfaceVariant,
    onSurfaceVariant = F1TextSecondary,
    outline = F1BorderRed
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
