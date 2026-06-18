package com.sun.kmpstartertemplaterefined.core.ui.screens.main.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sun.kmpstartertemplaterefined.core.ui.screens.main.components.MainTopBar

private val PageBg = Color(0xFFF4F4F4)
private val TextDark = Color(0xFF333333)
private val TextGray = Color(0xFF666666)

private data class SeriousLearningItem(
    val title: String,
    val description: String,
)

private val seriousLearningItems = listOf(
    SeriousLearningItem(
        title = "聽力訓練",
        description = "即時英文新聞與娛樂影音節目，搭配生活實用例句原音朗讀，幫你成為聽力高手",
    ),
    SeriousLearningItem(
        title = "會話練習",
        description = "多國籍師資真人視訊會話互動，給你最實用的生活商務情境，訓練跨文化口語力",
    ),
    SeriousLearningItem(
        title = "書寫講堂",
        description = "專業師資真人教學與文案解析，幫你掌握文法點與構句實力，提升寫作應用技巧",
    ),
    SeriousLearningItem(
        title = "精讀課程",
        description = "各等級時事文章以及主題專欄、配合內文導讀講解與小驗收，累積閱讀與字彙量",
    ),
)

@Composable
fun SeriousLearningScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBg),
    ) {
        MainTopBar()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            seriousLearningItems.forEach { item ->
                SeriousLearningCard(item = item)
            }
        }
    }
}

@Composable
private fun SeriousLearningCard(item: SeriousLearningItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
        ),
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 24.dp,
                vertical = 18.dp,
            ),
        ) {
            Text(
                text = item.title,
                color = TextDark,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = item.description,
                color = TextGray,
                fontSize = 14.sp,
                lineHeight = 26.sp,
                modifier = Modifier.padding(top = 10.dp),
            )
        }
    }
}