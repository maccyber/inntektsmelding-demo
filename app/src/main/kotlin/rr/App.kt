package rr

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import no.nav.helse.rapids_rivers.RapidApplication

fun main() {
    val environment = setUpEnvironment()
    val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
    }
    RapidApplication.create(environment.raw).apply {
        BrregLøser(this, BrregClient(httpClient, environment.brregUrl))
        AaregLøser(this, AaregClient(httpClient, environment.aaregUrl))
        InntektsmeldingAkkumulator(this)
    }.start()
}
