package rr

fun setUpEnvironment(): Environment =
    Environment(
        raw = System.getenv(),
        brregUrl = System.getenv("BRREG_URL")
            ?: "https://data.brreg.no/enhetsregisteret/api/underenheter/",
        aaregUrl = System.getenv("AAREG_URL")
            ?: "https://modapp-q1.adeo.no/aareg-services"
    )

data class Environment(
    val raw: Map<String, String>,
    val brregUrl: String,
    val aaregUrl: String
)
