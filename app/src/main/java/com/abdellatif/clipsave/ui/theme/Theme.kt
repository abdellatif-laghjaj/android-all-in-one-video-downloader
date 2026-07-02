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

private val Brand = Color(0xFF3D5AFE)
private val BrandDark = Color(0xFF8C9EFF)

private val LightColors = lightColorScheme(
    primary = Brand,
    secondary = Color(0xFF5C6BC0),
    tertiary = Color(0xFF00ACC1)
)

private val DarkColors = darkColorScheme(
    primary = BrandDark,
    secondary = Color(0xFF7986CB),
    tertiary = Color(0xFF4DD0E1)
)

@Composable
fun ClipSaveTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean = true,
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
