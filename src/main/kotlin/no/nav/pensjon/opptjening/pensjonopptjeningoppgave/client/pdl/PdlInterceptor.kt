package no.nav.pensjon.opptjening.pensjonopptjeningoppgave.client.pdl

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpRequest
import org.springframework.http.MediaType
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import pensjon.opptjening.azure.ad.client.TokenProvider

class PdlInterceptor(private val tokenProvider: TokenProvider) : ClientHttpRequestInterceptor {
    override fun intercept(request: HttpRequest, body: ByteArray, execution: ClientHttpRequestExecution): ClientHttpResponse {
        val token = tokenProvider.getToken()

        request.headers[HttpHeaders.AUTHORIZATION] = "Bearer $token"
        request.headers["Nav-Consumer-Token"] = "Bearer $token"

        request.headers[HttpHeaders.CONTENT_TYPE] = MediaType.APPLICATION_JSON_VALUE
        request.headers["Tema"] = "PEN"

        return execution.execute(request, body)
    }
}