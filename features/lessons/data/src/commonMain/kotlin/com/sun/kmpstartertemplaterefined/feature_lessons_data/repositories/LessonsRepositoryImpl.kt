package com.sun.kmpstartertemplaterefined.feature_lessons_data.repositories

import com.sun.kmpstartertemplaterefined.feature_lessons_data.mappers.toDomain
import com.sun.kmpstartertemplaterefined.feature_lessons_data.remote.LessonsRemoteDataSource
import com.sun.kmpstartertemplaterefined.feature_lessons_domain.models.Lesson
import com.sun.kmpstartertemplaterefined.feature_lessons_domain.models.LessonDetail
import com.sun.kmpstartertemplaterefined.feature_lessons_domain.repositories.LessonsRepository
import io.ktor.client.plugins.ClientRequestException

class LessonsRepositoryImpl(
    private val remoteDataSource: LessonsRemoteDataSource,
) : LessonsRepository {

    override suspend fun getLessons(
        type: String,
        page: Int,
        limit: Int,
    ): Result<List<Lesson>> {
        return try {
            val response = remoteDataSource.getLessons(type = type, page = page, limit = limit)
            if (!response.status) {
                return Result.failure(Exception(response.message.ifBlank { "取得影片列表失敗" }))
            }
            val lessons = response.data.contents.map { it.toDomain() }
            Result.success(lessons)
        } catch (e: ClientRequestException) {
            val message = when (e.response.status.value) {
                401 -> "尚未登入或登入已過期"
                403 -> "沒有權限取得影片列表"
                404 -> "找不到影片列表 API"
                else -> "取得影片列表失敗（${e.response.status.value}），請稍後再試"
            }
            Result.failure(Exception(message))
        } catch (_: Exception) {
            Result.failure(Exception("網路連線異常，請確認網路後再試"))
        }
    }

    override suspend fun getLessonDetail(lessonId: String): Result<LessonDetail> {
        return try {
            val response = remoteDataSource.getLessonDetail(lessonId)
            if (!response.status) {
                return Result.failure(Exception(response.message.ifBlank { "取得影片詳情失敗" }))
            }
            Result.success(response.data.toDomain())
        } catch (e: ClientRequestException) {
            val message = when (e.response.status.value) {
                401 -> "尚未登入或登入已過期"
                403 -> "沒有權限取得影片詳情"
                404 -> "找不到此影片"
                else -> "取得影片詳情失敗（${e.response.status.value}），請稍後再試"
            }
            Result.failure(Exception(message))
        } catch (_: Exception) {
            Result.failure(Exception("網路連線異常，請確認網路後再試"))
        }
    }
}