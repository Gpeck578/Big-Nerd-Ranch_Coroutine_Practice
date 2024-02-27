package org.example

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import kotlinx.coroutines.*

private const val BASE_URL = "http://kotlin-book.bignerdranch.com/2e"
private const val FLIGHT_ENDPOINT = "$BASE_URL/flight"
private const val LOYALTY_ENDPOINT = "$BASE_URL/loyalty"

suspend fun fetchFlight(
    passengerName: String,
    start: Long
): FlightStatus = coroutineScope {
    val client = HttpClient(CIO)

    val flightResponse = async {
        println("Started fetching flight info: ${System.currentTimeMillis() - start}")
        client.get<String>(FLIGHT_ENDPOINT).also {
            println("Finished fetching flight info: ${System.currentTimeMillis() - start}")
        }
    }

    val loyaltyResponse = async {
        println("Started fetching loyalty info: ${System.currentTimeMillis() - start}")
        client.get<String>(LOYALTY_ENDPOINT).also {
            println("Finished fetching loyalty info: ${System.currentTimeMillis() - start}")
        }
    }
    delay(500)
    println("Combining flight data: ${System.currentTimeMillis() - start}")

    FlightStatus.parse(
        passengerName = passengerName,
        flightResponse = flightResponse.await(),
        loyaltyResponse = loyaltyResponse.await()
    )
}

fun main() {
    val start = System.currentTimeMillis()

    runBlocking {
        println("Started: ${System.currentTimeMillis() - start}")
        launch {
            val flight = fetchFlight("Madrigal", start)
            println(flight)
        }
        println("Finished: ${System.currentTimeMillis() - start}")
    }
}