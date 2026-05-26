package com.sun.kmpstartertemplaterefined.feature_lessons_data.mappers

import com.sun.kmpstartertemplaterefined.feature_lessons_data.remote.dto.*
import com.sun.kmpstartertemplaterefined.feature_lessons_domain.models.*

fun LessonDetailDataDto.toDomain(): LessonDetail = LessonDetail(
    lesson = content.toDomain(),
    playback = playback.toDomain(),
    captionsVersion = captionsVersion,
    captions = captions.map { it.toDomain() },
    vocabularyItems = vocabularyItems.map { it.toDomain() },
)

fun PlaybackDto.toDomain(): Playback = Playback(
    videoProvider = videoProvider,
    youtubeVideoId = youtubeVideoId,
    videoUrl = videoUrl,
    hlsUrl = hlsUrl,
    durationMs = durationMs,
    startAtMs = startAtMs,
    allowSeek = allowSeek,
    allowPlaybackSpeed = allowPlaybackSpeed,
)

fun CaptionDto.toDomain(): Caption = Caption(
    id = id,
    sortOrder = sortOrder,
    startMs = startMs,
    endMs = endMs,
    textEn = textEn,
    textZhTw = textZhTw,
)

fun VocabularyItemDto.toDomain(): VocabularyItem = VocabularyItem(
    id = id,
    captionId = captionId,
    startMs = startMs,
    endMs = endMs,
    phrase = phrase,
    definitionEn = definitionEn,
    definitionZhTw = definitionZhTw,
    noteZhTw = noteZhTw,
    level = level,
    examples = examples.map { it.toDomain() },
)

fun VocabularyExampleDto.toDomain(): VocabularyExample = VocabularyExample(
    en = en,
    zhTw = zhTw,
)