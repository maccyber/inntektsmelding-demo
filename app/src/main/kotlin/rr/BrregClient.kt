package rr

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable

@Serializable
data class Organisasjonsform(
    val kode: String
)

@Serializable
data class VirksomhetsInformasjon(
    val organisasjonsnummer: String,
    val navn: String,
    val organisasjonsform: Organisasjonsform,
    val slettedato: String? = null
)

class BrregClient(private val httpClient: HttpClient, private val brregUrl: String) {
    suspend fun getVirksomhetsNavn(virksomhetsNummer: String): VirksomhetsInformasjon =
        httpClient.get(brregUrl + virksomhetsNummer).body()
}
