package no.nav.pensjon.opptjening.pensjonopptjeningoppgave.util

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper


fun Any.toJson(): String = jacksonObjectMapper()
    .registerModule(JavaTimeModule())
    .writerWithDefaultPrettyPrinter()
    .writeValueAsString(this)