package guru.springframework.spring6restmvc.listeners;

import guru.springframework.spring6restmvc.events.OrderPlacedEvent;
import guru.springframework.spring6restmvc.model.BeerOrderDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by jt, Spring Framework Guru.
 */
@ExtendWith(MockitoExtension.class)
public class OrderPlacedListenerInjected {

    @Mock
    KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    OrderPlacedListener orderPlacedListener;

    @Test
    void listen() {
        OrderPlacedEvent orderPlacedEvent = OrderPlacedEvent.builder()
                .beerOrderDTO(BeerOrderDTO.builder()
                        .id(UUID.randomUUID())
                        .build())
                .build();

        orderPlacedListener.listen(orderPlacedEvent);

        verify(kafkaTemplate).send(anyString(), any(OrderPlacedEvent.class));
    }
}
