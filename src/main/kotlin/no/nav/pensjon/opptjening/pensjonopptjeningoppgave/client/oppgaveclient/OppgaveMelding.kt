package no.nav.pensjon.opptjening.pensjonopptjeningoppgave.client.oppgaveclient

data class OppgaveMelding(
    val aktoerId: String,
    val sakNr: String,
    val journalpostId: String?,
    val tildeltEnhetsnr: String,
    val oppgaveType: String
)