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

private data class StudyPackUi(
    val title: String,
    val overlayTitle: String,
    val overlaySubtitle: String,
    val imageUrl: String,
)

private val fakeStudyPackList = listOf(
    StudyPackUi(
        title = "長得相似卻意義迥異？3組最易混淆的英文單字精準辨析",
        overlayTitle = "長得相似卻意義迥異？",
        overlaySubtitle = "3組最易混淆的英文單字精準辨析",
        imageUrl = "https://picsum.photos/seed/studypack_1/900/520",
    ),
    StudyPackUi(
        title = "AI 看似取代了許多工作，但它是真的想讓人失業？",
        overlayTitle = "AI 看似取代了許多工作，",
        overlaySubtitle = "但它真的想讓人失業？",
        imageUrl = "https://picsum.photos/seed/studypack_2/900/520",
    ),
    StudyPackUi(
        title = "英文時態完全攻略：從現在式到完成進行式一次搞懂",
        overlayTitle = "英文時態完全攻略",
        overlaySubtitle = "從現在式到完成進行式一次搞懂",
        imageUrl = "https://picsum.photos/seed/studypack_3/900/520",
    ),
    StudyPackUi(
        title = "開口說英文不再卡關！5個打破英文口說恐懼的關鍵技巧",
        overlayTitle = "開口說英文不再卡關！",
        overlaySubtitle = "5個打破英文口說恐懼的關鍵技巧",
        imageUrl = "https://picsum.photos/seed/studypack_4/900/520",
    ),
)

@Composable
fun StudyPackTab() {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),
    ) {
        TabSearchBar()
        Spacer(modifier = Modifier.height(20.dp))
        fakeStudyPackList.forEach { item ->
            StudyPackCard(item = item)
            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun StudyPackCard(item: StudyPackUi) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier.fillMaxWidth().height(210.dp).clip(RoundedCornerShape(4.dp))
                .background(Color(0xFFEFEFEF)),
        ) {
            CoilImage(
                modifier = Modifier.fillMaxSize(),
                url = item.imageUrl,
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
            )
            // Text Overlay (bottom left corner, semi-transparent black background)
            Box(
                modifier = Modifier.align(Alignment.BottomStart).fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.42f))
                    .padding(horizontal = 14.dp, vertical = 14.dp),
            ) {
                Column {
                    Text(
                        text = item.overlayTitle,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 24.sp,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.overlaySubtitle,
                        color = Color(0xFFFFE082), // Yellow subtitle
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 20.sp,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = item.title,
            color = TextDark,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            lineHeight = 30.sp,
        )
    }
}