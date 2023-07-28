package com.airwallex.risk

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable

@Serializable
internal data class EventResponse(
    val message: String
)

internal interface IApiService {
    suspend fun postEvents(events: List<Event>): EventResponse
}

internal class ApiService(
    private val riskContext: RiskContext,
    private val client: HttpClient = HttpClient(CIO) {
        expectSuccess = true

        install(UserAgent) {
            agent = riskContext.userAgent
        }
        install(ContentNegotiation) {
            json()
        }
        HttpResponseValidator { }
        defaultRequest {

        }
    }
) : IApiService {

    override suspend fun postEvents(events: List<Event>): EventResponse {
        val url = riskContext.environment.host + riskContext.sessionId.toString()

        return client.post(url) {
            contentType(ContentType.Application.Json)
            setBody(events)
        }.body()
    }
}



