package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = GramSaffronLight,
    onPrimary = Color.Black,
    primaryContainer = GramSaffronDark,
    onPrimaryContainer = Color(0xFFFFDBCF),
    secondary = GramNavyLight,
    onSecondary = Color.White,
    secondaryContainer = GramNavy,
    onSecondaryContainer = Color(0xFFD6E4FF),
    tertiary = GramGreenLight,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = DarkTextSecondary,
    outline = Color(0xFF4A4E59)
)

private val LightColorScheme = lightColorScheme(
    primary = GramSaffron,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFECE3),
    onPrimaryContainer = GramSaffronDark,
    secondary = GramNavy,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDCE9FF),
    onSecondaryContainer = GramNavyDark,
    tertiary = GramGreen,
    onTertiary = Color.White,
    background = CreamBackground,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = BorderSubtle
)

@Composable
fun GrampanchayatTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
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

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    GrampanchayatTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}
