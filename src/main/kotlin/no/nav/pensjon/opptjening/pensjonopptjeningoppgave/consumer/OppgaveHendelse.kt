package no.nav.pensjon.opptjening.pensjonopptjeningoppgave.consumer

class OppgaveHendelse(
    val fnr: String,
    val sakNr: String,
    val journalpostId: String?,
    val tildeltEnhetsnr: String,
    val oppgaveType: String,
)