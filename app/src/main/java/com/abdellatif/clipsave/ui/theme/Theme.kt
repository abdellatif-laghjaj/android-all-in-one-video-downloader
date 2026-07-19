package com.abdellatif.clipsave.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * "Ink & paper" minimal design language: warm neutral surfaces, hairline outlines,
 * a single amber brand accent, and ink-colored primary actions. Deliberately flat —
 * no tonal elevation, no dynamic color.
 */
private val Amber = Color(0xFFFFB703)
private val AmberDeep = Color(0xFFEBA400)
private val OnAmber = Color(0xFF201500)

// Light: warm paper
private val PaperLight = Color(0xFFFAF9F7)
private val InkLight = Color(0xFF191817)
private val MutedLight = Color(0xFF8A867E)
private val TileLight = Color(0xFFF1EFEA)
private val HairlineLight = Color(0xFFE7E4DE)

// Dark: soft charcoal
private val PaperDark = Color(0xFF111013)
private val InkDark = Color(0xFFF0EEEA)
private val MutedDark = Color(0xFF908D87)
private val TileDark = Color(0xFF1B1A1E)
private val HairlineDark = Color(0xFF27262B)

private val LightColors = lightColorScheme(
    primary = AmberDeep,
    onPrimary = OnAmber,
    primaryContainer = Amber.copy(alpha = 0.16f),
    onPrimaryContainer = Color(0xFF6B4A00),
    secondary = InkLight,
    onSecondary = PaperLight,
    secondaryContainer = TileLight,
    onSecondaryContainer = InkLight,
    tertiary = AmberDeep,
    onTertiary = OnAmber,
    background = PaperLight,
    onBackground = InkLight,
    surface = PaperLight,
    onSurface = InkLight,
    surfaceVariant = TileLight,
    onSurfaceVariant = MutedLight,
    surfaceContainer = TileLight,
    surfaceContainerLow = Color(0xFFF5F3EF),
    surfaceContainerHigh = Color(0xFFEDEBE5),
    outline = HairlineLight,
    outlineVariant = Color(0xFFEFEDE8),
    error = Color(0xFFC0453D),
    onError = Color.White
)

private val DarkColors = darkColorScheme(
    primary = Amber,
    onPrimary = OnAmber,
    primaryContainer = Amber.copy(alpha = 0.14f),
    onPrimaryContainer = Color(0xFFFFD262),
    secondary = InkDark,
    onSecondary = PaperDark,
    secondaryContainer = TileDark,
    onSecondaryContainer = InkDark,
    tertiary = Amber,
    onTertiary = OnAmber,
    background = PaperDark,
    onBackground = InkDark,
    surface = PaperDark,
    onSurface = InkDark,
    surfaceVariant = TileDark,
    onSurfaceVariant = MutedDark,
    surfaceContainer = TileDark,
    surfaceContainerLow = Color(0xFF17161A),
    surfaceContainerHigh = Color(0xFF211F25),
    outline = HairlineDark,
    outlineVariant = Color(0xFF1F1E23),
    error = Color(0xFFE5726A),
    onError = Color(0xFF230404)
)

private val ClipSaveShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun ClipSaveTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = ClipSaveTypography,
        shapes = ClipSaveShapes,
        content = content
    )
}
