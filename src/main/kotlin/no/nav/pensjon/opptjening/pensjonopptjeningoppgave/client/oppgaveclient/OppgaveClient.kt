package no.nav.pensjon.opptjening.pensjonopptjeningoppgave.client.oppgaveclient

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class OppgaveClient(@Qualifier("oppgaveRestTemplate") restTemplate: RestTemplate, @Value("\${OPPGAVE_URL}") url: String) {


    fun lagOppgave(lagOppgaveRequest: LagOppgaveRequest) {
        TODO("Not yet implemented")
    }

    /**
    @Retryable(
    include = [HttpStatusCodeException::class],
    backoff = Backoff(delay = 1000L, maxDelay = 140000L, multiplier = 3.0)
    )
    fun lagOppgave(fnr: String): LanguageCode {
    try {
    val response = restTemplate.exchange(
    url,
    HttpMethod.GET,
    HttpEntity("", createHeaders(fnr)),
    KrrResponse::class.java
    )
    counterSuccessfulKrr.increment()
    return response.body?.getLanguageCode() ?: LanguageCode.standardLanguage()

    } catch (e: HttpStatusCodeException) {
    if (e.statusCode == HttpStatus.NOT_FOUND) {
    counterNotFoundKrr.increment()
    return LanguageCode.standardLanguage()
    } else {
    logger.error("En feil oppstod ved kall mot KRR: ${e.statusCode.value()} ${e.responseBodyAsString}")
    println("En feil oppstod ved kall mot KRR: ${e.statusCode.value()} ${e.responseBodyAsString}")
    counterFailedKrr.increment()
    throw e
    }
    }

    }
     */

}