package no.nav.pensjon.opptjening.pensjonopptjeningoppgave.client.pdl

import no.nav.pensjon.opptjening.pensjonopptjeningoppgave.client.pdl.model.GeografiskTilknytningResponse
import no.nav.pensjon.opptjening.pensjonopptjeningoppgave.client.pdl.model.GraphqlRequest
import no.nav.pensjon.opptjening.pensjonopptjeningoppgave.client.pdl.model.IdenterResponse
import no.nav.pensjon.opptjening.pensjonopptjeningoppgave.client.pdl.model.Variables
import org.springframework.http.HttpEntity
import org.springframework.web.client.RestTemplate
import java.net.URI


class PdlClient(
    private val pdlRestTemplate: RestTemplate,
    private val url: URI,
) {
    /**
     * Oppretter GraphQL Query for uthentig av en person sin AktørID.
     *
     * @param ident: Personen sin ident (fnr). Legges til som variabel på spørringen.
     *
     * @return GraphQL-objekt [IdenterResponse] som inneholder data eller error.
     */
    internal fun hentAktorId(ident: String): IdenterResponse? {
        val query = getGraphqlResource("/graphql/hentAktorId.graphql")
        val request = GraphqlRequest(query, Variables(ident))

        return pdlRestTemplate.postForObject(url, HttpEntity(request), IdenterResponse::class.java)
    }


    /**
     * Oppretter GraphQL Query for uthentig av en person sin geografiske tilknytning.
     *
     * @param ident: Personen sin ident (fnr). Legges til som variabel på spørringen.
     *
     * @return GraphQL-objekt [GeografiskTilknytningResponse] som inneholder data eller error.
     */
    internal fun hentGeografiskTilknytning(ident: String): GeografiskTilknytningResponse? {
        val query = getGraphqlResource("/graphql/hentGeografiskTilknytning.graphql")
        val request = GraphqlRequest(query, Variables(ident))

        return pdlRestTemplate.postForObject(url, HttpEntity(request), GeografiskTilknytningResponse::class.java)
    }

    private fun getGraphqlResource(file: String): String =
        javaClass.getResource(file).readText().replace(Regex("[\n\t]"), "")
}