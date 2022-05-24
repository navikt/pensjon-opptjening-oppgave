package no.nav.pensjon.opptjening.pensjonopptjeningoppgave.client.pdl.model

data class PdlException(val msg: String) : RuntimeException(msg)
