package com.sun.kmpstartertemplaterefined.feature_lessons_data.remote

import com.sun.kmpstartertemplaterefined.feature_lessons_data.remote.dto.LessonDetailResponseDto
import com.sun.kmpstartertemplaterefined.feature_lessons_data.remote.dto.LessonsResponseDto

interface LessonsRemoteDataSource {
    suspend fun getLessons(
        type: String = "video",
        page: Int = 1,
        limit: Int = 20,
    ): LessonsResponseDto

    suspend fun getLessonDetail(lessonId: String): LessonDetailResponseDto
}