package com.abdellatif.clipsave.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.abdellatif.clipsave.R

private val Figtree = FontFamily(
    Font(R.font.figtree_light, FontWeight.Light),
    Font(R.font.figtree_regular, FontWeight.Normal),
    Font(R.font.figtree_medium, FontWeight.Medium),
    Font(R.font.figtree_semibold, FontWeight.SemiBold),
    Font(R.font.figtree_bold, FontWeight.Bold),
    Font(R.font.figtree_extrabold, FontWeight.ExtraBold),
    Font(R.font.figtree_black, FontWeight.Black)
)

private fun TextStyle.figtree() = copy(fontFamily = Figtree)
private val DefaultTypography = Typography()

val ClipSaveTypography = DefaultTypography.copy(
    displayLarge = DefaultTypography.displayLarge.figtree(),
    displayMedium = DefaultTypography.displayMedium.figtree(),
    displaySmall = DefaultTypography.displaySmall.figtree(),
    headlineLarge = DefaultTypography.headlineLarge.figtree(),
    headlineMedium = DefaultTypography.headlineMedium.figtree(),
    headlineSmall = DefaultTypography.headlineSmall.figtree(),
    titleLarge = DefaultTypography.titleLarge.figtree(),
    titleMedium = DefaultTypography.titleMedium.figtree(),
    titleSmall = DefaultTypography.titleSmall.figtree(),
    bodyLarge = DefaultTypography.bodyLarge.figtree(),
    bodyMedium = DefaultTypography.bodyMedium.figtree(),
    bodySmall = DefaultTypography.bodySmall.figtree(),
    labelLarge = DefaultTypography.labelLarge.figtree(),
    labelMedium = DefaultTypography.labelMedium.figtree(),
    labelSmall = DefaultTypography.labelSmall.figtree()
)
