package com.sun.kmpstartertemplaterefined.feature_lessons_data.remote

import com.sun.kmpstartertemplaterefined.feature_lessons_data.remote.dto.LessonDetailResponseDto
import com.sun.kmpstartertemplaterefined.feature_lessons_data.remote.dto.LessonsResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class LessonsRemoteDataSourceImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String,
) : LessonsRemoteDataSource {

    // GET /api/v1/contents?type=video&page=1&limit=20
    override suspend fun getLessons(
        type: String,
        page: Int,
        limit: Int,
    ): LessonsResponseDto =
        httpClient.get("$baseUrl/contents") {
            parameter("type", type)
            parameter("page", page)
            parameter("limit", limit)
        }.body()

    // GET /api/v1/contents/{lessonId}
    override suspend fun getLessonDetail(lessonId: String): LessonDetailResponseDto =
        httpClient.get("$baseUrl/contents/$lessonId").body()
}