package rr

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import kotlinx.serialization.Serializable

@Serializable
data class Arbeidsforhold(
    val arbeidsgiver: Arbeidsgiver
)

@Serializable
data class Arbeidsgiver(
    val type: String,
    val organisasjonsnummer: String?
)

val mockEngine = MockEngine {
    respond(
        content = ByteReadChannel("""[{"arbeidsgiver": {"type": "BEDR", "organisasjonsnummer": "222222222"}}]"""),
        status = HttpStatusCode.OK,
        headers = headersOf(HttpHeaders.ContentType, "application/json")
    )
}

class AaregClient(private val httpClient: HttpClient, private val aaregUrl: String) {
    val mockHttpClient = HttpClient(mockEngine) {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun hentArbeidsforhold(fnr: String): List<Arbeidsforhold> =
        mockHttpClient.get(aaregUrl + fnr).body()
}
