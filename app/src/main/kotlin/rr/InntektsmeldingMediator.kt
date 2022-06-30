package rr

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import no.nav.helse.rapids_rivers.JsonMessage
import no.nav.helse.rapids_rivers.MessageContext
import no.nav.helse.rapids_rivers.MessageProblems
import no.nav.helse.rapids_rivers.RapidsConnection
import no.nav.helse.rapids_rivers.River
import java.util.UUID

@Serializable
data class Inntektsmelding(
    val Arbeidsforhold: List<Arbeidsforhold>?,
    val VirksomhetsInformasjon: VirksomhetsInformasjon?
)

class InntektsmeldingMediator(rapidsConnection: RapidsConnection) : River.PacketListener {
    // Bør ha en map cache med TTL for å unngå at meldinger blir liggende
    val inntektsMeldinger: MutableMap<UUID, Inntektsmelding> = mutableMapOf()

    init {
        River(rapidsConnection).apply {
            validate {
                it.demandKey("@løsning")
                it.demandValue("@event_name", "InntektsmeldingInformasjon")
                it.requireKey("@id")
            }
        }.register(this)
    }

    override fun onPacket(packet: JsonMessage, context: MessageContext) {
        println("Mottok melding: ${packet.toJson()}")
        val id = UUID.fromString(packet["@id"].asText())
        val løsning = packet["@løsning"].toString()

        val løsningsData = Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }.decodeFromString<Inntektsmelding>(løsning)

        inntektsMeldinger[id]?.let {
            inntektsMeldinger[id] = løsningsData.merge(it)
        } ?: run {
            inntektsMeldinger[id] = løsningsData
        }

        println(inntektsMeldinger)
        // Sjekk at all informasjon er satt ved f.eks. flagg eller nullcheck, og publiser melding tilbake til API
        // inntektsMeldinger.remove(id)
    }

    private fun JsonMessage.setLøsning(nøkkel: String, data: Any) {
        this["@løsning"] = mapOf(
            nøkkel to data
        )
    }

    override fun onError(problems: MessageProblems, context: MessageContext) {
        println("Forstod ikke melding\n$problems")
    }
}
