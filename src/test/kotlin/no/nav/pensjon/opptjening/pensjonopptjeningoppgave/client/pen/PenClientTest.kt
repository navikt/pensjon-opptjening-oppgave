package no.nav.pensjon.opptjening.pensjonopptjeningoppgave.client.pen

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.util.toJson
import no.nav.pensjon.opptjening.pensjonopptjeningoppgave.MockTokenConfig
import no.nav.pensjon.opptjening.pensjonopptjeningoppgave.client.pen.model.EnhetNotFoundException
import no.nav.pensjon.opptjening.pensjonopptjeningoppgave.client.pen.model.PenEnhetResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import org.springframework.web.client.HttpStatusCodeException

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics = ["pensjonopptjening.pensjon-opptjening-oppgave-topic"])
@WireMockTest(httpPort = 4567)
internal class PenClientTest {
    @Autowired
    private lateinit var penClient: PenClient

    @Test
    fun `PenClient should return enhetId`() {
        val response = PenEnhetResponse(PEN_ENHET)
        stubPenEnhetEndpoint(response.toJson(), PEN_SAK_ID)
        assertEquals(PEN_ENHET, penClient.getPenEnhet(PEN_SAK_ID))
    }

    @Test
    fun `PenClient should throw EnhetNotFoundException when no data in response from pen`() {
        val response = PenEnhetResponse(PEN_ENHET)
        stubPenEnhetEndpoint(response.toJson(), OTHER_PEN_SAK_ID, status = HttpStatus.NO_CONTENT.value())
        assertThrows<EnhetNotFoundException> { penClient.getPenEnhet(PEN_SAK_ID) }
    }

    @Test
    fun `should use header Authorization equal to system token`() {
        stubPenEnhetEndpoint(PenEnhetResponse(PEN_ENHET).toJson(), PEN_SAK_ID)

        penClient.getPenEnhet(PEN_SAK_ID)

        WireMock.verify(1, WireMock.getRequestedFor(WireMock.urlPathEqualTo("/$PEN_SAK_ID"))
            .withHeader(HttpHeaders.AUTHORIZATION, WireMock.equalTo("Bearer ${MockTokenConfig.PEN_TOKEN}"))
        )
    }

    @Test
    fun `Should rethrow Status code exception when errorcode is returned from Pen`() {
        stubPenEnhetEndpoint("", PEN_SAK_ID, status = HttpStatus.INTERNAL_SERVER_ERROR.value())
        assertThrows<HttpStatusCodeException> { penClient.getPenEnhet(PEN_SAK_ID) }
    }

    private fun stubPenEnhetEndpoint(response: String, penSakId: Long, status: Int = HttpStatus.OK.value()) {
        WireMock.stubFor(
            WireMock.get(WireMock.urlEqualTo("/$penSakId"))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(status)
                        .withHeader("Content-Type", "application/json")
                        .withBody(response)
                )
        )
    }

    companion object {
        private const val PEN_ENHET = "1234321"
        private const val PEN_SAK_ID = 111111L
        private const val OTHER_PEN_SAK_ID = 111111L
    }
}