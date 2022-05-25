package no.nav.pensjon.opptjening.pensjonopptjeningoppgave

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics = ["pensjonopptjening.pensjon-opptjening-oppgave-topic"])
@ActiveProfiles("local")
class PensjonOpptjeningOppgaveApplicationTests {

    @Test
    fun contextLoads() {
    }

}
