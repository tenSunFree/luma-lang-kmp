package com.sun.kmpstartertemplaterefined.feature_lessons_data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LessonDetailResponseDto(
    val status: Boolean = false,
    val message: String = "",
    val data: LessonDetailDataDto = LessonDetailDataDto(),
)

@Serializable
data class LessonDetailDataDto(
    val content: LessonDto = LessonDto(),
    val playback: PlaybackDto = PlaybackDto(),
    val captionsVersion: Int = 1,
    val captions: List<CaptionDto> = emptyList(),
    val vocabularyItems: List<VocabularyItemDto> = emptyList(),
)

@Serializable
data class PlaybackDto(
    val videoProvider: String = "",
    val youtubeVideoId: String = "",
    val videoUrl: String = "",
    val hlsUrl: String = "",
    val durationMs: Long = 0L,
    val startAtMs: Long = 0L,
    val allowSeek: Boolean = true,
    val allowPlaybackSpeed: Boolean = true,
)

@Serializable
data class CaptionDto(
    val id: String = "",
    val sortOrder: Int = 0,
    val startMs: Long = 0L,
    val endMs: Long = 0L,
    val textEn: String = "",
    val textZhTw: String = "",
)

@Serializable
data class VocabularyItemDto(
    val id: String = "",
    val captionId: String = "",
    val startMs: Long = 0L,
    val endMs: Long = 0L,
    val phrase: String = "",
    val definitionEn: String = "",
    val definitionZhTw: String = "",
    val noteZhTw: String = "",
    val level: String = "",
    val examples: List<VocabularyExampleDto> = emptyList(),
)

@Serializable
data class VocabularyExampleDto(
    val en: String = "",
    val zhTw: String = "",
)