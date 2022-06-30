package rr

import kotlinx.coroutines.runBlocking
import no.nav.helse.rapids_rivers.JsonMessage
import no.nav.helse.rapids_rivers.MessageContext
import no.nav.helse.rapids_rivers.MessageProblems
import no.nav.helse.rapids_rivers.RapidsConnection
import no.nav.helse.rapids_rivers.River

class AaregLøser(rapidsConnection: RapidsConnection, val aaregClient: AaregClient) : River.PacketListener {
    companion object {
        internal const val behov = "Arbeidsforhold"
    }

    init {
        River(rapidsConnection).apply {
            validate {
                it.demandAll("@behov", listOf(behov))
                it.requireKey("@id")
                it.requireKey("fnr")
                it.rejectKey("@løsning")
            }
        }.register(this)
    }

    override fun onPacket(packet: JsonMessage, context: MessageContext) {
        println("Mottok melding: ${packet.toJson()}")
        val løsning = runBlocking {
            aaregClient.hentArbeidsforhold(packet["fnr"].asText())
        }
        packet.setLøsning(behov, løsning)
        context.publish(packet.toJson())
    }

    private fun JsonMessage.setLøsning(nøkkel: String, data: Any) {
        this["@løsning"] = mapOf(
            nøkkel to data
        )
    }

    override fun onError(problems: MessageProblems, context: MessageContext) {
        println("Forstod ikke $behov med melding\n$problems")
    }
}
