package com.sun.kmpstartertemplaterefined.core.ui.screens.login.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sun.kmpstartertemplaterefined.feature_resources.Res
import com.sun.kmpstartertemplaterefined.feature_resources.login_apple
import com.sun.kmpstartertemplaterefined.feature_resources.login_fb
import com.sun.kmpstartertemplaterefined.feature_resources.login_google
import com.sun.kmpstartertemplaterefined.feature_resources.login_line
import org.jetbrains.compose.resources.painterResource

// The `label` parameter allows both LoginCard and RegisterCard to share this component.
@Composable
internal fun ThirdPartySection(
    label: String = "或由第三方登入",
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE6E6E6))
        Text(
            text = "  $label  ",
            color = Color(0xFFAAAAAA),
            fontSize = 13.sp,
        )
        HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE6E6E6))
    }
    Spacer(modifier = Modifier.height(18.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SocialButton(
            modifier = Modifier.weight(1f),
            bgColor = Color.White,
            borderColor = Color(0xFFDDDDDD),
        ) {
            Image(
                painter = painterResource(Res.drawable.login_google),
                contentDescription = "Google",
                modifier = Modifier.fillMaxSize(),
            )
        }
        SocialButton(
            modifier = Modifier.weight(1f),
            bgColor = Color.White,
            borderColor = Color(0xFFDDDDDD),
        ) {
            Image(
                painter = painterResource(Res.drawable.login_fb),
                contentDescription = "FB",
                modifier = Modifier.fillMaxSize(),
            )
        }
        SocialButton(
            modifier = Modifier.weight(1f),
            bgColor = Color.White,
            borderColor = Color(0xFFDDDDDD),
        ) {
            Image(
                painter = painterResource(Res.drawable.login_line),
                contentDescription = "Line",
                modifier = Modifier.fillMaxSize(),
            )
        }
        SocialButton(
            modifier = Modifier.weight(1f),
            bgColor = Color.White,
            borderColor = Color(0xFFDDDDDD),
        ) {
            Image(
                painter = painterResource(Res.drawable.login_apple),
                contentDescription = "Apple",
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
internal fun SocialButton(
    modifier: Modifier = Modifier,
    bgColor: Color,
    borderColor: Color = Color.Transparent,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier.aspectRatio(1f),
        shape = RoundedCornerShape(8.dp),
        color = bgColor,
        shadowElevation = if (borderColor == Color.Transparent) 2.dp else 0.dp,
    ) {
        Box(
            modifier = Modifier.border(
                width = if (borderColor == Color.Transparent) 0.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp),
            ),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }
    }
}