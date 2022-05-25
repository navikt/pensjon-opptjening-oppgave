package no.nav.pensjon.opptjening.pensjonopptjeningoppgave

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry

@SpringBootApplication
@EnableRetry
class PensjonOpptjeningOppgaveApplication

fun main(args: Array<String>) {
    logger.error("Test sikker logg. Heeeeeeyyyy")
    runApplication<PensjonOpptjeningOppgaveApplication>(*args)
}

private val logger = LoggerFactory.getLogger("tjenestekall")