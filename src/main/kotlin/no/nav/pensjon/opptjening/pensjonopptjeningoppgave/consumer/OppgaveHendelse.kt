package no.nav.pensjon.opptjening.pensjonopptjeningoppgave.consumer

class OppgaveHendelse(
    val fnr: String,
    val penSakId: String,
    val journalpostId: String?,
    val tildeltEnhetsnr: String?, // Bruker denne hvis den er satt ellers henter vi enhet fra sakId i PEN
    val oppgaveType: String,
)