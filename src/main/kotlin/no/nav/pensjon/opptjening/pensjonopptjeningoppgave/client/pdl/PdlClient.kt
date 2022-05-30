package no.nav.pensjon.opptjening.pensjonopptjeningoppgave.client.pdl

import no.nav.pensjon.opptjening.pensjonopptjeningoppgave.client.pdl.model.*
import no.nav.pensjon.opptjening.pensjonopptjeningoppgave.util.toJson
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
        val request = GraphqlRequest(getGraphqlAktorIdQuery(), Variables(fnr))
        val response = pdlRestTemplate.postForObject(baseUrl, HttpEntity(request), IdenterResponse::class.java)

        response?.errors?.let { handleErrors(pdlErrorMessage(fnr, it)) }
        return response?.data?.hentIdenter?.identer?.firstNotNullOfOrNull { it.ident } ?: handleErrors(identNullErrorMessage(fnr))
    }

    private fun getGraphqlAktorIdQuery(): String {
        return javaClass.getResource(HENT_AKTOER_ID_FILE)!!.readText().replace(Regex("[\n\t]"), "")
    }

    private fun handleErrors(message: String): String {
        logger.error(message)
        throw PdlException(message)
    }

    private fun pdlErrorMessage(fnr: String, errors: List<ResponseError>): String {
        return "Oppslag mot PDL feilet for fnr $fnr med error:  \n ${errors.toJson()}"
    }
    private fun identNullErrorMessage(fnr: String): String {
        return "Oppslag mot PDL feilet for fnr $fnr. ident var null"
    }

    companion object{
        private const val HENT_AKTOER_ID_FILE = "/graphql/hentAktorId.graphql"
    }
}