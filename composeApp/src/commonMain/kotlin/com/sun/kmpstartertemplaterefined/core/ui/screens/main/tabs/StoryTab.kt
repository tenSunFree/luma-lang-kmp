import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

private data class StoryUi(
    val title: String,
    val imageUrl: String,
)

private val fakeStoryList = listOf(
    StoryUi(
        title = "父與子",
        imageUrl = "https://picsum.photos/seed/story_1/900/520",
    ),
    StoryUi(
        title = "狗和販狗商人",
        imageUrl = "https://picsum.photos/seed/story_2/900/520",
    ),
    StoryUi(
        title = "北風與太陽",
        imageUrl = "https://picsum.photos/seed/story_3/900/520",
    ),
    StoryUi(
        title = "龜兔賽跑",
        imageUrl = "https://picsum.photos/seed/story_4/900/520",
    ),
)

@Composable
fun StoryTab() {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),
    ) {
        TabSearchBar()
        Spacer(modifier = Modifier.height(20.dp))
        fakeStoryList.forEach { item ->
            StoryCard(item = item)
            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun StoryCard(item: StoryUi) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier.fillMaxWidth().height(210.dp).clip(RoundedCornerShape(4.dp))
                .background(Color(0xFFF2F2F2)),
        ) {
            CoilImage(
                modifier = Modifier.fillMaxSize(),
                url = item.imageUrl,
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = item.title,
            color = TextDark,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
        )
    }
}