package no.nav.pensjon.opptjening.pensjonopptjeningoppgave

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pensjon.opptjening.azure.ad.client.TokenProvider
import pensjon.opptjening.azure.ad.client.mock.MockTokenProvider

@Configuration
class MockTokenConfig {

    companion object {
        const val OPPGAVE_TOKEN = "oppgave.oppgave.oppgave"
    }


    @Bean("tokenProviderOppgave")
    fun mockTokenProviderOppgave(): TokenProvider = MockTokenProvider(OPPGAVE_TOKEN)
}