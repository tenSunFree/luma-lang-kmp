package com.sun.kmpstartertemplaterefined.core.ui.screens.main.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Pink = Color(0xFFFF3F68)

private data class BottomNavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

@Composable
fun MainBottomBar(selectedIndex: Int, onTabSelected: (Int) -> Unit) {
    val items = listOf(
        BottomNavItem("輕鬆學", Icons.Filled.Headphones, Icons.Outlined.Headphones),
        BottomNavItem("認真學", Icons.Filled.MenuBook, Icons.Outlined.MenuBook),
        BottomNavItem("複習", Icons.Filled.LocalLibrary, Icons.Outlined.LocalLibrary),
        BottomNavItem("精讀收錄", Icons.Filled.Bookmark, Icons.Outlined.Bookmark),
        BottomNavItem("更多", Icons.Filled.Person, Icons.Outlined.Person),
    )
    NavigationBar(containerColor = Color.White, tonalElevation = 0.dp) {
        items.forEachIndexed { index, item ->
            val isSelected = selectedIndex == index
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(index) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Pink,
                    selectedTextColor = Pink,
                    unselectedIconColor = Color(0xFF888888),
                    unselectedTextColor = Color(0xFF888888),
                    indicatorColor = Color.Transparent,
                ),
            )
        }
    }
}