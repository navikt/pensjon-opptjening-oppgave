package no.nav.pensjon.opptjening.pensjonopptjeningoppgave.client.pdl

import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.util.toJson
import no.nav.pensjon.opptjening.pensjonopptjeningoppgave.client.pdl.model.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpEntity
import org.springframework.web.client.RestTemplate
import java.net.URI

// TODO Secure logs
private val logger = LoggerFactory.getLogger(PdlClient::class.java)

@Configuration
class PdlClient(
    @Qualifier("pdlRestTemplate") private val pdlRestTemplate: RestTemplate,
    @Value("\${PDL_URL}") url: String,
) {
    private val baseUrl = URI(url)

    internal fun hentAktorId(fnr: String): String {
        val request = GraphqlRequest(getGraphqlResource("/graphql/hentAktorId.graphql"), Variables(fnr))
        val response = pdlRestTemplate.postForObject(baseUrl, HttpEntity(request), IdenterResponse::class.java)

        response?.errors?.let { handleErrors(pdlErrorMessage(fnr, it)) }
        return response?.data?.hentIdenter?.identer?.firstNotNullOfOrNull { it.ident } ?: handleErrors(identNullErrorMessage(fnr))
    }

    internal fun hentGeografiskTilknytning(ident: String): GeografiskTilknytningResponse? {
        val query = getGraphqlResource("/graphql/hentGeografiskTilknytning.graphql")
        val request = GraphqlRequest(query, Variables(ident))

        return pdlRestTemplate.postForObject(baseUrl, HttpEntity(request), GeografiskTilknytningResponse::class.java)
    }

    private fun getGraphqlResource(file: String): String {
        return javaClass.getResource(file).readText().replace(Regex("[\n\t]"), "")
    }

    private fun handleErrors(message: String): String {
        logger.error(message)
        throw PdlException(message)
    }

    private fun pdlErrorMessage(fnr: String, errors: List<ResponseError>) = "Oppslag mot PDL feilet for fnr $fnr med error:  \n ${errors.toJson()}"
    private fun identNullErrorMessage(fnr: String): String = "Oppslag mot PDL feilet for fnr $fnr. ident var null"
}