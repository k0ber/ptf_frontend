package org.patifiner.client.base

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.util.AttributeKey

val AuthRequiredKey = AttributeKey<Boolean>("AuthRequired")

fun HttpRequestBuilder.authRequired(required: Boolean) {
    attributes.put(AuthRequiredKey, required)
}

suspend inline fun <reified T : Any, reified R : Any> HttpClient.post(
    urlString: String,
    body: T,
    authRequired: Boolean = true // По умолчанию True
): R = post(urlString) {
    authRequired(authRequired)
    setBody(body)
}.body()

suspend inline fun <reified R : Any> HttpClient.get(
    urlString: String,
    authRequired: Boolean = true
): R = get(urlString) {
    authRequired(authRequired)
}.body()

suspend inline fun <reified R : Any> HttpClient.getSearch(
    urlString: String,
    query: String,
    authRequired: Boolean = true
): R = get(urlString) {
    authRequired(authRequired)
    url { parameters.append("q", query) }
}.body()

suspend inline fun <reified T : Any> HttpClient.deleteWithCount(
    urlString: String,
    body: T,
    authRequired: Boolean = true
): Int = delete(urlString) {
    authRequired(authRequired)
    setBody(body)
}.body<Map<String, Int>>()["removed"] ?: 0