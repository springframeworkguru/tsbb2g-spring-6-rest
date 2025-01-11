package guru.springframework.spring6restmvc.listeners;

import guru.springframework.spring6restmvc.events.OrderPlacedEvent;
import guru.springframework.spring6restmvc.model.BeerOrderDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class OrderPlacedListenerTest {

    KafkaTemplate<String, Object> kafkaTemplate;

    OrderPlacedListener orderPlacedListener;

    @BeforeEach
    void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);

        orderPlacedListener = new OrderPlacedListener(kafkaTemplate);
    }

    @Test
    void listen() {
        OrderPlacedEvent orderPlacedEvent = OrderPlacedEvent.builder()
                .beerOrderDTO(BeerOrderDTO.builder()
                        .id(UUID.randomUUID())
                        .build())
                .build();

        orderPlacedListener.listen(orderPlacedEvent);

        verify(kafkaTemplate, times(1)).send(anyString(), any(OrderPlacedEvent.class));
    }
}













