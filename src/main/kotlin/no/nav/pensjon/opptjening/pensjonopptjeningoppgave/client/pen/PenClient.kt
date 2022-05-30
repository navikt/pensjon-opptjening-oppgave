package no.nav.pensjon.opptjening.pensjonopptjeningoppgave.client.pen

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.net.URL

@Component
class PenClient(
    @Qualifier("penRestTemplate") private val restTemplate: RestTemplate,
    @Value("\${PEN_ENHET_URL}") url: String,
) {

    private val baseUrl = URL(url)

    fun getPenEnhet(penSakId: Long): String? {
        val responseEntity = restTemplate.getForEntity(URL(baseUrl, "$penSakId").toURI(), PenEnhetResponse::class.java)
        return responseEntity.body?.enhetId
    }
}