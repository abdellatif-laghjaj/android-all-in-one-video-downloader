package com.abdellatif.clipsave.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val Primary = Color(0xFFFFB703)
private val Secondary = Color(0xFFFB8500)

private val LightColors = lightColorScheme(
    primary = Primary,
    onPrimary = Color.Black,
    primaryContainer = Primary,
    onPrimaryContainer = Color.Black,
    secondary = Secondary,
    onSecondary = Color.Black,
    secondaryContainer = Primary,
    onSecondaryContainer = Color.Black
)

private val DarkColors = darkColorScheme(
    primary = Primary,
    onPrimary = Color.Black,
    primaryContainer = Primary,
    onPrimaryContainer = Color.Black,
    secondary = Secondary,
    onSecondary = Color.Black,
    secondaryContainer = Primary,
    onSecondaryContainer = Color.Black
)

@Composable
fun ClipSaveTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)

        darkTheme -> DarkColors
        else -> LightColors
    }
    MaterialTheme(colorScheme = colorScheme, typography = ClipSaveTypography, content = content)
}
