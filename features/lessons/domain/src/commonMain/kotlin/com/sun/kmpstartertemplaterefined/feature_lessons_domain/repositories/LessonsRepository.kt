package com.sun.kmpstartertemplaterefined.feature_lessons_domain.repositories

import com.sun.kmpstartertemplaterefined.feature_lessons_domain.models.Lesson
import com.sun.kmpstartertemplaterefined.feature_lessons_domain.models.LessonDetail

interface LessonsRepository {
    suspend fun getLessons(
        type: String = "video",
        page: Int = 1,
        limit: Int = 20,
    ): Result<List<Lesson>>

    suspend fun getLessonDetail(lessonId: String): Result<LessonDetail>
}