package org.example.bankramenserver.infrastructure.integration;

import com.sun.net.httpserver.HttpServer;
import org.example.bankramenserver.infrastructure.integration.domain.IntegrationConnectionProperties;
import org.example.bankramenserver.infrastructure.integration.domain.IntegrationOutbox;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class HermesWebhookConnectorTest {

    @Test
    void sendsSignedPayloadAndIdempotencyHeaderToConfiguredConnection() throws Exception {
        AtomicReference<String> body = new AtomicReference<>();
        AtomicReference<String> signature = new AtomicReference<>();
        AtomicReference<String> requestId = new AtomicReference<>();
        HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/hook", exchange -> {
            body.set(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
            signature.set(exchange.getRequestHeaders().getFirst("X-Hub-Signature-256"));
            requestId.set(exchange.getRequestHeaders().getFirst("X-Request-ID"));
            exchange.sendResponseHeaders(202, -1);
            exchange.close();
        });
        server.start();
        try {
            String payload = "{\"event_type\":\"transaction.created\"}";
            IntegrationOutbox outbox = IntegrationOutbox.pending(
                    "event-1", "hermes-personal", "HERMES_WEBHOOK", "transaction.created", payload
            );
            IntegrationConnectionProperties.Connection connection = new IntegrationConnectionProperties.Connection(
                    "hermes-personal", "consumer", "HERMES_WEBHOOK", true, List.of("transaction.created"),
                    "http://127.0.0.1:" + server.getAddress().getPort() + "/hook", "test-secret"
            );

            new HermesWebhookConnector().dispatch(outbox, connection);

            assertThat(body.get()).isEqualTo(payload);
            assertThat(signature.get()).isEqualTo(HermesWebhookConnector.signature(payload.getBytes(StandardCharsets.UTF_8), "test-secret"));
            assertThat(requestId.get()).isEqualTo("event-1");
        } finally {
            server.stop(0);
        }
    }

    @Test
    void signsTheExactUtf8PayloadBytesWithHmacSha256() {
        assertThat(HermesWebhookConnector.signature("payload".getBytes(), "test-secret"))
                .isEqualTo("sha256=2fcd0dbc44d5dd073ead5ea4b4d81cfd543e5de42e9c353f80452715e2b576a3");
    }
}
