package no.nav.pensjon.opptjening.pensjonopptjeningoppgave.client.pdl

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import no.nav.pensjon.opptjening.pensjonopptjeningoppgave.MockTokenConfig
import no.nav.pensjon.opptjening.pensjonopptjeningoppgave.client.pdl.model.PdlException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext


@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics = ["pensjonopptjening.pensjon-opptjening-oppgave-topic"])
@WireMockTest(httpPort = 4567)
internal class PdlClientTest {

    @Autowired
    private lateinit var pdlClient: PdlClient

    @Test
    fun `PDL should return aktoerId`() {
        stubPdl(PDL_AKTORID_HENTIDENTER_RESPONSE)
        assertEquals(AKTOER_ID, pdlClient.hentAktorId(FNR))
    }

    @Test
    fun `should use header Authorization and Nav-Consumer-Token equal to system token and Tema equal to PEN`() {
        stubPdl(PDL_AKTORID_HENTIDENTER_RESPONSE)

        pdlClient.hentAktorId(FNR)

        verify(1, postRequestedFor(urlPathEqualTo("/"))
            .withHeader(HttpHeaders.AUTHORIZATION, equalTo("Bearer ${MockTokenConfig.PDL_TOKEN}"))
            .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MediaType.APPLICATION_JSON_VALUE))
            .withHeader("Nav-Consumer-Token", equalTo("Bearer ${MockTokenConfig.PDL_TOKEN}"))
            .withHeader("Tema", equalTo("PEN"))
        )
    }

    @Test
    fun `Should throw PdlException if error from PDL`() {
        stubPdl(PDL_ERROR)
        assertThrows<PdlException> { pdlClient.hentAktorId(FNR) }
    }

    @Test
    fun `Should throw PdlException if required ident field is missing`() {
        stubPdl(PDL_ERROR_MISSING_IDENT_FIELDS)
        assertThrows<PdlException> {pdlClient.hentAktorId(FNR)}
    }

    private fun stubPdl(response: String) {
        stubFor(
            post(urlEqualTo("/"))
                .willReturn(
                    aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(response)
                )
        )
    }

    companion object {

    }
}

private const val FNR = "12345678901"
private const val AKTOER_ID = "444444"


private const val PDL_AKTORID_HENTIDENTER_RESPONSE = """
    {
        "data": {
            "hentIdenter": {
                "identer": [
                    {
                        "ident": "$AKTOER_ID",
                        "historisk": true,
                        "gruppe": "AKTORID"
                    }
                ]
            }
        }
    }
    """

private const val PDL_ERROR = """
    {
        "errors": [
            {
                "message": "Ikke autentisert",
                "locations": [
                    {
                        "line": 2,
                        "column": 5
                    }
                ],
                "path": [
                    "hentIdenter"
                ],
                "extensions": {
                    "code": "unauthenticated",
                    "classification": "ExecutionAborted"
                }
            }
        ],
        "data": {
            "hentIdenter": null
        }
    }
    """

private const val PDL_ERROR_MISSING_IDENT_FIELDS = """
    {
        "data": {
            "hentIdenter": null
        }
    }
    """