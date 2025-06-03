package ai.face.liva.sdk.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable


private val DarkColorPalette by lazy {

    darkColorScheme(
        primary = appStyleColor,
        onPrimary = appLightColor,
        secondary = appLightColor,
        background = appBackgroundColor,
        error = errorColor,
        surface = appBackgroundColor,
        onSecondary = appSecondaryVariant,
        onBackground = appLightColor,
        onSurface = appLightColor
    )
}

private val LightColorPalette by lazy { DarkColorPalette }

@Composable
fun ScyllaAITheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colors,
        content = content,
        shapes = Shapes(
            inputBorderShape,
            buttonShape
        )
    )
}