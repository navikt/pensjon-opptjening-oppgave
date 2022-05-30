package no.nav.pensjon.opptjening.pensjonopptjeningoppgave.client.pen

import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.interceptor.TokenInterceptor
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import pensjon.opptjening.azure.ad.client.AzureAdTokenProvider
import pensjon.opptjening.azure.ad.client.AzureAdVariableConfig
import pensjon.opptjening.azure.ad.client.TokenProvider
import java.time.Duration

@Configuration
class PenRestTemplateConfig {
    @Bean("azureAdConfigPen")
    @Profile("dev-gcp", "prod-gcp")
    fun azureAdConfigPen(
        @Value("\${AZURE_APP_CLIENT_ID}") azureAppClientId: String,
        @Value("\${AZURE_APP_CLIENT_SECRET}") azureAppClientSecret: String,
        @Value("\${PEN_API_ID}") penApiId: String,
        @Value("\${AZURE_APP_WELL_KNOWN_URL}") wellKnownUrl: String,
    ) = AzureAdVariableConfig(
        azureAppClientId = azureAppClientId,
        azureAppClientSecret = azureAppClientSecret,
        targetApiId = penApiId,
        wellKnownUrl = wellKnownUrl,
    )

    @Bean
    @Profile("dev-gcp", "prod-gcp")
    fun tokenProviderPen(@Qualifier("azureAdConfigPen") azureAdVariableConfig: AzureAdVariableConfig): TokenProvider = AzureAdTokenProvider(azureAdVariableConfig)

    @Bean
    fun penTokenInterceptor(@Qualifier("tokenProviderPen") tokenProvider: TokenProvider): TokenInterceptor = TokenInterceptor(tokenProvider)

    @Bean
    fun penRestTemplate(@Qualifier("penTokenInterceptor") tokenInterceptor: TokenInterceptor) = RestTemplateBuilder()
        .setConnectTimeout(Duration.ofMillis(1000))
        .additionalInterceptors(tokenInterceptor)
        .build()
}