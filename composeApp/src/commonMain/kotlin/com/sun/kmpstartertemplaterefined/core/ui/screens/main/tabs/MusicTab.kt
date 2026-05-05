package com.sun.kmpstartertemplaterefined.core.ui.screens.main.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sun.kmpstartertemplaterefined.core.ui.screens.main.tabs.shared.TabSearchBar
import com.sun.kmpstartertemplaterefined.ui_components.image.CoilImage

private val TextDark = Color(0xFF4A4A4A)

data class MusicUi(
    val title: String,
    val artist: String,
    val views: Int,
    val imageUrl: String,
    val tag: String = "中英字幕",
)

val fakeMusicList = listOf(
    MusicUi("Underwater Boi", "Turnstile", 291, "https://picsum.photos/seed/music_1/900/520"),
    MusicUi("vampire", "Olivia Rodrigo", 542, "https://picsum.photos/seed/music_2/900/520"),
    MusicUi("Espresso", "Sabrina Carpenter", 3572, "https://picsum.photos/seed/music_3/900/520"),
    MusicUi("Luther", "Kendrick Lamar ft. SZA", 987, "https://picsum.photos/seed/music_4/900/520"),
)

@Composable
fun MusicTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),
    ) {
        TabSearchBar()
        Spacer(modifier = Modifier.height(20.dp))
        fakeMusicList.forEach { item ->
            MusicCard(item = item)
            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
fun MusicCard(item: MusicUi) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.Black),
            contentAlignment = Alignment.Center,
        ) {
            CoilImage(
                modifier = Modifier.fillMaxWidth().height(160.dp),
                url = item.imageUrl,
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = item.tag,
                color = Color(0xFF888888),
                fontSize = 12.sp,
                modifier = Modifier
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xFFF0F0F0))
                    .padding(horizontal = 6.dp, vertical = 2.dp),
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "👁 ${item.views}次", color = Color(0xFF999999), fontSize = 13.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = item.title,
            color = TextDark,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            lineHeight = 28.sp,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = item.artist, color = Color(0xFF999999), fontSize = 16.sp)
    }
}