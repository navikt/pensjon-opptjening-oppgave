package no.nav.pensjon.opptjening.pensjonopptjeningoppgave

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pensjon.opptjening.azure.ad.client.TokenProvider
import pensjon.opptjening.azure.ad.client.mock.MockTokenProvider

@Configuration
class MockTokenConfig {

    companion object {
        const val OPPGAVE_TOKEN = "oppgave.oppgave.oppgave"
        const val PDL_TOKEN = "pdl.pdl.pdl"
    }

    @Bean("tokenProviderOppgave")
    fun mockTokenProviderOppgave(): TokenProvider = MockTokenProvider(OPPGAVE_TOKEN)

    @Bean("tokenProviderPdl")
    fun tokenProviderPdl(): TokenProvider = MockTokenProvider(PDL_TOKEN)
}