package no.nav.pensjon.opptjening.pensjonopptjeningoppgave.client.pdl

import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.util.toJson
import no.nav.pensjon.opptjening.pensjonopptjeningoppgave.client.pdl.model.*
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.web.client.RestTemplate
import java.net.URI

// TODO Secure logs
private val logger = LoggerFactory.getLogger(PdlClient::class.java)

class PdlClient(
    private val pdlRestTemplate: RestTemplate,
    private val url: URI,
) {

    internal fun hentAktorId(fnr: String): String {
        val request = GraphqlRequest(getGraphqlResource("/graphql/hentAktorId.graphql"), Variables(fnr))
        val response = pdlRestTemplate.postForObject(url, HttpEntity(request), IdenterResponse::class.java)

        response?.errors?.let { handleErrors(pdlErrorMessage(fnr, it)) }
        return response?.data?.hentIdenter?.identer?.firstNotNullOfOrNull { it.ident } ?: handleErrors(identNullErrorMessage(fnr))
    }

    internal fun hentGeografiskTilknytning(ident: String): GeografiskTilknytningResponse? {
        val query = getGraphqlResource("/graphql/hentGeografiskTilknytning.graphql")
        val request = GraphqlRequest(query, Variables(ident))

        return pdlRestTemplate.postForObject(url, HttpEntity(request), GeografiskTilknytningResponse::class.java)
    }

    private fun getGraphqlResource(file: String): String = javaClass.getResource(file).readText().replace(Regex("[\n\t]"), "")

    private fun handleErrors(message: String): String {
        logger.error(message)
        throw PdlException(message)
    }

    private fun pdlErrorMessage(fnr: String, errors: List<ResponseError>) = "Oppslag mot PDL feilet for fnr $fnr med error:  \n ${errors.toJson()}"
    private fun identNullErrorMessage(fnr: String): String = "Oppslag mot PDL feilet for fnr $fnr. ident var null"
}