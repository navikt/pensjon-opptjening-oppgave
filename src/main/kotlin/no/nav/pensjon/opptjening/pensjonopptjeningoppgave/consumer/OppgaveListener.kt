package no.nav.pensjon.opptjening.pensjonopptjeningoppgave.consumer

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.micrometer.core.instrument.MeterRegistry
import no.nav.pensjon.opptjening.pensjonopptjeningoppgave.service.OppgaveService
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class OppgaveListener(
    private val oppgaveService: OppgaveService,
    @Value("OPPGAVE_TOPIC") private val topic: String,
    registry: MeterRegistry,
) {
    private val counterStartingListener = registry.counter("oppgave_consumer_init", "topic", topic)
    private val counterFailedRecords = registry.counter("oppgave_consumed_records", "status", "failed")
    private val counterProcessedRecords = registry.counter("oppgave_consumed_records", "status", "ok")

    @KafkaListener(
        containerFactory = "kafkaListenerContainerFactory",
        idIsGroup = false,
        topics = ["\${OPPGAVE_TOPIC}"],
        groupId = "\${OPPGAVE_GROUP_ID}"
    )
    fun consumeOmsorgPGodskriving(hendelse: String, consumerRecord: ConsumerRecord<String, String>, acknowledgment: Acknowledgment) {
        val oppgaveHendelse: OppgaveHendelse = jacksonObjectMapper().readValue(consumerRecord.value(), OppgaveHendelse::class.java)
        try {
            oppgaveService.lagOppgave(oppgaveHendelse)
            acknowledgment.acknowledge()
            counterProcessedRecords.increment()
        } catch (e: Exception) {
            counterFailedRecords.increment()
            logger.error(e.message, e)
            throw e
        }
    }

    @PostConstruct
    fun initMetrics() {
        logger.info("Starting to listen to topic $topic")
        counterStartingListener.increment()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(OppgaveListener::class.java)
    }
}