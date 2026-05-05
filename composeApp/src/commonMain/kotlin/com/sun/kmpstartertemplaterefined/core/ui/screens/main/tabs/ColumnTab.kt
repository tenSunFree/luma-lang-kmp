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

private data class ColumnArticleUi(
    val title: String,
    val imageUrl: String,
)

private val fakeColumnList = listOf(
    ColumnArticleUi(
        title = "一級保育類猛禽 熊鷹的美麗與哀愁（下）",
        imageUrl = "https://picsum.photos/seed/column_1/900/520",
    ),
    ColumnArticleUi(
        title = "一級保育類猛禽 熊鷹的美麗與哀愁（上）",
        imageUrl = "https://picsum.photos/seed/column_2/900/520",
    ),
    ColumnArticleUi(
        title = "台灣黑熊保育現況與挑戰",
        imageUrl = "https://picsum.photos/seed/column_3/900/520",
    ),
    ColumnArticleUi(
        title = "珊瑚礁危機：氣候變遷下的海洋生態",
        imageUrl = "https://picsum.photos/seed/column_4/900/520",
    ),
)

@Composable
fun ColumnTab() {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),
    ) {
        TabSearchBar()
        Spacer(modifier = Modifier.height(20.dp))
        fakeColumnList.forEach { item ->
            ColumnArticleCard(item = item)
            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun ColumnArticleCard(item: ColumnArticleUi) {
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