package no.nav.pensjon.opptjening.pensjonopptjeningoppgave.client.pdl.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import no.nav.pensjon.opptjening.pensjonopptjeningoppgave.client.pdl.ResponseError

@JsonIgnoreProperties(ignoreUnknown = true)
internal data class GeografiskTilknytningResponse(
    val data: GeografiskTilknytningResponseData? = null,
    val errors: List<ResponseError>? = null
)

internal data class GeografiskTilknytningResponseData(
    @JsonProperty("hentGeografiskTilknytning")
    val geografiskTilknytning: GeografiskTilknytning? = null
)

data class GeografiskTilknytning(
    val gtType: GtType,
    val gtKommune: String? = null,
    val gtBydel: String? = null,
    val gtLand: String? = null
)

enum class GtType {
    KOMMUNE,
    BYDEL,
    UTLAND,
    UDEFINERT
}