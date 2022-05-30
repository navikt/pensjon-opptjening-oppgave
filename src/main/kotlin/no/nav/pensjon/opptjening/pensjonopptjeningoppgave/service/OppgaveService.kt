package no.nav.pensjon.opptjening.pensjonopptjeningoppgave.service

import no.nav.pensjon.opptjening.pensjonopptjeningoppgave.client.oppgaveclient.LagOppgaveRequest
import no.nav.pensjon.opptjening.pensjonopptjeningoppgave.client.oppgaveclient.OppgaveClient
import no.nav.pensjon.opptjening.pensjonopptjeningoppgave.client.pdl.PdlClient
import no.nav.pensjon.opptjening.pensjonopptjeningoppgave.consumer.OppgaveHendelse
import org.springframework.stereotype.Service

@Service
class OppgaveService(
    private val pdlClient: PdlClient,
    private val oppgaveClient: OppgaveClient,
) {

    fun lagOppgave(oppgaveHendelse: OppgaveHendelse) {
        val aktoerId = pdlClient.hentAktorId(oppgaveHendelse.fnr)
        val lagOppgaveRequest = createLagOppgaveRequest(aktoerId, oppgaveHendelse)
        oppgaveClient.lagOppgave(lagOppgaveRequest)
    }

    private fun createLagOppgaveRequest(aktoerId: String, oppgaveHendelse: OppgaveHendelse): LagOppgaveRequest {
        return LagOppgaveRequest(
            aktoerId = aktoerId,
            sakNr = oppgaveHendelse.penSakId,
            journalpostId = oppgaveHendelse.journalpostId,
            tildeltEnhetsnr = oppgaveHendelse.tildeltEnhetsnr!!, //TODO kall pen
            oppgaveType = oppgaveHendelse.oppgaveType
        )
    }
}