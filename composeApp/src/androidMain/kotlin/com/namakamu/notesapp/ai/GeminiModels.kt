package com.namakamu.notesapp.ai

import kotlinx.serialization.Serializable

@Serializable
data class GeminiRequest(val contents: List<Content>)

@Serializable
data class Content(val parts: List<Part>, val role: String = "user")

@Serializable
data class Part(val text: String)

@Serializable
data class GeminiResponse(
    val candidates: List<Candidate>? = null,
    val error: GeminiError? = null
)

@Serializable
data class Candidate(
    val content: Content? = null
)

@Serializable
data class GeminiError(
    val code: Int? = null,
    val message: String? = null,
    val status: String? = null
)

data class SummaryResult(val summary: String, val isSuccess: Boolean, val errorMessage: String? = null)