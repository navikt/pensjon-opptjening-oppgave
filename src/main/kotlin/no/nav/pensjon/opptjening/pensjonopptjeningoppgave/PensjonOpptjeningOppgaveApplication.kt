package no.nav.pensjon.opptjening.pensjonopptjeningoppgave

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry

@SpringBootApplication
@EnableRetry
class PensjonOpptjeningOppgaveApplication

fun main(args: Array<String>) {
    runApplication<PensjonOpptjeningOppgaveApplication>(*args)
}
