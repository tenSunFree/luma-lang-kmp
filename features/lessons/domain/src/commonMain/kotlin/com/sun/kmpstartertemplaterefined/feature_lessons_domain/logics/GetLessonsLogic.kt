package com.sun.kmpstartertemplaterefined.feature_lessons_domain.logics

import com.sun.kmpstartertemplaterefined.feature_lessons_domain.models.Lesson
import com.sun.kmpstartertemplaterefined.feature_lessons_domain.repositories.LessonsRepository

class GetLessonsLogic(
    private val repository: LessonsRepository,
) {
    suspend operator fun invoke(
        type: String = "video",
        page: Int = 1,
        limit: Int = 20,
    ): Result<List<Lesson>> = repository.getLessons(type = type, page = page, limit = limit)
}