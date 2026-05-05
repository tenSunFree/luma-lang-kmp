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

data class VideoUi(
    val title: String,
    val subtitle: String,
    val views: Int,
    val imageUrl: String,
    val tag: String = "中英字幕",
)

val fakeVideos = listOf(
    VideoUi(
        title = "LumaLang Cinephile 電影迷 | Paterson 派特森",
        subtitle = "LumaLang | CINEPHILE  看電影學英文",
        views = 975,
        imageUrl = "https://picsum.photos/seed/video_1/900/520",
    ),
    VideoUi(
        title = "「不要為流量犧牲全部價值」想當YouTuber前，你必須知道的現實",
        subtitle = "LumaLang Chat",
        views = 576,
        imageUrl = "https://picsum.photos/seed/video_2/900/520",
    ),
)

@Composable
fun VideoTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),
    ) {
        TabSearchBar()
        Spacer(modifier = Modifier.height(20.dp))
        fakeVideos.forEach { video ->
            VideoCard(video = video)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun VideoCard(video: VideoUi) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFFEFEFEF)),
        ) {
            CoilImage(
                modifier = Modifier.fillMaxSize(),
                url = video.imageUrl,
                contentDescription = video.title,
                contentScale = ContentScale.Crop,
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.55f))
                    .padding(vertical = 10.dp, horizontal = 12.dp),
            ) {
                Text(
                    text = video.subtitle,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = video.tag,
                color = Color(0xFF888888),
                fontSize = 12.sp,
                modifier = Modifier
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xFFF0F0F0))
                    .padding(horizontal = 6.dp, vertical = 2.dp),
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "👁 ${video.views}次", color = Color(0xFF999999), fontSize = 13.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = video.title,
            color = TextDark,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            lineHeight = 28.sp,
        )
    }
}