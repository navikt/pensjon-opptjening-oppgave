package no.nav.pensjon.opptjening.pensjonopptjeningoppgave.client.oppgaveclient

import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.interceptor.TokenInterceptor
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.client.RestTemplate
import pensjon.opptjening.azure.ad.client.AzureAdTokenProvider
import pensjon.opptjening.azure.ad.client.AzureAdVariableConfig
import pensjon.opptjening.azure.ad.client.TokenProvider
import java.time.Duration

@Configuration
class OppgaveClientConfig {

    @Bean("azureAdConfigOppgave")
    @Profile("dev-gcp", "prod-gcp")
    fun azureAdConfigOppgave(
        @Value("\${AZURE_APP_CLIENT_ID}") azureAppClientId: String,
        @Value("\${AZURE_APP_CLIENT_SECRET}") azureAppClientSecret: String,
        @Value("\${BREVBAKER_API_ID}") pgiEndringApiId: String,
        @Value("\${AZURE_APP_WELL_KNOWN_URL}") wellKnownUrl: String,
    ) = AzureAdVariableConfig(
        azureAppClientId = azureAppClientId,
        azureAppClientSecret = azureAppClientSecret,
        targetApiId = pgiEndringApiId,
        wellKnownUrl = wellKnownUrl,
    )

    @Bean("tokenProviderOppgave")
    @Profile("dev-gcp", "prod-gcp")
    fun tokenProviderOppgave(@Qualifier("azureAdConfigOppgave") azureAdVariableConfig: AzureAdVariableConfig): TokenProvider = AzureAdTokenProvider(azureAdVariableConfig)

    @Bean("oppgaveTokenInterceptor")
    fun oppgaveTokenInterceptor(@Qualifier("tokenProviderOppgave") tokenProvider: TokenProvider): TokenInterceptor = TokenInterceptor(tokenProvider)

    @Bean("oppgaveRestTemplate")
    fun oppgaveRestTemplate(@Value("\${OPPGAVE_URL}") url: String, @Qualifier("oppgaveTokenInterceptor") tokenInterceptor: TokenInterceptor) = RestTemplateBuilder()
        .setConnectTimeout(Duration.ofMillis(1000))
        .rootUri(url)
        .additionalInterceptors(tokenInterceptor)
        .build()

    @Bean
    fun oppgaveClient(@Qualifier("oppgaveRestTemplate") restTemplate: RestTemplate,@Value("\${OPPGAVE_URL}") url: String): OppgaveClient {
        return OppgaveClient(restTemplate,url)
    }
}