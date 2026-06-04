package com.sun.kmpstartertemplaterefined.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.sun.kmpstartertemplaterefined.starter_resources.Res
import com.sun.kmpstartertemplaterefined.starter_resources.manrope_bold
import com.sun.kmpstartertemplaterefined.starter_resources.manrope_extra_bold
import com.sun.kmpstartertemplaterefined.starter_resources.manrope_light
import com.sun.kmpstartertemplaterefined.starter_resources.manrope_medium
import com.sun.kmpstartertemplaterefined.starter_resources.manrope_regular
import com.sun.kmpstartertemplaterefined.starter_resources.manrope_semibold
import org.jetbrains.compose.resources.Font

@Composable
fun getAppFontFamily() = FontFamily(
    Font(
        resource = Res.font.manrope_light,
        weight = FontWeight.Thin
    ),
    Font(
        resource = Res.font.manrope_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resource = Res.font.manrope_medium,
        weight = FontWeight.Medium
    ),
    Font(
        resource = Res.font.manrope_semibold,
        weight = FontWeight.SemiBold
    ),
    Font(
        resource = Res.font.manrope_bold,
        weight = FontWeight.Bold
    ),
    Font(
        resource = Res.font.manrope_extra_bold,
        weight = FontWeight.ExtraBold
    )
)

// Default Material 3 typography values
val baselineTypography = Typography()