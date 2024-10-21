package com.airwallex.risk

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedOutputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.ConnectException
import java.net.HttpRetryException
import java.net.HttpURLConnection
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.URL
import java.net.UnknownHostException

internal interface IApiService {
    suspend fun postEvents(events: List<Event>)
}

internal class ServerException : IOException("Server-side error")

internal class ApiService(
    private val riskContext: IRiskContext
) : IApiService {
    companion object {
        private const val serverErrorCode = 500
    }

    override suspend fun postEvents(events: List<Event>) {
        val url = riskContext.environment.host + riskContext.sessionId.toString()
        withContext(Dispatchers.IO) {
            val connection =
                runCatching { URL(url).openConnection() as HttpURLConnection }.getOrElse {
                    Log.d(Constants.logTag, "HttpURLConnection cannot be opened: ${it.message}")
                    return@withContext
                }

            val body =
                runCatching { Json.encodeToString(events).toByteArray() }.getOrElse {
                    Log.d(Constants.logTag, "Json encoding error: ${it.message}")
                    return@withContext
                }

            try {
                connection.apply {
                    requestMethod = "POST"
                    doOutput = true
                    setFixedLengthStreamingMode(body.size)
                    setRequestProperty("Content-Type", "application/json")
                }

                val outputStream = BufferedOutputStream(connection.outputStream)
                outputStream.write(body)
                outputStream.flush()

                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                reader.forEachLine { response.append(it) }
                Log.d(Constants.logTag, "Response body: $response")

                if (connection.responseCode >= serverErrorCode) {
                    throw ServerException()
                }
                if (events.any { event -> event.event.type == Constants.installationEventName }) {
                    riskContext.hasSentInstallationEvent = true
                }
            } catch (e: IOException) {
                Log.d(Constants.logTag, "HttpURLConnection output error: ${e.message}")
                when (e) {
                    is ConnectException, is UnknownHostException, is HttpRetryException,
                    is NoRouteToHostException, is SocketTimeoutException, is ServerException -> {
                        throw e
                    }
                }
            } finally {
                connection.disconnect()
            }
        }
    }
}