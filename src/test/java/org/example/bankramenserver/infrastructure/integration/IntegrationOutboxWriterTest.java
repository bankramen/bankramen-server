package org.example.bankramenserver.infrastructure.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bankramenserver.domain.category.domain.Category;
import org.example.bankramenserver.domain.transaction.event.PaymentTransactionRecordedEvent;
import org.example.bankramenserver.infrastructure.integration.domain.IntegrationConnectionProperties;
import org.example.bankramenserver.infrastructure.integration.domain.IntegrationOutbox;
import org.example.bankramenserver.infrastructure.integration.domain.IntegrationOutboxRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IntegrationOutboxWriterTest {

    @Mock
    private IntegrationOutboxRepository outboxRepository;

    @Test
    void recordsOnePendingDeliveryForEachEnabledSubscribedConnection() throws Exception {
        IntegrationConnectionProperties properties = new IntegrationConnectionProperties();
        properties.setConnections(List.of(
                new IntegrationConnectionProperties.Connection(
                        "hermes-personal", "yuseob-finance-assistant", "HERMES_WEBHOOK", true,
                        List.of("transaction.created"), "http://localhost:8644/webhooks/bankramen-transactions", "test-secret"
                ),
                new IntegrationConnectionProperties.Connection(
                        "disabled", "disabled-agent", "HERMES_WEBHOOK", false,
                        List.of("transaction.created"), "http://localhost/disabled", "test-secret"
                )
        ));
        IntegrationOutboxWriter writer = new IntegrationOutboxWriter(outboxRepository, properties, new ObjectMapper());
        UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID transactionId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        writer.record(new PaymentTransactionRecordedEvent(
                userId, transactionId, "스타벅스", 4500L, Category.CAFE_SNACK, LocalDate.of(2026, 7, 11)
        ));

        ArgumentCaptor<IntegrationOutbox> captor = ArgumentCaptor.forClass(IntegrationOutbox.class);
        verify(outboxRepository).save(captor.capture());
        IntegrationOutbox outbox = captor.getValue();
        JsonNode payload = new ObjectMapper().readTree(outbox.getPayload());
        assertThat(outbox.getConnectionId()).isEqualTo("hermes-personal");
        assertThat(outbox.getEventId()).isEqualTo(transactionId.toString());
        assertThat(outbox.isPending()).isTrue();
        assertThat(payload.path("event_type").asText()).isEqualTo("transaction.created");
        assertThat(payload.path("transaction").path("amount").asLong()).isEqualTo(4500L);
    }
}
