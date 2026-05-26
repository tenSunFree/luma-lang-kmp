package com.sun.kmpstartertemplaterefined.feature_lessons_domain.models

data class Lesson(
    val id: String,
    val type: String,
    val category: String,
    val coverUrl: String,
    val createdAt: String,
    val description: String,
    val durationMs: Long,
    val isFree: Boolean,
    val level: String,
    val subtitle: String,
    val tags: List<String>,
    val title: String,
    val updatedAt: String,
    val viewCount: Int,
)