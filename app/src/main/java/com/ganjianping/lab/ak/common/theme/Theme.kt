package com.ganjianping.lab.ak.common.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = SlateWhite,
    onPrimary = SlateBlack,
    primaryContainer = SlateDarkPrimaryContainer,
    onPrimaryContainer = SlateWhite,
    inversePrimary = SlateBlack,
    secondary = SlateDarkSecondary,
    onSecondary = SlateBlack,
    secondaryContainer = SlateDarkSecondaryContainer,
    onSecondaryContainer = SlateWhite,
    tertiary = SlateDarkTertiary,
    onTertiary = SlateBlack,
    tertiaryContainer = SlateDarkTertiaryContainer,
    onTertiaryContainer = SlateWhite,
    background = SlateDarkBackground,
    onBackground = SlateDarkOnSurface,
    surface = SlateDarkSurface,
    onSurface = SlateDarkOnSurface,
    surfaceVariant = SlateDarkSurfaceContainerHigh,
    onSurfaceVariant = SlateDarkOnSurfaceVariant,
    surfaceTint = SlateWhite,
    inverseSurface = SlateNeutral,
    inverseOnSurface = SlateLightOnSurface,
    error = SlateDarkError,
    onError = SlateDarkOnError,
    errorContainer = SlateDarkErrorContainer,
    onErrorContainer = SlateDarkOnErrorContainer,
    outline = SlateDarkOutline,
    outlineVariant = SlateDarkOutlineVariant,
    scrim = SlateBlack,
    surfaceBright = SlateDarkSurfaceContainerHighest,
    surfaceDim = SlateDarkSurfaceDim,
    surfaceContainer = SlateDarkSurfaceContainer,
    surfaceContainerHigh = SlateDarkSurfaceContainerHigh,
    surfaceContainerHighest = SlateDarkSurfaceContainerHighest,
    surfaceContainerLow = SlateDarkSurfaceContainerLow,
    surfaceContainerLowest = SlateBlack
)

private val LightColorScheme = lightColorScheme(
    primary = SlateBlack,
    onPrimary = SlateWhite,
    primaryContainer = SlateNeutral,
    onPrimaryContainer = SlateBlack,
    inversePrimary = SlateWhite,
    secondary = SlateLightSecondary,
    onSecondary = SlateWhite,
    secondaryContainer = SlateNeutral,
    onSecondaryContainer = SlateLightOnSecondaryContainer,
    tertiary = SlateLightTertiary,
    onTertiary = SlateWhite,
    tertiaryContainer = SlateNeutral,
    onTertiaryContainer = SlateBlack,
    background = SlateWarmCanvas,
    onBackground = SlateLightOnSurface,
    surface = SlateLightSurface,
    onSurface = SlateLightOnSurface,
    surfaceVariant = SlateNeutral,
    onSurfaceVariant = SlateLightOnSurfaceVariant,
    surfaceTint = SlateBlack,
    inverseSurface = SlateLightOnSurface,
    inverseOnSurface = SlateWhite,
    error = SlateError,
    onError = SlateOnError,
    errorContainer = SlateErrorContainer,
    onErrorContainer = SlateOnErrorContainer,
    outline = SlateLightOutline,
    outlineVariant = SlateLightOutlineVariant,
    scrim = SlateBlack,
    surfaceBright = SlateWhite,
    surfaceDim = SlateLightSurfaceDim,
    surfaceContainer = SlateLightSurfaceContainer,
    surfaceContainerHigh = SlateLightSurfaceContainerHigh,
    surfaceContainerHighest = SlateLightSurfaceContainerHighest,
    surfaceContainerLow = SlateLightSurfaceContainerLow,
    surfaceContainerLowest = SlateWhite
)

@Composable
fun GJPLabTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // The brand palette is the default. Callers may opt into wallpaper-derived colors.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
