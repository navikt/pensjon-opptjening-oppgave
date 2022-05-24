package no.nav.pensjon.opptjening.pensjonopptjeningoppgave.client.pdl.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import no.nav.pensjon.opptjening.pensjonopptjeningoppgave.client.pdl.ResponseError

@JsonIgnoreProperties(ignoreUnknown = true)
internal data class IdenterResponse(
    val data: IdenterDataResponse? = null,
    val errors: List<ResponseError>? = null
)

internal data class IdenterDataResponse(
    val hentIdenter: HentIdenter?
)

internal data class HentIdenter(
    val identer: List<IdentInformasjon>
)

data class IdentInformasjon(
    val ident: String,
    val gruppe: IdentGruppe
)

enum class IdentGruppe {
    AKTORID,
    FOLKEREGISTERIDENT,
    NPID
}