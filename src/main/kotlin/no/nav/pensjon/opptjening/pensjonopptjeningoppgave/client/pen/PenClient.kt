package no.nav.pensjon.opptjening.pensjonopptjeningoppgave.client.pen

import no.nav.pensjon.opptjening.pensjonopptjeningoppgave.client.pen.model.PenEnhetResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import java.net.URL

// TODO Secure logs
private val logger = LoggerFactory.getLogger(PenClient::class.java)

@Component
class PenClient(
    @Qualifier("penRestTemplate") private val restTemplate: RestTemplate,
    @Value("\${PEN_ENHET_URL}") url: String,
) {

    private val baseUrl = URL(url)

    fun getPenEnhet(penSakId: Long): String? {
        try {
            val responseEntity = restTemplate.getForEntity(URL(baseUrl, "$penSakId").toURI(), PenEnhetResponse::class.java)
            return responseEntity.body?.enhetId
        } catch (e: HttpStatusCodeException) {
            logger.error("Failed with status ${e.statusCode} when retrieving enhet from Pen on sak: $penSakId", e)
            throw e
        }
    }
}