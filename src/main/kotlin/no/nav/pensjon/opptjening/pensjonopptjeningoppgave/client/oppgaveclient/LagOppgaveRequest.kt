package no.nav.pensjon.opptjening.pensjonopptjeningoppgave.client.oppgaveclient

data class LagOppgaveRequest(
    val aktoerId: String,
    val sakNr: String,
    val journalpostId: String?,
    val tildeltEnhetsnr: String,
    val oppgaveType: String
)