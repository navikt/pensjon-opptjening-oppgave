package no.nav.pensjon.opptjening.pensjonopptjeningoppgave.client.pdl

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
import java.net.URI
import java.time.Duration

@Configuration
class PdlClientConfig {

    @Bean("azureAdConfigPdl")
    @Profile("dev-gcp", "prod-gcp")
    fun azureAdConfigPdl(
        @Value("\${AZURE_APP_CLIENT_ID}") azureAppClientId: String,
        @Value("\${AZURE_APP_CLIENT_SECRET}") azureAppClientSecret: String,
        @Value("\${PDL_API_ID}") pgiEndringApiId: String,
        @Value("\${AZURE_APP_WELL_KNOWN_URL}") wellKnownUrl: String,
    ) = AzureAdVariableConfig(
        azureAppClientId = azureAppClientId,
        azureAppClientSecret = azureAppClientSecret,
        targetApiId = pgiEndringApiId,
        wellKnownUrl = wellKnownUrl,
    )

    @Bean("tokenProviderPdl")
    @Profile("dev-gcp", "prod-gcp")
    fun tokenProviderPdl(@Qualifier("azureAdConfigPdl") azureAdVariableConfig: AzureAdVariableConfig): TokenProvider = AzureAdTokenProvider(azureAdVariableConfig)

    @Bean
    fun pdlInterceptor(@Qualifier("tokenProviderPdl") tokenProvider: TokenProvider) = PdlInterceptor(tokenProvider)

    @Bean("pdlRestTemplate")
    fun pdlRestTemplate(@Value("\${PDL_URL}") url: String, pdlInterceptor: PdlInterceptor): RestTemplate = RestTemplateBuilder()
        .setConnectTimeout(Duration.ofMillis(1000))
        .rootUri(url)
        .additionalInterceptors(pdlInterceptor)
        .build()

    @Bean
    fun pdlClient(@Qualifier("pdlRestTemplate") restTemplate: RestTemplate, @Value("\${PDL_URL}") url: String): PdlClient {
        return PdlClient(restTemplate, URI(url))
    }
}