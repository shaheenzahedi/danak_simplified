package org.aydm.danak.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.aydm.danak.service.facade.FileResponse
import org.aydm.danak.service.facade.UpdateResponse
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL

data class Update(val checksum: String, var path: String)
data class Delete(val checksum: String, var path: String)

fun main() {
    // Define the URLs and bearer token
    val url1 = "https://api.danakapp.org/api/update-assets?fromVersion=80&toVersion=81"
    val url2 = "https://ftp.danakapp.org/"
    val bearerToken =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJST0xFX0FETUlOLFJPTEVfVVNFUiIsImV4cCI6MTY5NTczMjU0NX0.9I6dMIt3Ir3FKS2mwOJlU6_HRIjMQbgRYCH8VUVr9B2SgVpUg6EMvZ2aGQU5DTkipsP7ZcslSduPSTSxCjn4qw"

    // Create a RestTemplate instance
    val restTemplate = RestTemplateBuilder().build()

    // Create HttpHeaders and set the bearer token
    val headers = HttpHeaders()
    headers.contentType = MediaType.APPLICATION_JSON
    headers.set("Authorization", "Bearer $bearerToken")

    // Create a RequestEntity with the headers
    val requestEntity = RequestEntity<Any>(headers, HttpMethod.GET, URI(url1))

    // Perform the HTTP request
    val responseEntity = restTemplate.exchange(requestEntity, String::class.java)

    if (responseEntity.statusCode.is2xxSuccessful) {
        val jsonData = responseEntity.body

        if (jsonData != null) {
            // Parse the JSON data using Jackson's ObjectMapper
            val objectMapper = ObjectMapper()
            val data = objectMapper.readValue(jsonData, UpdateResponse::class.java)

            // Resolve remote file URLs and update the path
            data.updates.forEach { it.path = resolveRemoteFileUrl(url2, it.path) }
            data.deletes.forEach { it.path = resolveRemoteFileUrl(url2, it.path) }

            // Calculate total sizes
            val updateTotalSize = calculateTotalSize(data.updates)
            val deleteTotalSize = calculateTotalSize(data.deletes)

            println("Updated JSON:")
            println(objectMapper.writeValueAsString(data))
            println("Total Update Size: $updateTotalSize")
            println("Total Delete Size: $deleteTotalSize")
        }
    }
}

fun resolveRemoteFileUrl(baseUrl: String, path: String): String {
    // You can use your preferred method to combine the base URL and path
    // For simplicity, we'll just concatenate them here
    return "$baseUrl$path"
}

fun calculateTotalSize(files: List<FileResponse>): Long {
    return files.sumOf { getFileSize(it.path) }
}

fun getFileSize(fileUrl: String): Long {
    try {
        val url = URL(fileUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "HEAD"
        connection.connect()

        if (connection.responseCode == HttpURLConnection.HTTP_OK) {
            // The "Content-Length" header contains the file size in bytes
            val contentLength = connection.getHeaderField("Content-Length")
            if (contentLength != null) {
                return contentLength.toLong()
            }
        }
    } catch (e: IOException) {
        // Handle exceptions here (e.g., URL not found, connection issues)
        e.printStackTrace()
    }

    // Return -1 if the file size couldn't be determined
    return -1
}
