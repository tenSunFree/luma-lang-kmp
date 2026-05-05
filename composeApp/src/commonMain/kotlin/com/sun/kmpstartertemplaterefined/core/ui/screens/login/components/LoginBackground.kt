package com.sun.kmpstartertemplaterefined.core.ui.screens.login.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import com.sun.kmpstartertemplaterefined.feature_resources.Res
import com.sun.kmpstartertemplaterefined.feature_resources.login_bg
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun LumaLangLoginBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(Res.drawable.login_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xCC006492),
                            Color(0x44006492),
                            Color(0x00006492),
                        )
                    )
                )
        )
    }
}