package com.sun.kmpstartertemplaterefined.core.ui.screens.main.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Pink = Color(0xFFFF3F68)
private val TextDark = Color(0xFF4A4A4A)
private val BorderGray = Color(0xFFE5E5E5)
private val TabUnselected = Color(0xFFC5C5C5)

@Composable
fun EasyLearningScreen() {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("直播室", "FunTV", "影片", "音樂", "童話", "專欄", "大補帖")
    Column(modifier = Modifier.fillMaxSize()) {
        TopBar()
        TabRow(
            tabs = tabs,
            selectedIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it },
        )
        when (selectedTabIndex) {
            0 -> LiveStreamingTab()
            1 -> FunTvTab()
            2 -> VideoTab()
            3 -> MusicTab()
            4 -> StoryTab()
            5 -> ColumnTab()
            6 -> StudyPackTab()
        }
    }
}

@Composable
private fun TopBar() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "LumaLang",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextDark,
            modifier = Modifier.weight(1f),
        )
        Icon(
            imageVector = Icons.Filled.GridView,
            contentDescription = "課表",
            tint = Color(0xFF555555),
            modifier = Modifier.size(26.dp),
        )
        Spacer(modifier = Modifier.width(20.dp))
        Box(
            modifier = Modifier.size(32.dp).clip(CircleShape).background(Color(0xFFFFA2B9)),
            contentAlignment = Alignment.Center,
        ) {
            Text("🤖", fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.width(20.dp))
        Box(
            modifier = Modifier.size(32.dp).clip(CircleShape).background(Color(0xFF1E9BEF)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Headphones,
                contentDescription = "耳機",
                tint = Color.White,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
private fun TabRow(tabs: List<String>, selectedIndex: Int, onTabSelected: (Int) -> Unit) {
    val scrollState = rememberScrollState()
    Column {
        Row(modifier = Modifier.fillMaxWidth().horizontalScroll(scrollState)) {
            tabs.forEachIndexed { index, title ->
                val isSelected = selectedIndex == index
                Column(
                    modifier = Modifier
                        .clickable { onTabSelected(index) }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Pink else TabUnselected,
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .height(3.dp)
                            .width(52.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(if (isSelected) Pink else Color.Transparent),
                    )
                }
            }
        }
        HorizontalDivider(color = BorderGray, thickness = 0.8.dp)
    }
}