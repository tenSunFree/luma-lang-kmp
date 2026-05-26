package com.sun.kmpstartertemplaterefined.feature_lessons_data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LessonsResponseDto(
    val status: Boolean = false,
    val message: String = "",
    val data: LessonsDataDto = LessonsDataDto(),
)

@Serializable
data class LessonsDataDto(
    val contents: List<LessonDto> = emptyList(),
    val page: Int = 1,
    val limit: Int = 20,
    val total: Int = 0,
)

@Serializable
data class LessonDto(
    val id: String = "",
    val type: String = "",
    val category: String = "",
    val coverUrl: String = "",
    val createdAt: String = "",
    val description: String = "",
    val durationMs: Long = 0L,
    val isFree: Boolean = false,
    val level: String = "",
    val subtitle: String = "",
    val tags: List<String> = emptyList(),
    val title: String = "",
    val updatedAt: String = "",
    val viewCount: Int = 0,
)