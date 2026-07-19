package com.abdellatif.clipsave.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
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
private val Default = Typography()

/** Figtree everywhere; headlines tightened, labels slightly tracked for a premium feel. */
val ClipSaveTypography = Default.copy(
    displayLarge = Default.displayLarge.figtree().copy(
        fontWeight = FontWeight.Bold, letterSpacing = (-1).sp
    ),
    displayMedium = Default.displayMedium.figtree().copy(
        fontWeight = FontWeight.Bold, letterSpacing = (-0.8).sp
    ),
    displaySmall = Default.displaySmall.figtree().copy(
        fontWeight = FontWeight.Bold, letterSpacing = (-0.6).sp
    ),
    headlineLarge = Default.headlineLarge.figtree().copy(
        fontWeight = FontWeight.Bold, letterSpacing = (-0.6).sp
    ),
    headlineMedium = Default.headlineMedium.figtree().copy(
        fontWeight = FontWeight.Bold, letterSpacing = (-0.5).sp
    ),
    headlineSmall = Default.headlineSmall.figtree().copy(
        fontWeight = FontWeight.Bold, letterSpacing = (-0.4).sp
    ),
    titleLarge = Default.titleLarge.figtree().copy(
        fontWeight = FontWeight.SemiBold, letterSpacing = (-0.3).sp
    ),
    titleMedium = Default.titleMedium.figtree().copy(
        fontWeight = FontWeight.SemiBold, letterSpacing = (-0.1).sp
    ),
    titleSmall = Default.titleSmall.figtree().copy(fontWeight = FontWeight.SemiBold),
    bodyLarge = Default.bodyLarge.figtree(),
    bodyMedium = Default.bodyMedium.figtree(),
    bodySmall = Default.bodySmall.figtree().copy(lineHeight = 18.sp),
    labelLarge = Default.labelLarge.figtree().copy(fontWeight = FontWeight.SemiBold),
    labelMedium = Default.labelMedium.figtree().copy(fontWeight = FontWeight.Medium),
    labelSmall = Default.labelSmall.figtree().copy(
        fontWeight = FontWeight.Medium, letterSpacing = 1.2.sp
    )
)
