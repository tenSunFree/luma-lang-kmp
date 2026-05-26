package com.sun.kmpstartertemplaterefined.feature_lessons_data.mappers

import com.sun.kmpstartertemplaterefined.feature_lessons_data.remote.dto.LessonDto
import com.sun.kmpstartertemplaterefined.feature_lessons_domain.models.Lesson

fun LessonDto.toDomain(): Lesson = Lesson(
    id = id,
    type = type,
    category = category,
    coverUrl = coverUrl,
    createdAt = createdAt,
    description = description,
    durationMs = durationMs,
    isFree = isFree,
    level = level,
    subtitle = subtitle,
    tags = tags,
    title = title,
    updatedAt = updatedAt,
    viewCount = viewCount,
)